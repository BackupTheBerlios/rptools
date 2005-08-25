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
package net.rptools.common.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.rptools.common.util.ImageUtil;

/**
 */
public class SwingUtil {
    
    private static Cursor emptyCursor;
    
    static {
        try {
            emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor( ImageUtil.getImage("net/rptools/common/swing/image/empty.png"), new Point (0,0), "");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	public static void centerOnScreen(Window window) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = window.getSize();
		
		int x = (screenSize.width - windowSize.width) / 2;
		int y = (screenSize.height - windowSize.height) / 2;
		
		window.setLocation(x, y);
	}
    

    public static boolean isControlDown(InputEvent e) {
        return (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) > 0;
    }
    
    public static boolean isShiftDown(InputEvent e) {
        return (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) > 0;
    }
    
    public static void centerOver(Window innerWindow, Window outterWindow) {
    	
    	Dimension innerSize = innerWindow.getSize();
    	Dimension outterSize = outterWindow.getSize();
    	
    	int x = outterWindow.getLocation().x + (outterSize.width - innerSize.width) / 2;
    	int y = outterWindow.getLocation().y + (outterSize.height - innerSize.height) / 2;
    	
    	innerWindow.setLocation(x, y);
    }
    
    public static void constrainTo(Dimension dim, int size) {
    	
    	boolean widthBigger = dim.width > dim.height;
    	
    	int oldW = dim.width;
    	int oldH = dim.height;
    	
    	if (widthBigger) {
    		dim.height = (int)((dim.height / (double)dim.width) * size);
    		dim.width = size;
    	} else {
    		dim.width = (int)((dim.width / (double)dim.height) * size);
    		dim.height = size;
    	}
    	
    	//System.out.println ("Constraining " + oldW + "." + oldH + " to " + dim.width + "." + dim.height);
    }

    /**
     * Don't show the mouse pointer for this component
     * @param component
     */
    public static void hidePointer ( Component component )
    {
        component.setCursor( emptyCursor );
    }
    
    /**
     * Set the mouse pointer for this component to the default system cursor
     * 
     * @param component
     */
    public static void showPointer ( Component component )
    {
        component.setCursor( Cursor.getDefaultCursor() );
    }
    
    
}
