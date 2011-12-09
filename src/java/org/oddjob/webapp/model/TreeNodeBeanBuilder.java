/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.oddjob.webapp.jsf.TreeNodeBean;

/**
 * Build a TreeNodeBean hierarchy.
 * 
 * @author Rob Gordon
 */
public class TreeNodeBeanBuilder {

	private final JobInfoLookup lookup;

	private final String currentRefId;
	
	/**
	 * Constructor.
	 * 
	 * @param lookup Lookup to use for building.
	 */
	public TreeNodeBeanBuilder(JobInfoLookup lookup, String currentRefId) {
		if (lookup == null) {
			throw new NullPointerException("Lookup can't be null!");
		}
		this.lookup = lookup;
		this.currentRefId = currentRefId;
	}
	
	/**
	 * Build the root bean.
	 */
	public TreeNodeBean buildRoot() {
		TreeNodeBean root = new TreeNodeBean();
		root.setRefId(lookup.getRootRefId());
		buildBean(root);
		return root;
	}

	/**
	 * Recursively refresh the {@link TreeNodeBean} hierarchy.
	 * 
	 * @param bean
	 */
	public void refresh(TreeNodeBean bean) {
		String refId = bean.getRefId();
		
		if (refId.equals(currentRefId)) {
			bean.setStyleClass("selected");
		}
		else {
			bean.setStyleClass("");
		}
		
		NodeInfo nodeInfo = lookup.nodeInfoFor(refId);
		
		bean.setTreeNodeBeanBuilder(this);
		bean.setNodeName(nodeInfo.getNodeName());
		bean.setIconId(nodeInfo.getIconId());
		
		Map<String, TreeNodeBean> childMap = bean.getChildMap();
		bean.setHasChildren(nodeInfo.getHasChildren());
		if (childMap == null) {
			return;
		}
		
		Map<String, TreeNodeBean> newMap = 
			new LinkedHashMap<String, TreeNodeBean>();
		
		String[] childRefIds = nodeInfo.getChildRefIds();
		
		// compare the refIds to see if we can refresh the child node
		// or we need to build a new one.
		for (int i = 0; i < childRefIds.length; ++i) {
			TreeNodeBean child = (TreeNodeBean) childMap.get(childRefIds[i]); 
			if (child == null) {
				child = new TreeNodeBean();
				child.setRefId(childRefIds[i]);
				buildBean(child);
			}
			else {
				refresh(child);
			}
			newMap.put(child.getRefId(), child);				
		}
		
		bean.setChildMap(newMap);
	}
	
	/**
	 * Build the children.
	 * 
	 * @param parent
	 * @return
	 */
	public Map<String, TreeNodeBean> buildChildren(TreeNodeBean parent) {
		String refId = parent.getRefId();
		NodeInfo nodeInfo = lookup.nodeInfoFor(refId);
		
		Map<String, TreeNodeBean> children = new LinkedHashMap<String, TreeNodeBean>();
		String [] childRefIds = nodeInfo.getChildRefIds();
		for (int i = 0; i < childRefIds.length; ++i) {
			TreeNodeBean child = new TreeNodeBean();
			child.setRefId(childRefIds[i]);
			buildBean(child);
			children.put(child.getRefId(), child);
		}
		return children;		
	}
	
	/**
	 * Helper function to build the bean.
	 * 
	 * @param bean The bean we're building.
	 */
	void buildBean(TreeNodeBean bean) {				
		String refId = bean.getRefId();
		NodeInfo nodeInfo = lookup.nodeInfoFor(refId);
		bean.setTreeNodeBeanBuilder(this);
		bean.setNodeName(nodeInfo.getNodeName());
		bean.setIconId(nodeInfo.getIconId());
		bean.setHasChildren(nodeInfo.getHasChildren());
		
		if (refId.equals(currentRefId)) {
			bean.setStyleClass("selected");
		}
		else {
			bean.setStyleClass("");
		}
	}

	
}
