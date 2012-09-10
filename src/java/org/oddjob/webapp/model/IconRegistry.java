/*
 * (c) Rob Gordon 2005 - 2011.
 */
package org.oddjob.webapp.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.oddjob.Iconic;

/**
 * A registry of icons so they can be served up to 
 * the view. This registry works on the principle that
 * an icon id identifies the same icon for all jobs. 
 * 
 * @author Rob Gordon.
 */
public class IconRegistry {

	/** The icons. */
	final private Map<String, byte[]> icons = new HashMap<String, byte[]>();

	/**
	 * Register an iconId. If the icon id isn't
	 * registered already, the icon is looked up.
	 * 
	 * @param iconId The iconId.
	 * @param iconic The Iconic that can provide 
	 * the lookup.
	 */
	public void register(String iconId, Iconic iconic) {
		synchronized (icons) {
			if (!icons.containsKey(iconId)) {
				ImageIcon icon = iconic.iconForId(iconId);
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				writeIcon(icon, out);
				
				icons.put(iconId, out.toByteArray());
			}
		}
	}

	/**
	 * Write the icon to an output stream.
	 * 
	 * @param icon The icon.
	 * @param out The output stream. This method will close the output
	 * stream.
	 */
	protected void writeIcon(ImageIcon icon, OutputStream out) {
		
		Image image = icon.getImage();
		RenderedImage rendered;
		if (image instanceof RenderedImage) {
			rendered = (RenderedImage) image;
		}
		else {
			BufferedImage buffered = new BufferedImage(
					icon.getIconWidth(),
					icon.getIconHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = buffered.createGraphics();
			g.drawImage(image, 0, 0, Color.WHITE, null);
			g.dispose();
			rendered = buffered;
		}
		try {
			ImageIO.write((BufferedImage) rendered, "jpg", out);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Retrieve an IconTip for a given icon id.
	 * 
	 * @param iconId The iconId.
	 * @return The IconTip, null if none exists if
	 * nothing is registered for that id.
	 */
	public byte[] retrieve(String iconId) {
		synchronized (icons) {
			return icons.get(iconId);
		}
	}
}
