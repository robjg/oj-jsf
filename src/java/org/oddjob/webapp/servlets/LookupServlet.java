/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.oddjob.Oddjob;
import org.oddjob.OddjobExecutors;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.monitor.context.ExplorerContext;
import org.oddjob.monitor.model.ExplorerContextImpl;
import org.oddjob.monitor.model.ExplorerModelImpl;
import org.oddjob.util.SimpleThreadManager;
import org.oddjob.util.ThreadManager;
import org.oddjob.webapp.WebappConstants;
import org.oddjob.webapp.model.IconRegistry;
import org.oddjob.webapp.model.JobInfoLookup;

/**
 * This servlet creates an {@link JobInfoLookup} on an Oddjob instance that 
 * tracks changes in the Oddjob instance so that the detail is available for 
 * presentation.
 *  
 * @author Rob Gordon.
 */
public class LookupServlet extends HttpServlet {
	private static final long serialVersionUID = 20051103;
	private static final Logger logger = Logger.getLogger(LookupServlet.class);
	
	/** The lookup this instance creates. */
	private JobInfoLookup lookup;
	
	private ThreadManager threadManager;
	
	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {		
		ServletContext context = config.getServletContext();
						
		Oddjob oddjob = (Oddjob) context.getAttribute(
				WebappConstants.ODDJOB_INSTANCE); 
		if (oddjob == null) {
			throw new ServletException("No Oddjob Instance - ensure Oddjob Servlet starts first!");
		}
		
		OddjobExecutors executors = (OddjobExecutors) context.getAttribute(
				WebappConstants.EXECUTORS);
		if (executors == null) {
			throw new ServletException("No Executors - ensure Execution Servlet starts first!");
		}
		
		threadManager = new SimpleThreadManager(executors.getPoolExecutor()); 
		
		InitParam params = new InitParam(config);
		
		String logFormat = params.getInitParam(
				WebappConstants.LOG_FORMAT_PARAM);
				
		String root = params.getInitParam(
				WebappConstants.ROOT_PARAM);
		
		ExplorerModelImpl explorerModel = new ExplorerModelImpl(
				new StandardArooaSession());
		explorerModel.setLogFormat(logFormat);
		explorerModel.setThreadManager(threadManager);
		explorerModel.setOddjob(oddjob);
		
		ExplorerContext eContext = new ExplorerContextImpl(explorerModel);
		
		new OddjobRunOrLoad(oddjob).runOrLoad(
				params.getInitParam(WebappConstants.RUN_OR_LOAD_PARAM));
		
		Object rootNode = oddjob;
		if (root != null) {
			OddjobLookup lookup = new OddjobLookup(oddjob);
			rootNode = lookup.lookup(root);

			if (rootNode == null) {
				throw new ServletException(
						"Can't find job for root node " + root);
			}
			
			eContext = eContext.addChild(rootNode);
		}
				
		lookup = new JobInfoLookup((IconRegistry)
				context.getAttribute(WebappConstants.ICON_REGISTRY));
		
		lookup.setRoot(rootNode, eContext);
		
		context.setAttribute(WebappConstants.DETAIL_LOOKUP, lookup);
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();		
		out.println("Lookup Servlet. " + lookup.getJobCount() + 
				" jobs cached.");
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.Servlet#destroy()
	 */
	public void destroy() {
		logger.debug("Destroying Oddjob Lookup.");
		lookup.destroy();
		threadManager.close();
	}
}
