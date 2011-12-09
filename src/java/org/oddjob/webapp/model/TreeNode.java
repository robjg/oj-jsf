/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.model;

import java.util.ArrayList;
import java.util.List;

import org.oddjob.Structural;
import org.oddjob.monitor.context.ExplorerContext;
import org.oddjob.structural.StructuralEvent;
import org.oddjob.structural.StructuralListener;

/**
 * A model for a node in the job tree.
 * <p>
 * TODO: Revisiting this in 2011 it's not clear why we need a {@link TreenNodeBean}
 * which is the old Struts form bean, and this, and a {@link NodeInfo}.
 * 
 * @author Rob Gordon.
 */
class TreeNode implements StructuralListener {

	/** Used to create a unique reference number for each node. */
    private static int lastRefId = 0;

    /** Reference Id */
    private final String refId;

	/** The originating object */
	private final Object node;

	/** And keep track of children. */
	private final List<String> children = 
		new ArrayList<String>();

	/** So we can add and remove children. */
	private final JobInfoLookup lookup;
	
	private final ExplorerContext explorerContext;
	
	/**
	 * Constructor for the top level node.
	 * 
	 * @param explorerModel The ExplorerModel.
	 * @param lookup The lookup to add children to.
	 */
	TreeNode(Object node, ExplorerContext eContext, JobInfoLookup lookup) {		
		this.node = node;
		this.lookup = lookup;
		
		synchronized (TreeNode.class) {
			this.refId = Integer.toString(lastRefId++);
		}	    
		
		explorerContext = eContext;
	}

	/**
	 * Constructor for child nodes.
	 * 
	 * @param o The job.
	 * @param lookup The lookup to add children to.
	 */
	TreeNode(TreeNode parent, Object node) {
		this.node = node;
		this.lookup = parent.lookup;
		synchronized (TreeNode.class) {
			this.refId = Integer.toString(lastRefId++);
		}	    
		explorerContext = parent.explorerContext.addChild(node);
	}
	
	/**
	 * Get the underlying job for this node.
	 * 
	 * @return The job.
	 */
	Object getComponent() {
		return node;
	}
	
	/**
	 * Get the refIds of any children.
	 * 
	 * @return A String array of child refIds.
	 */
	String[] getChildRefIds() {
		return (String[]) this.children.toArray(new String[0]);
	}

	/**
	 * Get the refId of this node.
	 * 
	 * @return The refId.
	 */
	String getRefId() { 
		return refId;
	}
	
	/**
	 * Get the node name for this node.
	 * 
	 * @return The node name.
	 */
	String getNodeName() {
		return node.toString();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.oddjob.structural.StructuralListener#childAdded(org.oddjob.structural.StructuralEvent)
	 */
	public void childAdded(StructuralEvent event) {

		Object child = event.getChild();
		int index = event.getIndex();
		TreeNode childNode = new TreeNode(this, child);
		if (child instanceof Structural) {
			((Structural) child).addStructuralListener(childNode);
		}
		
		synchronized (children) {
			children.add(index, childNode.refId);
		}
		lookup.addChild(childNode); 
	}

	/*
	 *  (non-Javadoc)
	 * @see org.oddjob.structural.StructuralListener#childRemoved(org.oddjob.structural.StructuralEvent)
	 */
	public void childRemoved(StructuralEvent event) {
		int index = event.getIndex();
		String childRefId = null;
		synchronized (children) {
			childRefId = (String) children.remove(index);
		}

		lookup.removeChild(childRefId);
	}
	
	/**
	 * @return Returns the context.
	 */
	public ExplorerContext getExplorerContext() {
		return explorerContext;
	}
	
}