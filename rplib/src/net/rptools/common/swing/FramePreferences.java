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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

/**
 * Automatically keeps track of and restores frame size when opening/closing
 * the application.
 * 
 * To use, simply add a line like this to you frame's constructor:
 * 
 *      addWindowListener(new FramePreferences(appName, this));
 */
public class FramePreferences extends WindowAdapter {

    private Preferences          prefs;

    private static final String  KEY_X                         = "x";
    private static int     DEFAULT_X;

    private static final String  KEY_Y                         = "y";
    private static int     DEFAULT_Y;

    private static final String  KEY_WIDTH                     = "width";
    private static int     DEFAULT_WIDTH;

    private static final String  KEY_HEIGHT                    = "height";
    private static int     DEFAULT_HEIGHT;


    public FramePreferences(String appName, JFrame frame) {
        prefs = Preferences.userRoot().node(appName + "/frame/" + frame.getTitle());        
        
        DEFAULT_X = frame.getLocation().x;
        DEFAULT_Y = frame.getLocation().y;
        
        DEFAULT_WIDTH = frame.getSize().width;
        DEFAULT_HEIGHT = frame.getSize().height;
        
        restorePreferences(frame);
    }
    
    /**
     * Clear out window preferences from the user's system
     */
    public void clear() {
        try {
            prefs.clear();
        } catch (BackingStoreException bse) {
            // This error shouldn't matter, really,
            // since it is an asthetic action
            bse.printStackTrace();
        }
    }
    
    ////
    // Preferences

    protected int getX() {
        return prefs.getInt(KEY_X, DEFAULT_X);
    }

    protected void setX(int x) {
        prefs.putInt(KEY_X, x);
    }

    protected int getY() {
        return prefs.getInt(KEY_Y, DEFAULT_Y);
    }

    protected void setY(int y) {
        prefs.putInt(KEY_Y, y);
    }

    protected int getWidth() {
        return prefs.getInt(KEY_WIDTH, DEFAULT_WIDTH);
    }

    protected void setWidth(int width) {
        prefs.putInt(KEY_WIDTH, width);
    }

    protected int getHeight() {
        return prefs.getInt(KEY_HEIGHT, DEFAULT_HEIGHT);
    }

    protected void setHeight(int height) {
        prefs.putInt(KEY_HEIGHT, height);
    }
    
    protected void storePreferences(JFrame frame) {

        setX(frame.getLocation().x);
        setY(frame.getLocation().y);
        
        setWidth(frame.getSize().width);
        setHeight(frame.getSize().height);
    }
    
    protected void restorePreferences(JFrame frame) {
        
        frame.setLocation(getX(), getY());
        frame.setSize(getWidth(), getHeight());
    }
    
    ////
    // WINDOW ADAPTER
    public final void windowClosing(WindowEvent e) {
        
        storePreferences((JFrame) e.getSource());
    }
}
