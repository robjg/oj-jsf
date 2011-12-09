/*
 * (c) Rob Gordon 2004 - 2011.
 */
package org.oddjob.webapp;

/**
 * Constants used in the Webapp.
 *
 * @author Rob Gordon.
 */
public class WebappConstants {

	/** The parameter that identifies the Oddjob 
	 * configuration file. */
	public final static String FILE_PARAM = "file";
	
	/** The parameter that identifies the Oddjob
	 * job name. */
	public final static String NAME_PARAM = "name";
	
	/** The parameter that controls the minimum refresh allowed. */
	public final static String MINIMUM_REFRESH = "minimum_refresh";
	
	/** The id of the node to use as the root of the displayed
	 * tree. If empty then oddjob node itself is displayed.*/
	public final static String ROOT_PARAM = "root";
	
	/** The log format for the log panel. */
	public final static String LOG_FORMAT_PARAM = "logFormat";
	
	/** The single instance of Oddjob created by the Oddjob
	 * Servlet. */
	public final static String ODDJOB_INSTANCE = "oj_oddjob";
	
	/** The detail lookup which provides an instance of a lookup
	 * on the single Oddjob instance. */
	public final static String DETAIL_LOOKUP = "oj_detaillookup";
	
	/** An IconRegistry instance which will be created using the
	 * IconServlet to repond to icon image requests. */
	public final static String ICON_REGISTRY = "oj_iconregistry";
		
}
