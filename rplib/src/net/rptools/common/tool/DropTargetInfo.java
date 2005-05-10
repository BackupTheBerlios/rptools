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

package net.rptools.common.tool;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JFrame;

/**
 * This class will show a frame that accepts system drag-and-drop events
 * it is a discovery tool useful to determine which flavors a drop from a specific application 
 * supports (such as a browser)
 */
public class DropTargetInfo extends JFrame implements DropTargetListener{
    
    public DropTargetInfo() {
        super ("Drag and drop into this window");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(300,200);
        setSize(200, 200);
        
        new DropTarget(this, this);
    }
    
    public static void main(String[] args) {
        
        DropTargetInfo dti = new DropTargetInfo();
        
        dti.setVisible(true);
    }
    
    ////
    // DROP TARGET LISTENER
    public void dragEnter(DropTargetDragEvent dtde) {
    }
    
    public void dragExit(DropTargetEvent dte) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }
    
    public void drop(DropTargetDropEvent dtde) {
        System.out.println ("DropAction:" + dtde.getDropAction());
        System.out.println ("Source:" + dtde.getSource());
        System.out.println ("DropTargetContext:" + dtde.getDropTargetContext());
        System.out.println ("Location:" + dtde.getLocation());
        System.out.println ("Transferable:" + dtde.getTransferable());
        System.out.println ("Data Flavors:");
        for (DataFlavor flavor : dtde.getCurrentDataFlavorsAsList()) {
            System.out.println ("\t" + flavor.getMimeType());
        }
    }
    
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }
}
