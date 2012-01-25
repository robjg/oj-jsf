/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oddjob.Iconic;
import org.oddjob.Stateful;
import org.oddjob.Structural;
import org.oddjob.images.IconEvent;
import org.oddjob.images.IconListener;
import org.oddjob.logging.ConsoleArchiver;
import org.oddjob.logging.LogArchiver;
import org.oddjob.logging.LogEvent;
import org.oddjob.logging.LogLevel;
import org.oddjob.logging.LogListener;
import org.oddjob.monitor.context.ExplorerContext;
import org.oddjob.monitor.model.Describer;
import org.oddjob.monitor.model.LogContextInialiser;
import org.oddjob.state.StateEvent;

/**
 * Provide a lookup facility for job information.
 * 
 * @author Rob Gordon.
 */
public class JobInfoLookup {

	/** Icon registry. */
	private final IconRegistry iconRegistry;
	
	/** Jobs by refId. */
	private Map<String, TreeNode> jobs = new HashMap<String, TreeNode>();
	
	/** The root refId */
	private String rootRefId; 

	/**
	 * Constructor.
	 * 
	 * @param iconRegistry The icon registry.
	 */
	public JobInfoLookup(IconRegistry iconRegistry) {
		if (iconRegistry == null) {
			throw new NullPointerException("Icon Registry must not be null");
		}
		this.iconRegistry = iconRegistry;
	}

	/**
	 * Set the root node. This method must be called before the lookup can be used.
	 * Setting logFormat and other such properties must be done before this method
	 * is called.
	 * 
	 * @param root The root node.
	 */
	public void setRoot(Object root, ExplorerContext eContext) {
		if (root == null) {
			throw new NullPointerException("Root node must not be null");
		}
		if (rootRefId != null) {
			throw new IllegalStateException("Can't change root.");			
		}

		TreeNode rootNode = new TreeNode(root, eContext, this);
		if (root instanceof Structural) {
			((Structural) root).addStructuralListener(rootNode);
		}		

		jobs.put(rootNode.getRefId(), rootNode);
		
		this.rootRefId = rootNode.getRefId();
	}
	
	/**
	 * Get the root reference id.
	 * 
	 * @return The reference id.
	 */
	public String getRootRefId() {
		return rootRefId;
	}
			
	/**
	 * Get the last state event for the given refId.
	 * 
	 * @param refId The refId.
	 * @return The last state event. Null if it isn't Stateful.
	 */
	public StateEvent stateFor(String refId) {
		Object object = objectFor(refId);
		if (object == null) {
			throw new IllegalStateException("[" + refId + "] does not exist!");
		}
		
		if (!(object instanceof Stateful)) {
			return null;
		}

		return ((Stateful) object).lastStateEvent();
	}
	
	/**
	 * Get {@link NodeInfo} for the given refId.
	 * 
	 * @param refId The refId.
	 * @return Node Info. Never null.
	 */
	public NodeInfo nodeInfoFor(String refId) {
		TreeNode treeNode = jobs.get(refId);
		Object object = objectFor(refId);
		
		String iconId = null;

		if (object instanceof Iconic) {
			class IL implements IconListener {
				IconEvent lastEvent;
				public void iconEvent(IconEvent event) {
					lastEvent = event;
				}
			}
			IL il = new IL();
			
			((Iconic) object).addIconListener(il);
			((Iconic) object).removeIconListener(il);
			iconId = il.lastEvent.getIconId();
			iconRegistry.register(iconId, (Iconic) object);
		}
		
		return new NodeInfo(treeNode.getNodeName(),
				treeNode.getChildRefIds(), 
				iconId);
	}
		
	/**
	 * Get a map of properties for the properties tab.
	 * 
	 * @param refId The refId.
	 * @return A map of properties. May be null if the node has been
	 * destroyed by Oddjob.
	 */
	public Map<String, String> propertiesFor(String refId) {
		Object object = objectFor(refId);
		if (object == null) {
			return null;
		}
		return Describer.describe(object);
	}
	
	/**
	 * Provide a list of console LogEvents.
	 * 
	 * @param refId The refId of the job.
	 * @return A list of LogEvent objects.
	 */
	public List<LogEvent> consoleEventsFor(String refId) {
		TreeNode treeNode = (TreeNode) jobs.get(refId);
		if (treeNode == null) {
			throw new IllegalStateException("[" + refId + "] does not exist!");
		}
		
		class LL implements LogListener {
			List<LogEvent> list = new ArrayList<LogEvent>();
			public void logEvent(LogEvent logEvent) {
				list.add(logEvent);
			}
		}
		
		LL ll = new LL();
		Object object = treeNode.getComponent();
		ConsoleArchiver consoleArchiver = 
			(ConsoleArchiver) treeNode.getExplorerContext().getValue(
					LogContextInialiser.CONSOLE_ARCHIVER);
		consoleArchiver.addConsoleListener(ll, object, -1, 1000);
		consoleArchiver.removeConsoleListener(ll, object);
		return ll.list;
	}

	/**
	 * Provide a list of logger LogEvents.
	 * 
	 * @param refId The refId of the job.
	 * @return A list of LogEVent objects.
	 */
	public List<LogEvent> logEventsFor(String refId) {
		TreeNode treeNode = (TreeNode) jobs.get(refId);
		if (treeNode == null) {
			throw new IllegalStateException("[" + refId + "] does not exist!");
		}
		
		class LL implements LogListener {
			List<LogEvent> list = 
				new ArrayList<LogEvent>();
			
			public void logEvent(LogEvent logEvent) {
				list.add(logEvent);
			}
		}
		
		LL ll = new LL(); 
		
		Object object = treeNode.getComponent();
		LogArchiver logArchiver = 
			(LogArchiver) treeNode.getExplorerContext().getValue(
					LogContextInialiser.LOG_ARCHIVER);
		logArchiver.addLogListener(ll, object, LogLevel.DEBUG, -1, 1000);
		logArchiver.removeLogListener(ll, object);
		return ll.list;
	}

	/**
	 * Get the {@link WebJobActions} for a node.
	 * 
	 * @param refId
	 * @return
	 */
	public WebJobActions actionsFor(String refId) {
		WebJobActions actions = new WebJobActions();
		if (refId != null) {
			TreeNode treeNode = (TreeNode) jobs.get(refId);
			if (treeNode != null) {
				actions.select(treeNode.getComponent(), 
						treeNode.getExplorerContext());
			}
		}
		return actions;
	}
	
	/**
	 * Helper function to resolve an object (the job) from the
	 * refId.
	 * 
	 * @param refId The refId.
	 * 
	 * @return The Job.
	 * 
	 * @throws IllegalStateException If the refId doesn't 
	 * reference a job.
	 */
	Object objectFor(String refId) throws IllegalStateException {
		TreeNode treeNode = (TreeNode) jobs.get(refId);
		if (treeNode == null) {
			throw new IllegalStateException("[" + refId + "] does not exist!");
		}
		return treeNode.getComponent();
	}
	
	/**
	 * Used by TreeNode to add itself to the lookup.
	 * 
	 * @param treeNode The TreeNode.
	 */
	void addChild(TreeNode treeNode) {
		jobs.put(treeNode.getRefId(), treeNode);
	}
	
	/**
	 * Used by TreeNode to remove the node.
	 * 
	 * @param refId The name of the node.
	 */
	void removeChild(String refId) {
		jobs.remove(refId);
	}

	/**
	 * The number of jobs cached.
	 * 
	 * @return
	 */
	public int getJobCount() {
		return jobs.size();
	}
	
	/**
	 * Free up resources. 
	 *
	 */
	public void destroy() {
		jobs.clear();
	}
}
