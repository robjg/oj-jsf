/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;
import org.oddjob.logging.LogEvent;
import org.oddjob.webapp.model.JobInfoLookup;

/**
 * The backing bean for the log tab.
 *  
 * @author Rob Gordon.
 */
@ManagedBean(name="log")
public class LogTabBean extends DetailBase {
	private static final Logger logger = Logger.getLogger(LogTabBean.class);
	
	/**
	 * Getter for log events.
	 * 
	 * @return A list of LogEvent objects.
	 */
	public Collection<LogEvent> getEvents() {

		logger.debug("Retrieving Log Events for [" + getRefId() + "]" );
		
		JobInfoLookup lookup = getJobInfoLookup();
		
		List<LogEvent> logEvents = lookup.logEventsFor(getRefId());
		
		return logEvents;
	}
}