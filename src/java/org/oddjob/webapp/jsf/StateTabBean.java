/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.jsf;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;
import org.oddjob.state.StateEvent;
import org.oddjob.webapp.model.JobInfoLookup;

/**
 * The backing bean that handles the state tab.
 *  
 * @author Rob Gordon.
 */
@ManagedBean(name="state")
public class StateTabBean extends DetailBase {
	private static final Logger logger = Logger.getLogger(StateTabBean.class);
	
	/** The state. */
	private transient String jobState;
	
	/** The time. */
	private transient String time;
	
	/** The exception, if any. */
	private transient String Exception;
	
	/**
	 * Called when the bean is created which because this is a request scope
	 * bean is at the beginning of each request that this bean is used for.
	 */
	@PostConstruct
	public void init() {
		
		logger.debug("Retrieving state for [" + getRefId() + "]" );
		
		JobInfoLookup lookup = (JobInfoLookup) getJobInfoLookup();
		
		StateEvent jobStateEvent = lookup.stateFor(getRefId());
		
		String state;
		String time;
		Throwable t;
		
		if (jobStateEvent == null) {
			state = "Not Stateful";
			time = " - - - - -";
			t = null;
		}
		else {
			state = jobStateEvent.getState().toString();			
			time = jobStateEvent.getTime().toString();
			t = jobStateEvent.getException();
		}
		
		setJobState(state);
		setTime(time);
		
		
		if (t == null) {
			setException("");
		}
		else {
			// TODO: full stack trace.
			setException(t.getMessage());
		}
		
	}

	/**
	 * Getter for exception. 
	 * 
	 * @return The exception string.
	 */
	public String getException() {
		return Exception;
	}

	/**
	 * Setter for the exception.
	 * 
	 * @param exception The exception.
	 */
	public void setException(String exception) {
		Exception = exception;
	}

	/**
	 * Getter for the state time.
	 * 
	 * @return The state time.
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Setter for the state time.
	 * 
	 * @param time The state time.
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Getter for job state.
	 * 
	 * @return The job state.
	 */
	public String getState() {
		return jobState;
	}

	/**
	 * Setter for job state.
	 * 
	 * @param jobState The job state.
	 */
	public void setJobState(String jobState) {
		this.jobState = jobState;
	}

}