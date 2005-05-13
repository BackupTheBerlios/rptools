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

package net.rptools.tokentool;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

public class AppActions {

    public static final Action EXIT_APP = new AbstractAction () {
        
       {
           putValue(Action.NAME, "Exit");
           putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK));
       }
       
       public void actionPerformed(ActionEvent e) {

           System.exit(0);
        }
    };
    
    public static final Action SAVE_TOKEN = new AbstractAction () {
     
        {
            putValue(Action.NAME, "Save Token");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        }
        
        public void actionPerformed(java.awt.event.ActionEvent e) {
            
            new Thread() {
                public void run() {
                    
                    File file = TokenTool.getFrame().showSaveDialog(); 
                    if (file != null) {
                        
                        if (!file.getName().toUpperCase().endsWith(".PNG")) {
                            file = new File(file.getAbsolutePath() + ".png");
                        }
                        if (file.exists()) {
                            if (!TokenTool.confirm("File exists.  Overwrite?")) {
                                return;
                            }
                        }
                        
                        try {
                            ImageIO.write(TokenTool.getFrame().getComposedToken(), "png", file);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            TokenTool.showError("Unable to write image: " + ioe);
                        }
                    }
                }
            }.start ();

        }
    };
}
