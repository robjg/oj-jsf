package org.oddjob.webapp.servlets;

import java.util.concurrent.ExecutorService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.oddjob.Oddjob;
import org.oddjob.OddjobExecutors;
import org.oddjob.OurDirs;
import org.oddjob.state.ParentState;
import org.oddjob.webapp.WebappConstants;

public class OddjobServletTest extends TestCase {
	
	public void testInitDestroy() throws ServletException, InterruptedException {
		
		ExecutorService executorService = Mockito.mock(ExecutorService.class);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				((Runnable) invocation.getArguments()[0]).run();
				return null;
			}
		}).when(executorService).execute(Matchers.any(Runnable.class));
		
		OddjobExecutors executors = Mockito.mock(OddjobExecutors.class);
		Mockito.when(executors.getPoolExecutor()).thenReturn(executorService);
				
		ServletContext context = Mockito.mock(ServletContext.class);

		Mockito.when(context.getInitParameter(
				WebappConstants.FILE_NAME_PARAM)).thenReturn(
						"test/config/servlet-test.xml");

		Mockito.when(context.getInitParameter(
				WebappConstants.FILE_PATH_PARAM)).thenReturn(
						new OurDirs().base().getAbsolutePath());
		
		Mockito.when(context.getInitParameter(
				WebappConstants.NAME_PARAM)).thenReturn(
						"Servlet Test");
						
		Mockito.when(context.getInitParameter(
				WebappConstants.RUN_OR_LOAD_PARAM)).thenReturn(
						"run");
		
		Mockito.when(context.getAttribute(
				WebappConstants.EXECUTORS)).thenReturn(
						executors);
		
		CapturingMatcher<Oddjob> capturing = new CapturingMatcher<Oddjob>();
			
		ServletConfig config = Mockito.mock(ServletConfig.class);
		Mockito.when(config.getServletContext()).thenReturn(context);
		
		OddjobServlet test = new OddjobServlet();
				
		test.init(config);
				
		Mockito.verify(context).setAttribute(
				Mockito.eq(WebappConstants.ODDJOB_INSTANCE), 
				Mockito.argThat(capturing));
		
		Oddjob oddjob = capturing.getLastValue();

		assertNotNull(oddjob);

		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
				
		test.destroy();
		
		assertEquals(ParentState.DESTROYED, 
				oddjob.lastStateEvent().getState());
	}
}
