package org.oddjob.webapp.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * Utility class that allows initialisation parameters to be specified either
 * in the Servlet context (the web.xml) or in the container configuration.
 * <p>
 * The advantage of specifying parameters in the Servlet context is they
 * can be specified on a machine by machine bases and be independent of the
 * deployed web application archive.
 * <p>
 * This utility will first look for a container parameter before looking
 * for a web.xml version.
 * 
 * @author rob
 *
 */
public class InitParam {

	private final ServletConfig config;
	
	/**
	 * Constructor.
	 * 
	 * @param config The Servlet container configuration
	 */
	public InitParam(ServletConfig config) {
		this.config = config;
	}

	/**
	 * Get the initialisation parameter, or null if it's not specified.
	 * 
	 * @param parameterName The parameter.
	 * @return The value or null if none is found.
	 */
	public String getInitParam(String parameterName) {
		
		return getInitParam(parameterName, null);
	}
	
	/**
	 * Get the initialisation parameter with a default if it's not specified.
	 * 
	 * @param parameterName The parameter.
	 * @param theDefault The default value. May be null.
	 * @return The value, or the default value if no value is found.
	 */
	public String getInitParam(String parameterName, String theDefault) {
		
		String value = config.getInitParameter(
				parameterName);
		if (value == null) {
			value = config.getServletContext().getInitParameter(
					parameterName);
		}
		if (value == null) {
			return theDefault;
		}
		else {
			return value;
		}
	}
	
	/**
	 * Get a parameter that must be specified.
	 * 
	 * @param parameterName The parameter.
	 * @return A value. Never null.
	 * 
	 * @throws ServletException If the parameter doesn't exist.
	 */
	public String getRequiredInitParam(String parameterName) 
	throws ServletException {
		
		String value = getInitParam(parameterName, null);
		
		if (value == null) {
			throw new ServletException("Initialisation parameter " + 
				parameterName + " must be provided.");
		}
		
		return value;
	}
}
