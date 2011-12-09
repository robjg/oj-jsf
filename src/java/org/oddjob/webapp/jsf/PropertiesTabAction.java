/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;
import org.oddjob.webapp.model.JobInfoLookup;

/**
 * The backing bean that handles the property tab.
 *  
 * @author Rob Gordon.
 */
@ManagedBean(name="properties")
public class PropertiesTabAction extends DetailBase {
	private static final Logger logger = Logger.getLogger(PropertiesTabAction.class);
	
	/** JSF gets the properties several times in each request. No idea why,
	 * so we cache the properties so they are not got many times, using 
	 * {@link #init()}.
	 */
	private ArrayList<Map.Entry<String, String>> entrySet;
	
	/**
	 * Called when the bean is created which because this is a request scope
	 * bean is at the beginning of each request that this bean is used for.
	 */
	@PostConstruct
	public void init() {
		
		logger.debug("Retrieving properties for [" + getRefId() + "]" );

		JobInfoLookup lookup = getJobInfoLookup();
		
		Map<String, String> properties= lookup.propertiesFor(getRefId());

		if (properties != null) {
			entrySet = new ArrayList<Map.Entry<String, String>>(properties.entrySet());
		}
	}
	
	/**
	 * Get the property entry set.
	 * 
	 * @return
	 */
	public Collection<Map.Entry<String, String>> getEntries() {

		return entrySet;
	}
}
