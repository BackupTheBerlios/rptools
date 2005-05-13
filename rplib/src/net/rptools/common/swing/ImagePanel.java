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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

public class ImagePanel extends JComponent implements Scrollable, DragGestureListener, DragSourceListener, MouseListener  {

    public enum SelectionMode {
        SINGLE,
        MULTIPLE,
        NONE
    };
    
	private ImagePanelModel model;
	
	private int gridSize = 50;
	private int gridPadding = 5;
	
	private Map<Rectangle, Integer> imageBoundsMap = new HashMap<Rectangle, Integer>();

    private boolean isDraggingEnabled = true;
    private SelectionMode selectionMode = SelectionMode.NONE;
    
    private List<Object> selectedIDList = new ArrayList<Object>();
    private List<SelectionListener> selectionListenerList = new ArrayList<SelectionListener>();
    
	public ImagePanel() {

        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);        
		addMouseListener(this);
	}
    
    public void setDraggingEnabled(boolean enabled) {
        isDraggingEnabled = enabled;
    }
    
    public void setSelectionMode(SelectionMode mode) {
        selectionMode = mode;
        
        selectedIDList.clear();
        repaint();
    }
	
	public ImagePanelModel getModel() {
		return model;
	}
	
	public void setModel(ImagePanelModel model) {
		this.model = model;

		revalidate();
		JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this); 
		if (scrollPane != null) {
			scrollPane.revalidate();
			scrollPane.repaint();
		}
	}
	
    public void addSelectionListener(SelectionListener listener) {
        selectionListenerList.add(listener);
    }
    
    public void removeSelectionListener(SelectionListener listener) {
        selectionListenerList.remove(listener);
    }
    
	public boolean isOpaque() {
		return true;
	}
	
	protected void paintComponent(Graphics g) {

		Dimension size = getSize();
		
		g.setColor(getBackground());
		g.fillRect(0, 0, size.width, size.height);
		
		if (model == null) {
			return;
		}

		g.setColor(Color.black);
		
		imageBoundsMap.clear();
		
		int x = gridPadding;
		int y = gridPadding;
		for (int i = 0; i < model.getImageCount(); i++) {

			Image image = model.getImage(i);
			Dimension dim = constrainSize(image, gridSize);

			g.drawImage(image, x + (gridSize - dim.width)/2, y + (gridSize - dim.height)/2, dim.width, dim.height, this);
			
			g.drawRect(x, y, gridSize - 1, gridSize - 1);
			
            Rectangle bounds = new Rectangle(x-1, y-1, gridSize+1, gridSize+1);
			imageBoundsMap.put(bounds, i);
            
            // Selected
            // TODO: Use the image border util
            if (selectedIDList.contains(model.getID(i))) {
                g.setColor(Color.blue);
                g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
			
			x += gridSize + gridPadding;
			if (x > size.width - gridPadding - gridSize) {
				x = gridPadding;
				y += gridSize + gridPadding;
			}
		}
	}
    
    protected Object getImageIDAt(int x, int y) {
        
        for (Rectangle bounds : imageBoundsMap.keySet()) {
            
            if (bounds.contains(x, y)) {
                return model.getID(imageBoundsMap.get(bounds));
            }
        }
        
        return null;
    }

    protected void fireSelectionEvent() {
        
        List<Object> selectionList = Collections.unmodifiableList(selectedIDList);
        
        for (int i = 0; i < selectionListenerList.size(); i++) {
            selectionListenerList.get(i).selectionPerformed(selectionList);
        }
    }
    
	private Dimension constrainSize(Image image, int size) {
		
		int imageWidth = image.getWidth(this);
		int imageHeight = image.getHeight(this);
		
		if (imageWidth == imageHeight) {
			return new Dimension(size, size);
		}
		
		int width = 0;
		int height = 0;
		if (imageWidth > imageHeight) {
			
			width = size;
			height = (int)(imageHeight * ((float)size) / imageWidth);
		} else {

			height = size;
			width = (int)(imageWidth * ((float)size) / imageHeight);
		}
		
		return new Dimension(width, height);
	}
	
	public Dimension getPreferredSize() {
		int width = getSize().width;
		int height = (int)(model != null ? Math.ceil(model.getImageCount() / Math.floor(width / (gridSize + gridPadding))) * (gridSize + gridPadding) + gridPadding : gridSize + gridPadding);
		return new Dimension(width, height);
	}
	
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	protected int getImageIndexAt(int x, int y) {
		
		for (Rectangle rect : imageBoundsMap.keySet()) {
			
			if (rect.contains(x, y)) {
				return imageBoundsMap.get(rect);
			}
		}
		
		return -1;
	}
	
	////
	// SCROLLABLE
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return (gridSize + gridPadding) * 2;
	}
	
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}
	
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return gridSize / 4;
	}

	////
	// DRAG GESTURE LISTENER
	public void dragGestureRecognized(DragGestureEvent dge) {

		if (model == null || !isDraggingEnabled) {
			return;
		}
		
		int index = getImageIndexAt(dge.getDragOrigin().x, dge.getDragOrigin().y);
		if (index < 0) {
			return;
		}
		
		Transferable transferable = model.getTransferable(index);
		if (transferable == null) {
            return;
        }
        
        dge.startDrag(Toolkit.getDefaultToolkit().createCustomCursor(model.getImage(index), new Point(0, 0), "Thumbnail"), transferable, this);
	}
	
	////
	// DRAG SOURCE LISTENER
	public void dragDropEnd(DragSourceDropEvent dsde) {}
	
	public void dragEnter(DragSourceDragEvent dsde) {}
	
	public void dragExit(DragSourceEvent dse) {}
	
	public void dragOver(DragSourceDragEvent dsde) {}
	
	public void dropActionChanged(DragSourceDragEvent dsde) {}
    
    ////
    // MOUSE LISTENER
    public void mouseClicked(MouseEvent e) {}
    
    public void mouseEntered(MouseEvent e) {}
    
    public void mouseExited(MouseEvent e) {}
    
    public void mousePressed(MouseEvent e) {
        
        if (selectionMode == SelectionMode.NONE) {
            return;
        }
        
        Object imageID = getImageIDAt(e.getX(), e.getY());
        
        // TODO: Handle shift too
        if (!SwingUtil.isControlDown(e) || selectionMode == SelectionMode.SINGLE) {
            selectedIDList.clear();
        }
        
        if (imageID != null) {
            selectedIDList.add(imageID);
            
            repaint();
        }
		
		fireSelectionEvent();
    }
    
    public void mouseReleased(MouseEvent e) {}
}
