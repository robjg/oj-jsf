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

	/** The parameter that identifies the path to the
	 * configuration file. If this is not specified the 
	 * configuration file is assumed to be a resource.*/
	public final static String FILE_PATH_PARAM = "oddjob-config-path";
	
	/** The parameter that identifies the Oddjob 
	 * configuration file. */
	public final static String FILE_NAME_PARAM = "oddjob-config-file";
	
	/** Should Oddjob be run or loaded */
	public final static String RUN_OR_LOAD_PARAM = "oddjob-run-or-load";
	
	/** The parameter that identifies the Oddjob
	 * job name that the root Oddjob is given Defaults to Oddjob. */
	public final static String NAME_PARAM = "oddjob-name";
	
	/** The parameter that controls the minimum refresh allowed. */
	public final static String MINIMUM_REFRESH = "minimum_refresh";
	
	/** The id of the node to use as the root of the displayed
	 * tree. If empty then the Oddjob node itself is displayed.*/
	public final static String ROOT_PARAM = "oddjob-root-job-id";
	
	/** The log format for the log panel. */
	public final static String LOG_FORMAT_PARAM = "oddjob-log-format";
	
	/** The single instance of the a ThreadManager created
	 * by the Execution Servlet. */
	public final static String EXECUTORS = "oj_threadman";
	
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
