package org.oddjob.webapp.servlets;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.oddjob.Oddjob;
import org.oddjob.OddjobExecutors;
import org.oddjob.OddjobLookup;
import org.oddjob.Stateful;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.state.JobState;
import org.oddjob.state.ParentState;
import org.oddjob.webapp.WebappConstants;
import org.oddjob.webapp.model.IconRegistry;
import org.oddjob.webapp.model.JobInfoLookup;
import org.oddjob.webapp.model.NodeInfo;
import org.oddjob.webapp.model.WebJobActions;

public class LookupServletTest extends TestCase {
	
	public void testInitDestroy() throws Exception {

		String xml = 
				"<oddjob>" +
				"<job>" +
				"<sequence from='1' id='sequence'/>" +
				"</job>" +
				"</oddjob>";
			
		Oddjob oddjob = new Oddjob();
		oddjob.setConfiguration(new XMLConfiguration("XML", xml));

		oddjob.run();

		assertEquals(ParentState.COMPLETE, oddjob.lastStateEvent().getState());

		OddjobLookup lookup = new OddjobLookup(oddjob);

		Integer current = lookup.lookup("sequence.current", Integer.class);
		assertEquals(new Integer(1), current);

		Stateful sequence = lookup.lookup("sequence", Stateful.class);
		
		// Mocks 
				
		ExecutorService executorService = Mockito.mock(ExecutorService.class);
		Mockito.doAnswer(new Answer<Future<?>>() {
			@Override
			public Future<?> answer(InvocationOnMock invocation) throws Throwable {
				((Runnable) invocation.getArguments()[0]).run();
				return Mockito.mock(Future.class);
			}
		}).when(executorService).submit(Matchers.any(Runnable.class));
		
		OddjobExecutors executors = Mockito.mock(OddjobExecutors.class);
		Mockito.when(executors.getPoolExecutor()).thenReturn(executorService);
		
		ServletContext context = Mockito.mock(ServletContext.class);

		Mockito.when(context.getInitParameter(
				WebappConstants.LOG_FORMAT_PARAM)).thenReturn(
						null);

		Mockito.when(context.getInitParameter(
				WebappConstants.ROOT_PARAM)).thenReturn(
						"sequence");
						
		Mockito.when(context.getAttribute(
				WebappConstants.EXECUTORS)).thenReturn(
						executors);

		Mockito.when(context.getAttribute(
				WebappConstants.ODDJOB_INSTANCE)).thenReturn(
						oddjob);
		
		Mockito.when(context.getAttribute(
				WebappConstants.ICON_REGISTRY)).thenReturn(
						new IconRegistry());
		
		CapturingMatcher<JobInfoLookup> capturing = 
				new CapturingMatcher<JobInfoLookup>();
			
		ServletConfig config = Mockito.mock(ServletConfig.class);
		Mockito.when(config.getServletContext()).thenReturn(context);
		
		LookupServlet test = new LookupServlet();
				
		test.init(config);
				
		Mockito.verify(context).setAttribute(
				Mockito.eq(WebappConstants.DETAIL_LOOKUP), 
				Mockito.argThat(capturing));
		
		JobInfoLookup jobInfoLookup = capturing.getLastValue();

		assertNotNull(jobInfoLookup);

		assertEquals(1, jobInfoLookup.getJobCount());
				
		String refId = jobInfoLookup.getRootRefId();
		
		NodeInfo nodeInfo = jobInfoLookup.nodeInfoFor(refId);
		assertEquals("A Sequence Number", nodeInfo.getNodeName());
		
		WebJobActions actions = jobInfoLookup.actionsFor(refId);
		actions.action("Hard Reset");
		
		assertEquals(JobState.READY, sequence.lastStateEvent().getState());
		
		actions.action("Start");
		
		assertEquals(JobState.COMPLETE, sequence.lastStateEvent().getState());
		
		current = lookup.lookup("sequence.current", Integer.class);
		assertEquals(new Integer(2), current);
		
		test.destroy();
		
	}
}
