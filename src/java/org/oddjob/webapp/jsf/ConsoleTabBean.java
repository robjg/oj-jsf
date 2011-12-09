/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;
import org.oddjob.logging.LogEvent;

/**
 * Managed Bean for console messages.
 *  
 * @author Rob Gordon.
 */
@ManagedBean(name="console")
public class ConsoleTabBean extends DetailBase {
	private static final Logger logger = Logger.getLogger(ConsoleTabBean.class);
	
	/**
	 * Getter for console events.
	 * 
	 * @return A list of LogEvent objects.
	 */
	public Collection<LogEvent> getEvents() {

		logger.debug("Retrieving console for [" + getRefId() + "]" );

		List<LogEvent> consoleEvents = 
				getJobInfoLookup().consoleEventsFor(getRefId());
		
		return consoleEvents;
	}
}