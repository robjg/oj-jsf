package org.oddjob.webapp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;

import junit.framework.TestCase;

import org.oddjob.OurDirs;
import org.oddjob.images.IconHelper;

public class IconRegistryTest extends TestCase {

	File workDir = new OurDirs().relative("work");
	
	
	public void testWriteIcon() throws FileNotFoundException {
		
		ImageIcon icon = IconHelper.notCompleteIcon;
		
		File file = new File(workDir, "incomplete.jpg");
		
		FileOutputStream out = new FileOutputStream(file);
		
		new IconRegistry().writeIcon(icon, out);
		
		assertTrue(file.exists());
	}
	
}
