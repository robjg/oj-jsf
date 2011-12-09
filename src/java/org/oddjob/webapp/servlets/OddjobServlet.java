/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.servlets;

import java.io.File;
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
import org.oddjob.webapp.WebappConstants;


/**
 * This is the Oddjob servlet that starts Oddjob running on
 * a separate thread within a Servlet Container. This is 
 * slightly frowned upon in J2EE circles but most containers
 * allow it.
 * 
 * @author Rob Gordon.
 */
public class OddjobServlet extends HttpServlet {
	private static final long serialVersionUID = 20051103;
	private static final Logger logger = Logger.getLogger(OddjobServlet.class);
	
	/** The oddjob instance */
	private Oddjob oddjob; 
	
	/** The thread that is running it. */
	private Thread thread;

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		oddjob = new Oddjob();
		
		ServletContext context = config.getServletContext();
								
		String servletContextPath = context.getRealPath("/");		
		String oddjobFile = config.getServletContext().getInitParameter(
				WebappConstants.FILE_PARAM);
		if (oddjobFile == null) {
			oddjobFile = config.getInitParameter(
					WebappConstants.FILE_PARAM);
		}
		if (oddjobFile == null) {
			oddjobFile = servletContextPath + "/WEB-INF/" + "oddjob.xml";
		} else {
			oddjobFile = servletContextPath + "/" + oddjobFile;
		}
		oddjob.setFile(new File(oddjobFile));
		
		String name = context.getInitParameter(
				WebappConstants.NAME_PARAM);
		if (name == null) {
			name = config.getInitParameter(
					WebappConstants.NAME_PARAM);
		}
		if (name == null) {
			name = "Oddjob";
		}
		oddjob.setName(name);

		context.setAttribute(WebappConstants.ODDJOB_INSTANCE, oddjob);
		
		thread = new Thread(oddjob);		
		thread.start();
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
			
		res.setContentType("text/plain");	
		PrintWriter out = res.getWriter();
		out.println("Oddjob");
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
