package org.oddjob.webapp.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.OurDirs;
import org.oddjob.jobs.WaitJob;
import org.oddjob.state.ParentState;
import org.oddjob.state.StateConditions;
import org.oddjob.webapp.WebappConstants;

public class OddjobServletTest extends TestCase {

	class OurServletContext extends MockServletContext {
		
		Oddjob oddjob;
		
		@Override
		public String getInitParameter(String name) {
			if (WebappConstants.FILE_PARAM.equals(name)) {
				return "test/config/servlet-test.xml";
			}
			if (WebappConstants.NAME_PARAM.equals(name)) {
				return "Servlet Test";
			}
			
			throw new RuntimeException("Unexpected: " + name);
		}
		
		@Override
		public String getRealPath(String arg0) {
			return new OurDirs().base().toString();
		}
		
		@Override
		public void setAttribute(String name, Object value) {
			assertEquals(WebappConstants.ODDJOB_INSTANCE, name);
			oddjob = (Oddjob) value;
		}
	}
	
	class OurServletConfig extends MockServletConfig {
		
		OurServletContext context = new OurServletContext();
		
		@Override
		public ServletContext getServletContext() {
			return context;
		}
		
	}
	
	public void testInitDestroy() throws ServletException, InterruptedException {
		
		OddjobServlet test = new OddjobServlet();
		
		OurServletConfig config = new OurServletConfig(); 
		
		test.init(config);
		
		Oddjob oddjob = config.context.oddjob;

		WaitJob wait = null;
		while (true) {
			wait = (WaitJob) new OddjobLookup(oddjob).lookup("wait");
			if (wait == null) {
				synchronized (this) {
					wait(1000);
				}
			}
			else {
				break;
			}
		}
		
		WaitJob waitForWait = new WaitJob();
		waitForWait.setFor(wait);
		waitForWait.setState(StateConditions.EXECUTING);
		
		waitForWait.run();
		
		test.destroy();
		
		assertEquals(ParentState.DESTROYED, 
				oddjob.lastStateEvent().getState());
	}
}
