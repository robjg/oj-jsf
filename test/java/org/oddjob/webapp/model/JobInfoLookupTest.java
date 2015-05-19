/*
 * (c) Rob Gordon 2005
 */
package org.oddjob.webapp.model;

import java.util.List;

import javax.swing.ImageIcon;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.oddjob.Iconic;
import org.oddjob.Oddjob;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.images.IconEvent;
import org.oddjob.images.IconHelper;
import org.oddjob.images.IconListener;
import org.oddjob.jobs.structural.JobFolder;
import org.oddjob.logging.LogEnabled;
import org.oddjob.logging.LogEvent;
import org.oddjob.logging.LogHelper;
import org.oddjob.logging.log4j.Log4jArchiver;
import org.oddjob.monitor.context.ExplorerContext;
import org.oddjob.monitor.model.LogContextInialiser;
import org.oddjob.state.ParentState;
import org.oddjob.tools.OurDirs;
import org.oddjob.util.SimpleThreadManager;
import org.oddjob.util.ThreadManager;

public class JobInfoLookupTest extends TestCase {
	private static final Logger logger = Logger.getLogger(JobInfoLookupTest.class);
	
	IconRegistry ir = new IconRegistry();
	ArooaSession session = new StandardArooaSession();
	
	
	public void testGetRootRefId() {
		Object root = new Object();
		JobInfoLookup test = new JobInfoLookup(ir, session);
		test.setRoot(root, null);
		
		// note: we can't test for zero here because when running tests
		// in a shared jvm other test will have already incremented the
		// refid.
		assertNotNull(test.getRootRefId());
	}
	
	public void testIcon() {
		Iconic root = new Iconic() {
			public void addIconListener(IconListener listener) {
				IconEvent e = new IconEvent(this, "foo");
				listener.iconEvent(e);
			}
			public ImageIcon iconForId(String id) {
				return IconHelper.completeIcon;
			}
			public void removeIconListener(IconListener listener) {
			}
			
		};
		
		JobInfoLookup lookup = new JobInfoLookup(ir, session);
		lookup.setRoot(root, null);
		NodeInfo ni = lookup.nodeInfoFor(lookup.getRootRefId());
		assertEquals("foo", ni.getIconId());
		
		byte[] icon = ir.retrieve("foo");
		assertNotNull(icon);
	}

	public void testNoChildren() {
		String xml = "<oddjob/>";
		
		Oddjob oj = new Oddjob();
		oj.setConfiguration(new XMLConfiguration("XML", xml));
		oj.run();
		
		JobInfoLookup lookup = new JobInfoLookup(ir, session);
		lookup.setRoot(oj, null);
		NodeInfo ni = lookup.nodeInfoFor(lookup.getRootRefId());
		
		assertFalse(ni.getHasChildren());
		assertEquals(0, ni.getChildRefIds().length);
	}
	
	class OurExplorerContext extends MockExplorerContext {
		Object component;
		ThreadManager threadManager;
		
		@Override
		public ThreadManager getThreadManager() {
			return threadManager;
		}
		
		@Override
		public Object getThisComponent() {
			return component;
		}
		
		@Override
		public ExplorerContext addChild(Object child) {
			return this;
		}
	}
	

	/**
	 * Test that children are added and removed correctly in the
	 * lookup.
	 *
	 */
	public void testChildren() {
		String xml = 
			"<oddjob>" +
			" <job>" +
			"  <echo>Hello World</echo>" +
			" </job>" +
			"</oddjob>";
		
		Oddjob oj = new Oddjob();
		oj.setConfiguration(new XMLConfiguration("XML", xml));
		oj.run();
		
		JobInfoLookup lookup = new JobInfoLookup(ir, session);
		
		OurExplorerContext explorerContext = new OurExplorerContext();
		explorerContext.component = oj;
		
		lookup.setRoot(oj, explorerContext);
		
		NodeInfo ni = lookup.nodeInfoFor(lookup.getRootRefId());
		
		assertTrue(ni.getHasChildren());
		assertEquals(1, ni.getChildRefIds().length);

		String childRef = ni.getChildRefIds()[0];
		
		oj.hardReset();
		
		ni = lookup.nodeInfoFor(lookup.getRootRefId());
		assertEquals(0, ni.getChildRefIds().length);

		try {
			lookup.nodeInfoFor(childRef);
			fail("childRef should not exist.");
		} catch (IllegalStateException e) {
			// past
		}
	}
	
	/**
	 * Test several children - trying to track down a bug where
	 * the first child is repeated.
	 *
	 */
	public void testManyChildren() {
		JobFolder f1 = new JobFolder();
		f1.setName("F1");
		JobFolder f2 = new JobFolder();
		f2.setName("F2");
		JobFolder f3 = new JobFolder();
		f3.setName("F3");
		
		f1.setJobs(0, f2);
		f2.setJobs(0, f3);
		
		JobInfoLookup lookup = new JobInfoLookup(ir, session);
		
		OurExplorerContext explorerContext = new OurExplorerContext();
		explorerContext.component = f1;
		
		lookup.setRoot(f1, explorerContext);
		
		String r1 = lookup.getRootRefId();
		String r2 = lookup.nodeInfoFor(r1).getChildRefIds()[0];
		String r3 = lookup.nodeInfoFor(r2).getChildRefIds()[0];
		
		NodeInfo ni = lookup.nodeInfoFor(r3);
		assertEquals("F3", ni.getNodeName());
	}
	
	public static class Loggable implements LogEnabled {
		private Logger logger = Logger.getLogger("foo");
		
		public String loggerName() {
			return "foo";
		}
	
		void logSomething() {
			logger.setLevel(Level.DEBUG);
			logger.debug("Test");			
		}
	}
		
	public void testLogging() {
		Loggable l = new Loggable();
		assertEquals("foo", LogHelper.getLogger(l));
		
		JobInfoLookup lookup = new JobInfoLookup(ir, session);
//		lookup.setLogFormat("%m");
		
		final Log4jArchiver archiver = new Log4jArchiver(l,"%m");
		
		OurExplorerContext explorerContext = new OurExplorerContext() {
			@Override
			public Object getValue(String key) {
				assertEquals(LogContextInialiser.LOG_ARCHIVER, key);
				return archiver;
			};
		};
		explorerContext.component = l;

		lookup.setRoot(l, explorerContext);
		
		l.logSomething();
		
		List<LogEvent> events = 
			lookup.logEventsFor(lookup.getRootRefId());
		
		assertEquals(1, events.size());
		LogEvent event = events.get(0);
		assertEquals("Test", event.getMessage());
		
		archiver.onDestroy();
	}
	
	public void testCommands() throws Exception {
		SimpleThreadManager tm = new SimpleThreadManager();
		
		JobInfoLookup test = new JobInfoLookup(ir, session);
		
		Oddjob oj = new Oddjob();
		oj.setFile(new OurDirs().relative("test/config/oddjob-test.xml"));

		OurExplorerContext explorerContext = new OurExplorerContext();
		explorerContext.component = oj;
		explorerContext.threadManager = tm;
		
		test.setRoot(oj, explorerContext);
		WebJobActions actions = test.actionsFor(test.getRootRefId());

		assertTrue(actions.commands().contains("Start"));
		assertTrue(actions.isEnabled("Start"));
		
		actions.action("Start");
		while (tm.activeDescriptions().length > 0) {
			logger.debug("Waiting for ThreadManager.");
			Thread.sleep(500);
		}
		
		assertEquals(ParentState.COMPLETE, oj.lastStateEvent().getState());
		
		assertTrue(actions.commands().contains("Hard Reset"));
		assertTrue(actions.isEnabled("Hard Reset"));
		
		actions.action("Hard Reset");
		while (tm.activeDescriptions().length > 0) {
			logger.debug("Waiting for ThreadManager.");
			Thread.sleep(500);
		}
		
		assertEquals(ParentState.READY, oj.lastStateEvent().getState());

		actions.action("Start");
		while (tm.activeDescriptions().length > 0) {
			logger.debug("Waiting for ThreadManager.");
			Thread.sleep(500);
		}
		
		assertEquals(ParentState.COMPLETE, oj.lastStateEvent().getState());
	}
}
