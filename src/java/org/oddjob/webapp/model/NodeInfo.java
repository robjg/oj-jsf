/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.model;

/**
 * Collect together job node information for use
 * in the tree view.
 * 
 * @author Rob Gordon.
 *
 */
public class NodeInfo {

	/** The node name. */
	private final String nodeName;
	
	/** A list of the refIds of any children. */
	private final String[] childRefIds;
	
	/** The iconId of the node */
	private final String iconId;
	
	/**
	 * Constructor.
	 * 
	 * @param nodeName The node name.
	 * @param childRefIds A list of the refIds of any children.
	 * @param iconId The icon id.
	 */
	public NodeInfo (String nodeName, 
			String[] childRefIds, 
			String iconId) {
		if (childRefIds == null) {
			throw new NullPointerException("childRefId[] should be an empty array, not null.");
		}
		this.nodeName = nodeName;
		this.childRefIds = childRefIds;
		this.iconId = iconId;
	}

	/**
	 * Get child refIds.
	 * 
	 * @return An array of child refIds, never Null.
	 */
	public String[] getChildRefIds() {
		return childRefIds;
	}

	/**
	 * Get the Icon id.
	 * 
	 * @return The icon id.
	 */
	public String getIconId() {
		return iconId;
	}

	/**
	 * Get the node name.
	 * 
	 * @return The node name.
	 */
	public String getNodeName() {
		return nodeName;
	}
	
	/**
	 * Has this node got children.
	 * 
	 * @return True if this node has children, false if it doesn't.
	 */
	public boolean getHasChildren() {
		return !(childRefIds.length == 0);  
	}
}
