package org.oddjob.webapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturingMatcher;
import org.oddjob.FailedToStopException;
import org.oddjob.OddjobExecutors;
import org.oddjob.io.BufferType;
import org.oddjob.jobs.WaitJob;
import org.oddjob.webapp.WebappConstants;

public class ExecutionServletTest extends TestCase {
	
	private static final Logger logger = 
			Logger.getLogger(ExecutionServletTest.class);
	
	public void testInitDestroy() throws ServletException, InterruptedException, 
			IOException, FailedToStopException {
				
		ServletContext context = Mockito.mock(ServletContext.class);

		CapturingMatcher<OddjobExecutors> capturing = 
				new CapturingMatcher<OddjobExecutors>();
			
		ServletConfig config = Mockito.mock(ServletConfig.class);
		Mockito.when(config.getServletContext()).thenReturn(context);
		
		ExecutionServlet test = new ExecutionServlet();
				
		test.init(config);
				
		Mockito.verify(context).setAttribute(
				Mockito.eq(WebappConstants.EXECUTORS), 
				Mockito.argThat(capturing));
		
		OddjobExecutors executors = capturing.getLastValue();

		assertNotNull(executors);
		
		WaitJob wait = new WaitJob();
		executors.getPoolExecutor().execute(wait);
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response= Mockito.mock(HttpServletResponse.class);

		BufferType buffer = new BufferType();
		buffer.configured();

		PrintWriter out = new PrintWriter(buffer.toOutputStream()); 
		Mockito.when(response.getWriter()).thenReturn(out);
		
		test.doGet(request, response);

		out.close();
		
		logger.info(buffer.getText());
		
		String[] lines = buffer.getLines();
		assertEquals(2, lines.length);
		
		wait.stop();

		test.destroy();
	}
}
