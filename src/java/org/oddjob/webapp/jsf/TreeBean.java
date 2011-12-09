/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.apache.log4j.Logger;
import org.oddjob.webapp.WebappConstants;
import org.oddjob.webapp.model.JobInfoLookup;
import org.oddjob.webapp.model.TreeNodeBeanBuilder;

/**
 * Backing bean for the tree. This provides the root {@link TreeNodeBean} 
 * from which the tree is built.
 * <p>
 * The bean is a session scoped bean so the tree expansion state and select
 * node are preserved between requests.
 * 
 * @author Rob Gordon.
 */
@SessionScoped
@ManagedBean(name="tree")
public class TreeBean {
	
	private static final Logger logger = Logger.getLogger(TreeBean.class);
	
	/** The root. */
	private TreeNodeBean root;
	
	/** The selected nodes reference Id. */
	private transient String selectedRefId;

	public TreeBean() {
		TreeNodeBeanBuilder builder = createNodeBuilder();
		
		logger.debug("Building tree.");
			
		this.root = builder.buildRoot();			
	}
	
	/** 
	 * Capture the preRenderView event to refresh tree.
	 * 
	 * @return Always null.
	 */
	public void refresh(ComponentSystemEvent event) {
		TreeNodeBeanBuilder builder = createNodeBuilder();
		
		logger.debug("Refreshing tree.");
		
		builder.refresh(this.root);		
	}
	
	/**
	 * Getter for the root.
	 * 
	 * @return
	 */
	public TreeNodeBean getRoot() {
		if (root == null) {
			logger.error("No root - need a refresh first.");
		}
		return this.root;
	}

	/**
	 * Getter for the selected node reference id.
	 * 
	 * @return
	 */
	public String getSelectedRefId() {
		return selectedRefId;
	}

	/**
	 * Setter for the selected node reference id.
	 * 
	 * @param selectedRefId
	 */
	public void setSelectedRefId(String selectedRefId) {
		this.selectedRefId = selectedRefId;
		logger.debug("Setting selectedRefId to " + selectedRefId);
	}

	/**
	 * Create a {@link TreeNodeBeanBuilder} to build the tree.
	 * 
	 * @return Never null.
	 */
	private TreeNodeBeanBuilder createNodeBuilder() {
		
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance(
				).getExternalContext().getApplicationMap();
		
		JobInfoLookup lookup = (JobInfoLookup) sessionMap.get(
				WebappConstants.DETAIL_LOOKUP);
		
		TreeNodeBeanBuilder builder = new TreeNodeBeanBuilder(
				lookup, selectedRefId);
		
		return builder;
	}
}