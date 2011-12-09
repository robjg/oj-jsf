/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.oddjob.webapp.WebappConstants;

/**
 * Backing bean that handles the refresh field.
 * <p>
 * Session scoped because the refresh has to go back to the top frame so
 * it must be remembered between requests.
 * 
 * @author Rob Gordon.
 */
@ManagedBean(name="refresh")
@SessionScoped
public class RefreshBean {
	
	private static final Logger logger = Logger.getLogger(RefreshBean.class);
	
	/** Refresh in seconds. */
	private Long refresh;

	/**
	 * The JSF form action.
	 * 
	 * @return
	 */
	public String submit() {

		logger.debug("Setting Refresh [" + refresh + "]" );
		
		if (refresh != null) {
			String minRefreshParam = (String)
					FacesContext.getCurrentInstance().getExternalContext(
							).getApplicationMap().get(
									WebappConstants.MINIMUM_REFRESH);
			long minRefresh = 1;
			
			if (minRefreshParam != null) {
				minRefresh = Long.parseLong(minRefreshParam);
			}
			
			if (refresh.longValue() < minRefresh) {
				refresh = null;
			}
		}
		
		return "index.xhtml?faces-redirect=true";
	}

	/**
	 * Capture the preRenderView event to set the refresh in the HTTP header.
	 * 
	 * @return Always null.
	 */
	public void setRefreshInHeader(ComponentSystemEvent event) {
		
		if (refresh != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) 
					context.getExternalContext().getResponse();
			response.setHeader("Refresh", refresh.toString());
		}
	}

	/**
	 * Setter for the form property.
	 * 
	 * @return
	 */
	public Long getRefresh() {
		return refresh;
	}

	/**
	 * Getter for the form property.
	 * 
	 * @param refresh
	 */
	public void setRefresh(Long refresh) {
		this.refresh = refresh;
	}
}