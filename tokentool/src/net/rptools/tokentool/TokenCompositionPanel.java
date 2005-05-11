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
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import net.rptools.common.ImageTransferable;
import net.rptools.common.swing.SwingUtil;
import net.rptools.common.util.ImageUtil;

public class TokenCompositionPanel extends JComponent implements DropTargetListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private BufferedImage overlayImage;
    private BufferedImage tokenImage;
    
    private Rectangle overlayBounds;
    
    private int tokenOffsetX;
    private int tokenOffsetY;
    
    private int dragStartX;
    private int dragStartY;
    private boolean isDraggingOverlay;
    
    private double tokenScale;
    
    private BufferedImage composedOverlayImage;

    private ChangeObservable changeObservers;
    
    public TokenCompositionPanel() {
        
        // DnD
        new DropTarget(this, this);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        
        changeObservers = new ChangeObservable();
    }

    public void addChangeObserver(Observer observer) {
        changeObservers.addObserver(observer);
    }
    
    public void removeChangeObserver(Observer observer) {
        changeObservers.deleteObserver(observer);
    }
    
    @Override
    protected void paintComponent(Graphics g) {

        if (composedOverlayImage == null) {
            composedOverlayImage = TokenCompositor.translateOverlay(overlayImage);
        }
        
        Dimension size = getSize();
        
        g.setColor(Color.black);
        g.fillRect(0, 0, size.width, size.height);

        if (overlayBounds == null) {
            overlayBounds = new Rectangle((size.width - composedOverlayImage.getWidth()) / 2, (size.height - composedOverlayImage.getHeight()) / 2, overlayImage.getWidth(), overlayImage.getHeight());
        }
        
        // TOKEN
        if (tokenImage != null) {
            int width = (int)(tokenImage.getWidth() * tokenScale);
            int height = (int)(tokenImage.getHeight() * tokenScale);

            g.drawImage(tokenImage, tokenOffsetX, tokenOffsetY, width, height, this);
        }
        
        // OVERLAY
        if (composedOverlayImage != null) {
            g.drawImage(composedOverlayImage, overlayBounds.x, overlayBounds.y, this);
        }
    }
    
    public void setToken(BufferedImage tokenImage) {
        this.tokenImage = tokenImage;
        tokenOffsetX = 0;
        tokenOffsetY = 0;
        
        tokenScale = 1;
        
        repaint();
        changeObservers.fireChangeEvent();
    }
    
    public void setOverlay(BufferedImage overlayImage) {
        this.overlayImage = overlayImage;
        composedOverlayImage = null;
        repaint();
        changeObservers.fireChangeEvent();
    }
    
    
    ////
    // DROP TARGET LISTNER
    
    public void dragEnter(DropTargetDragEvent dtde) {}
    public void dragExit(DropTargetEvent dte) {}
    public void dragOver(DropTargetDragEvent dtde) {}
    public void drop(DropTargetDropEvent dtde) {
        
        Transferable transferable = dtde.getTransferable();
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        
        if (transferable.isDataFlavorSupported(ImageTransferable.FLAVOR)) {

            try {
                setToken((BufferedImage) transferable.getTransferData(ImageTransferable.FLAVOR));
                return;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            }
        }
        
        try {
            List<File> list = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            
            if (list.size() == 0) {
                return;
            }
            
            // For some reason, firefox does not actually write out the temporary file designated in
            // this list until list line is called.  So it has to stay ABOVE the loadFile() call
            // It also requires just a moment to copy from internal system whatever into the file
            dtde.dropComplete(true);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Dropping from firefox has weird semantics.  Try waiting
            // for a bit to see if it can successfully show up
            setToken(ImageUtil.getImage(list.get(0)));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
        }
        
        changeObservers.fireChangeEvent();
    }
    public void dropActionChanged(DropTargetDragEvent dtde) {}
    
    ////
    // MOUSE LISTENER
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        
        isDraggingOverlay = overlayBounds.contains(e.getX(), e.getY());
        dragStartX = e.getX();
        dragStartY = e.getY();
    }
    public void mouseReleased(MouseEvent e) {}
    
    ////
    // MOUSE MOTION LISTENEr
    public void mouseDragged(MouseEvent e) {
        
        int dx = e.getX() - dragStartX;
        int dy = e.getY() - dragStartY;

        dragStartX = e.getX();
        dragStartY = e.getY();
        
        if (isDraggingOverlay) {
            overlayBounds.x += dx;
            overlayBounds.y += dy;
        } else {
            tokenOffsetX += dx;
            tokenOffsetY += dy;
        }
        
        repaint();
        changeObservers.fireChangeEvent();
    }
    public void mouseMoved(MouseEvent e) {}
    
    ////
    // Mouse Wheel
    public void mouseWheelMoved(MouseWheelEvent e) {

        if (tokenImage == null) {
            return;
        }
        
        double delta = SwingUtil.isControlDown(e) ? .1 : .01;
        
        double newScale = 0;
        if (e.getWheelRotation() > 0) {
            newScale = tokenScale - delta;
        } else {
            newScale = tokenScale + delta;
        }

        if (newScale * tokenImage.getWidth() < 10) {
            return;
        }
        
        tokenScale = newScale;
        
        repaint();
        changeObservers.fireChangeEvent();
    }   
    
    public BufferedImage getComposedToken() {
        if (overlayBounds == null) {
            return null;
        }
        
        return TokenCompositor.composeToken(overlayImage, tokenImage, overlayBounds.x - tokenOffsetX, overlayBounds.y - tokenOffsetY, tokenScale);
    }

    private static class ChangeObservable extends Observable {
        
        public void fireChangeEvent() {
            setChanged();
            notifyObservers();
        }
    }
}
