/*
 * (c) Rob Gordon 2005
 */
package org.oddjob.webapp.model;

import javax.swing.ImageIcon;

import junit.framework.TestCase;

import org.oddjob.Iconic;
import org.oddjob.Oddjob;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.images.IconHelper;
import org.oddjob.images.IconListener;
import org.oddjob.monitor.context.ExplorerContext;
import org.oddjob.monitor.model.MockExplorerContext;
import org.oddjob.util.ThreadManager;
import org.oddjob.webapp.jsf.TreeNodeBean;

public class TreeNodeBeanBuilderTest extends TestCase {

	class OurExplorerContext extends MockExplorerContext {
		
		@Override
		public ThreadManager getThreadManager() {
			return null;
		}
		
		@Override
		public Object getThisComponent() {
			return null;
		}
		
		@Override
		public ExplorerContext addChild(Object child) {
			return this;
		}
	}
	
	/** Test simply building a bean */
	public void testBuildBean() {
		String xml = 
			"<oddjob>" +
			" <job>" +
			"  <echo>Hello World</echo>" +
			" </job>" +
			"</oddjob>";
		
		Oddjob oj = new Oddjob();
		oj.setConfiguration(new XMLConfiguration("XML", xml));
		oj.setName("Test");
		
		JobInfoLookup lookup = new JobInfoLookup(new IconRegistry(),
				new StandardArooaSession());
		lookup.setRoot(oj, new OurExplorerContext());
		TreeNodeBeanBuilder builder = new TreeNodeBeanBuilder(lookup, "1");
		TreeNodeBean bean = builder.buildRoot();
		
		assertNotNull(bean);
		
		assertFalse(bean.isHasChildren());
		assertFalse(bean.isShowChildren());
		assertEquals("Test", bean.getNodeName());
		
		oj.run();
		
		builder.refresh(bean);
		
		assertTrue(bean.isHasChildren());
		assertFalse(bean.isShowChildren());
		
		bean.expand();
		
		assertTrue(bean.isHasChildren());
		assertTrue(bean.isShowChildren());
		assertEquals(1, bean.getChildren().size());		
		
		// check child.
		TreeNodeBean[] children = (TreeNodeBean[]) bean.getChildren().toArray(new TreeNodeBean[0]);
		TreeNodeBean child = children[0];
		assertEquals(1, children.length);		
		assertFalse(child.isShowChildren());		
		assertFalse(child.isHasChildren());		
		assertEquals(null, child.getChildren());
		
		bean.collapse();
		assertFalse(bean.isShowChildren());
		
		oj.hardReset();
		
		builder.refresh(bean);
		
		assertFalse(bean.isHasChildren());
		assertFalse(bean.isShowChildren());
	}

	/** More complicated bean - tracking down a bug where child displayed twice. */
	public void testBuildBean2() {
		String xml = 
			"<oddjob>" +
			" <job>" +
			"  <sequential name='Child 1'>" +
			"   <jobs>" +
			"    <sequential name='Child 2'>" +
			"     <jobs>" +
			"      <echo name='Child 3'>Hello World</echo>" +
			"     </jobs>" +
			"    </sequential>" +
			"   </jobs>" +
			"  </sequential>" +
			" </job>" +
			"</oddjob>";
		
		Oddjob oj = new Oddjob();
		oj.setConfiguration(new XMLConfiguration("XML", xml));
		oj.setName("Test");
		oj.run();
		
		JobInfoLookup lookup = new JobInfoLookup(new IconRegistry(),
				new StandardArooaSession());
		lookup.setRoot(oj, new OurExplorerContext());
		TreeNodeBeanBuilder builder = new TreeNodeBeanBuilder(lookup, "1");
		TreeNodeBean bean = builder.buildRoot();
		bean.expand();
		
		TreeNodeBean[] children;
		TreeNodeBean child;
		
		children = (TreeNodeBean[]) bean.getChildren().toArray(new TreeNodeBean[0]);
		child = children[0];
		assertEquals("Child 1", child.getNodeName());
		
		child.expand();
		children = (TreeNodeBean[]) child.getChildren().toArray(new TreeNodeBean[0]);
		child = children[0];
		assertEquals("Child 2", child.getNodeName());
		
		child.expand();
		children = (TreeNodeBean[]) child.getChildren().toArray(new TreeNodeBean[0]);
		child = children[0];
		assertEquals("Child 3", child.getNodeName());
	}
	
	public void testIcon() {
		class I implements Iconic {
			IconHelper ih = new IconHelper(this);
			boolean toggle;
			public void addIconListener(IconListener listener) {
				if (toggle) {
					ih.changeIcon(IconHelper.COMPLETE);
				} else {
					ih.changeIcon(IconHelper.EXCEPTION);
				}
				toggle = !toggle;
				ih.addIconListener(listener);
			}
			public ImageIcon iconForId(String id) {
				return ih.iconForId(id);
			}
			public void removeIconListener(IconListener listener) {
				ih.removeIconListener(listener);
			}
		}
		I i = new I();
		
		JobInfoLookup lookup = new JobInfoLookup(new IconRegistry(),
				new StandardArooaSession());
		lookup.setRoot(i, null);
		TreeNodeBeanBuilder builder = new TreeNodeBeanBuilder(lookup, "1");
		TreeNodeBean bean = builder.buildRoot();

		String iconId; 
		iconId = bean.getIconId();
		assertEquals(IconHelper.EXCEPTION, iconId);
		
		bean = builder.buildRoot();
		
		iconId = bean.getIconId();
		assertEquals(IconHelper.COMPLETE, iconId);
	}

}
