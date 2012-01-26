/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.oddjob.Oddjob;
import org.oddjob.OddjobExecutors;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.webapp.WebappConstants;

/**
 * This is the Oddjob Servlet. It relies on an OddjobExecutors being
 * available in the Servlet Context. {@link ExecutionServlet} provide
 * these.
 * 
 * @author Rob Gordon.
 */
public class OddjobServlet extends HttpServlet {
	private static final long serialVersionUID = 20051103;
	private static final Logger logger = Logger.getLogger(OddjobServlet.class);
	
	/** The oddjob instance */
	private Oddjob oddjob; 
	
	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		
		ServletContext context = config.getServletContext();
		
		OddjobExecutors executors = (OddjobExecutors) context.getAttribute(
				WebappConstants.EXECUTORS);
		if (executors == null) {
			throw new ServletException("No Executors - ensure Execution Servlet stats first!");
		}
		
		oddjob = new Oddjob();
		oddjob.setOddjobExecutors(executors);
		
		InitParam params = new InitParam(config);
		
		String oddjobPath = params.getInitParam(
				WebappConstants.FILE_PATH_PARAM);
		String oddjobFile = params.getRequiredInitParam(
				WebappConstants.FILE_NAME_PARAM);
		
		if (oddjobPath == null) {
			try {
				oddjob.setConfiguration(
						new XMLConfiguration( 
								context.getResource(oddjobFile)));
			} catch (MalformedURLException e) {
				throw new ServletException(e);
			}
		} else {
			oddjob.setFile(new File(oddjobPath, oddjobFile));
		}
		
		String name = params.getInitParam(
				WebappConstants.NAME_PARAM, "Oddjob");
		oddjob.setName(name);
		
		new OddjobRunOrLoad(oddjob).runOrLoad(
				params.getInitParam(WebappConstants.RUN_OR_LOAD_PARAM));

		context.setAttribute(WebappConstants.ODDJOB_INSTANCE, oddjob);		
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
			
		res.setContentType("text/plain");	
		PrintWriter out = res.getWriter();
		out.println("Oddjob Servlet"); 
		out.println("Root Oddjob is [" + oddjob.toString() + "]");
		out.println("State is [" + oddjob.lastStateEvent().getState() + "]");
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.Servlet#destroy()
	 */
	public void destroy() {
		
		logger.debug("Destroying Oddjob.");
				
		oddjob.destroy();
	}
}
