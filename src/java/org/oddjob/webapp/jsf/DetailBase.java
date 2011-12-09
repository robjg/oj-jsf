/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import java.util.Map;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.oddjob.webapp.WebappConstants;
import org.oddjob.webapp.model.JobInfoLookup;

/**
 * Shared functionality for all the 'detail' managed beands.
 * 
 * @author rob
 *
 */
public class DetailBase {

	private final Logger logger = Logger.getLogger(DetailBase.class);
	
	@ManagedProperty(value="#{detail}")
	private DetailBean detail;	
	
	public DetailBase() {
		logger.debug("Creating bean " + getClass());
	}
	
	/**
	 * Used by subclasses to get the {@link JobInfoLookup}.
	 * 
	 * @return
	 */
	protected JobInfoLookup getJobInfoLookup() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance(
				).getExternalContext().getApplicationMap();
		
		JobInfoLookup lookup = (JobInfoLookup) sessionMap.get(
				WebappConstants.DETAIL_LOOKUP);
		
		return lookup;
	}

	
	/**
	 * Access to detail. Not currently used, as only the currently selected
	 * node reference id is used from {@link #getRefId()}.
	 * 
	 * @return
	 */
	public DetailBean getDetail() {
		return detail;
	}

	/**
	 * Setter for the detail bean used by JSF.
	 * 	
	 * @param detail
	 */
	public void setDetail(DetailBean detail) {
		this.detail = detail;
	}
	
	/**
	 * Provide access to currently selected node reference id for sub classes.
	 * 
	 * @return
	 */
	protected String getRefId() {
		return detail.getRefId();
	}
}
