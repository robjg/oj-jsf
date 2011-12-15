/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.oddjob.webapp.model.TreeNodeBeanBuilder;

/**
 * A wrapper for a TreeNode to keep the backing bean as simple
 * as possible.
 * 
 * @author rob
 */
public class TreeNodeBean {
	
	private transient TreeNodeBeanBuilder builder;
	
	/** The reference id for this node. */
	private String refId;
	
	/** The node name */
	private transient String nodeName;
	
	/** The collection of child TreeNodeBeans. */
	private Map<String, TreeNodeBean> childCollection;
	
	/** Has this node been expanded */
	private boolean showChildren;
	
	/** The icon id of the node */
	private transient String iconId;
	
	/** Does this node have children. */
	private boolean hasChildren;
	
	/** Used for the selected node. */
	private String styleClass;
	
	public void setTreeNodeBeanBuilder(TreeNodeBeanBuilder builder){
		this.builder = builder;
	}
	
	/**
	 *  return the collection of children 
	 */
	public Collection<TreeNodeBean> getChildren() {
		if (childCollection == null) {
			return null;
		}
		return new ArrayList<TreeNodeBean>(childCollection.values());
	}

	public void setChildMap(Map<String, TreeNodeBean> childMap) {
		this.childCollection = childMap;
	}
	
	public Map<String, TreeNodeBean> getChildMap() {
		return childCollection;
	}
	
	/**
	 * The node name property.
	 * 
	 * @return The node name.
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * Set the node name.
	 * 
	 * @param nodeName The node name.
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * Getter method for the "showChildren" 
	 * property.
	 * 
	 * @return true if expanded.
	 */
	public boolean isShowChildren() {
		return this.showChildren;
	}

	/**
	 * Getter for the "hasChildren" property.
	 * 
	 * @return true if this node has child nodes 
	 */
	public boolean isHasChildren() {
		return hasChildren;
	}

	/**
	 * Called by the {@link TreeNodeBuilder}
	 * @param hasChildren
	 */
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
		if (!hasChildren) {
			showChildren = false;
		}
	}
	
	/**
	 */
	public void expand() {
		showChildren = true;
		if (childCollection == null) {
			// someones clicked on the expand children image
			// so create the children ready for the form.
			childCollection = builder.buildChildren(this);
		}
	}

	/**
	 */
	public void collapse() {
		showChildren = false;
		childCollection = null;
	}
	
	public String getRefId() {
	    return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
	
	public void setIconId(String iconId) {
		this.iconId = iconId;
	}

	public String getIconId() {
		return iconId;
	}
	
	public Map<String, String> getRequest() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("refId", refId);
		return map;
	}
	
	public Map<String, String> getIconIdRequest() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("iconId", iconId);
		return map;
	}

	public String getStyleClass() {
		if (styleClass == null) {
			return "";
		}
		else {
			return styleClass;
		}
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}