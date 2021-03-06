/* The MIT License
 * 
 * Copyright (c) 2005 David Rice, Trevor Croft
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation files 
 * (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, 
 * publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package net.rptools.common.util;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 
 * @author trevor
 */
public class ImageUtil {
	
	private static GraphicsConfiguration graphicsConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

	public static final FilenameFilter SUPPORTED_IMAGE_FILE_FILTER = new FilenameFilter () {
		
		public boolean accept(File dir, String name) {

			name = name.toLowerCase();
			
			return name.endsWith("png") ||
					name.endsWith("gif") ||
					name.endsWith("jpg") ||
					name.endsWith("jpeg") ||
					name.endsWith("bmp");
		}
	};
	
	public static void setGraphicsConfiguration(GraphicsConfiguration config) {
		graphicsConfig = config;
	}
	
    public static BufferedImage getImage (File file) throws IOException {
        return createCompatibleImage(bytesToImage(FileUtil.getBytes(file.toURL())));
    }
    
	public static BufferedImage getImage(String image) throws IOException {
		
		ByteArrayOutputStream dataStream = new ByteArrayOutputStream(8192);
		
		int bite;
		InputStream inStream = ImageUtil.class.getClassLoader().getResourceAsStream(image);
        if (inStream == null) {
            throw new IOException("Image not found: " + image);
        }
        
		while ((bite = inStream.read()) >= 0) {
			dataStream.write(bite);
		}
		
		return createCompatibleImage(bytesToImage(dataStream.toByteArray()));
	}

	public static BufferedImage getCompatibleImage(String image) throws IOException {
		return createCompatibleImage(getImage(image));
	}

    /**
     * Create a copy of the image that is compatible with the current graphics context
     * @param img
     * @return
     */
    public static BufferedImage createCompatibleImage(Image img) {
        if (img == null) {
            return null;
        }
        
        return createCompatibleImage(img, img.getWidth(null), img.getHeight(null));
    }

    /**
     * Create a copy of the image that is compatible with the current graphics context
     * and scaled to the supplied size
     * @param img
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createCompatibleImage(Image img, int width, int height) {
        
        BufferedImage compImg = graphicsConfig.createCompatibleImage(width, height, pickBestTransparency(img));
        
        Graphics g = null;
        try {
            g = compImg.getGraphics();
            
            g.drawImage(img, 0, 0, width, height, null);
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
        
        return compImg;
    }
    
    /**
     * Look at the image and determine which Transparency is most appropriate.
     * If it finds any translucent pixels it returns Transparency.TRANSLUCENT, if 
     * it finds at least one purely transparent pixel and no translucent pixels
     * it will return Transparency.BITMASK, in all other cases it returns 
     * Transparency.OPAQUE, including errors
     * 
     * @param image
     * @return one of Transparency constants
     */
    public static int pickBestTransparency ( Image image )
    {
        // Get the pixels
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        int [] pixelArray = new int [ width * height ];
        PixelGrabber pg = new PixelGrabber( image, 0, 0, width, height, pixelArray, 0, width );
        try 
        {
            pg.grabPixels();
        } 
        catch (InterruptedException e) 
        {
            System.err.println("interrupted waiting for pixels!");
            return Transparency.OPAQUE;
        }

        if ((pg.getStatus() & ImageObserver.ABORT) != 0) 
        {
            System.err.println("image fetch aborted or errored");
            return Transparency.OPAQUE;
        }

        // Look for specific pixels
        boolean foundTransparent = false;
        for (int y = 0; y < height; y++) 
        {
            for (int x = 0; x < width; x++) 
            {
                // Get the next pixel
                int pixel = pixelArray [ y*width + x ];
                int alpha = (pixel >> 24) & 0xff;
                
                // Is there translucency or just pure transparency ?
                if ( alpha > 0 && alpha < 255 )
                {
                    return Transparency.TRANSLUCENT;
                }
                
                if ( alpha == 0 && !foundTransparent )
                {
                    foundTransparent = true;
                }
            }
        }
            
        return foundTransparent ? Transparency.BITMASK : Transparency.OPAQUE; 
    }
    
    public static byte[] imageToBytes(BufferedImage image) throws IOException {
        return imageToBytes(image, "jpg");
    }
	
	public static byte[] imageToBytes(BufferedImage image, String format) throws IOException {
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(10000);

		ImageIO.write(image, format, outStream);
		
		return outStream.toByteArray();
	}
	
	public static Image bytesToImage(byte[] imageBytes) throws IOException {
		
		ByteArrayInputStream inStream = new ByteArrayInputStream(imageBytes);
		
		Image image = null;
		try {
			image = ImageIO.read(inStream);
		} catch (Exception e) {
			
			// Try the old fashioned way
			image = Toolkit.getDefaultToolkit().createImage(imageBytes);
			MediaTracker tracker = new MediaTracker(new JPanel());
			tracker.addImage(image, 0);
			try {
				tracker.waitForID(0);
			} catch (Exception e2) {
				// nothing to do
			}
		}
		
		return image;
	}
        
	public static void clearImage(BufferedImage image) {
		
		if (image == null) {return;}
		
		Graphics2D g = null;
		try {

			g = (Graphics2D)image.getGraphics();
			Composite oldComposite = g.getComposite();

        	g.setComposite(AlphaComposite.Clear);

        	g.fillRect(0, 0, image.getWidth(), image.getHeight());
        	
        	g.setComposite(oldComposite);
		} finally {
			if (g != null) {
				g.dispose();
			}
		}
	}
	
	public static BufferedImage rgbToGrayscale(BufferedImage image) {

		if (image == null) {
			return null;
		}
		
		BufferedImage returnImage = new BufferedImage (image.getWidth(), image.getHeight(), pickBestTransparency(image));
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {

				int encodedPixel = image.getRGB(x, y);

				int alpha = (encodedPixel >> 24) & 0xff;
				int red   = (encodedPixel >> 16) & 0xff;
				int green = (encodedPixel >>  8) & 0xff;
				int blue  = (encodedPixel      ) & 0xff;
	       
				int average = (int)((red + blue + green) / 3.0);
				
				// y = 0.3R + 0.59G + 0.11B luminance formula
				int value = (alpha << 24) + (average << 16) + (average << 8) + average;
				returnImage.setRGB(x, y, value);
			}
		}

		return returnImage;
	}	
}
