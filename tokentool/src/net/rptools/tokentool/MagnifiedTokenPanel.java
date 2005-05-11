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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import net.rptools.common.FileListTransferable;
import net.rptools.common.ImageTransferable;

public class MagnifiedTokenPanel extends JComponent implements Observer, DragGestureListener, DragSourceListener {

    private static final Dimension PREFERRED_SIZE = new Dimension(150, 150);
    
    public MagnifiedTokenPanel() {
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        g.setColor(Color.white);
        g.fillRect(0, 0, PREFERRED_SIZE.width, PREFERRED_SIZE.height);
        
        BufferedImage tokenImage = TokenTool.getFrame().getComposedToken();
        if (tokenImage != null) {
            g.drawImage(tokenImage, 0, 0, PREFERRED_SIZE.width, PREFERRED_SIZE.height, this);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }
    
    @Override
    public Dimension getMinimumSize() {
        return PREFERRED_SIZE;
    }
    
    ////
    // OBSERVER
    public void update(Observable o, Object arg) {
        repaint();
    }
    
    ////
    // DRAG GESTURE LISTENER
    private File tempTokenFile = new File(".token.drag.png");
    public void dragGestureRecognized(DragGestureEvent dge) {
        
        BufferedImage tokenImage = TokenTool.getFrame().getComposedToken();
        if (tokenImage == null) {
            return;
        }

        Transferable transferable = null;
        try {
            ImageIO.write(tokenImage, "png", tempTokenFile);
            
            transferable = new FileListTransferable(tempTokenFile);
        } catch (Exception e) {
            
            transferable = new ImageTransferable(tokenImage); 
        }
            
        dge.startDrag(Toolkit.getDefaultToolkit().createCustomCursor(tokenImage, new Point(0, 0), "Thumbnail"), transferable, this);
    }
    
    ////
    // DRAG SOURCE LISTENER
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }
    public void dragEnter(DragSourceDragEvent dsde) {
    }
    public void dragExit(DragSourceEvent dse) {
    }
    public void dragOver(DragSourceDragEvent dsde) {
    }
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }
    
}
