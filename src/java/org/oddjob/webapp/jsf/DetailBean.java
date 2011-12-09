/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

/**
 * The backing bean for the detail panel. This bean keeps track of 
 * the tab selected.
 * <p>
 * The bean is session scoped so that the tab can be remembered between
 * requests.
 * 
 * @author rob
 *
 */
@ManagedBean(name="detail")
@SessionScoped
public class DetailBean {

	private static final Logger logger = Logger.getLogger(DetailBean.class);
	
	/** Injected by JSF. */
	@ManagedProperty(value="#{tree}")
	private TreeBean tree;
	
	/** The currently selected tab. */
	private String tab = "state";
		
	/**
	 * Bean for Tab definitions.
	 * 
	 */
	public class Tab {
		
		private final String displayName;
		
		private final String page;
		
		private Tab(String displayName, String page) {
			this.displayName = displayName;
			this.page = page;
		}
		
		public String getDisplayName() {
			return displayName;
		}

		public String getPage() {
			return page;
		}

		public String getStyleClass() {
			if (page.equals(tab)) {
				return "selected";
			}
			else {
				return "notSelected";
			}
		}
		
	}

	/** List of availble tabs. */
	private final List<Tab> tabs = new ArrayList<Tab>();
	
	/**
	 * Constructor.
	 */
	public DetailBean() {
		logger.debug("Creating " + getClass());
		
		tabs.add(new Tab("State", "state"));
		tabs.add(new Tab("Console", "console"));
		tabs.add(new Tab("Log", "log"));
		tabs.add(new Tab("Properties", "properties"));
	}
	
	/**
	 * Setter for tree. This is automatically injected.
	 *  
	 * @param tree
	 */
	public void setTree(TreeBean tree) {
		logger.debug("Setting tree.");
		this.tree = tree;
	}
	
	/**
	 * Getter for tree. Not sure this is currently used anywhere.
	 * 
	 * @return
	 */
	public TreeBean getTree() {
		return tree;
	}
	
	/**
	 * Getter for refId.
	 * 
	 * @return The refId.
	 */
	public String getRefId() {
		return tree.getSelectedRefId();
	}
	
	/**
	 * Get currently selected tab.
	 * 
	 * @return
	 */
	public String getTab() {
		return tab;
	}

	/**
	 * Set currently selected tab. Set via the f:param tag in tabs.xhtml.
	 * 
	 * @param selectedTab
	 */
	public void setTab(String selectedTab) {
		this.tab = selectedTab;
	}
	
	/**
	 * Getter for tab defintions. Used by tabs.xhtml.
	 * @return
	 */
	public List<Tab> getTabs() {
		return tabs;
	}
}
