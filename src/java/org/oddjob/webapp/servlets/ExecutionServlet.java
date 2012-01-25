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
import org.oddjob.OddjobExecutors;
import org.oddjob.scheduling.DefaultExecutors;
import org.oddjob.webapp.WebappConstants;

/**
 * A servlet that provides {@link OddjobExecutors}.  This currenlty uses
 * the {@link DefaultExecutors} that start separate threads within a 
 * Servlet Container. This is slightly frowned upon in J2EE circles but 
 * many containers allow it.
 * 
 * @author Rob Gordon.
 */
public class ExecutionServlet extends HttpServlet {
	private static final long serialVersionUID = 20051103;
	private static final Logger logger = Logger.getLogger(ExecutionServlet.class);
	
	/** The Executors */
	private DefaultExecutors executors; 
	
	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		
		executors = new DefaultExecutors();
		
		ServletContext context = config.getServletContext();
		
		context.setAttribute(WebappConstants.EXECUTORS, 
				executors);
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
			
		res.setContentType("text/plain");	
		PrintWriter out = res.getWriter();
		
		out.println("Execution Servlet.");
		out.println("Executors: " + executors);
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.Servlet#destroy()
	 */
	public void destroy() {
		
		logger.debug("Stopping Executors.");
				
		executors.stop();
	}
}
