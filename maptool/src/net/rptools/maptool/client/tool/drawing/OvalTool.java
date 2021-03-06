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
package net.rptools.maptool.client.tool.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import net.rptools.maptool.client.ScreenPoint;
import net.rptools.maptool.client.ZonePoint;
import net.rptools.maptool.client.tool.ToolHelper;
import net.rptools.maptool.client.ui.ZoneRenderer;
import net.rptools.maptool.model.drawing.Oval;
import net.rptools.maptool.model.drawing.Pen;


/**
 * @author drice
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OvalTool extends AbstractDrawingTool implements MouseMotionListener {
    private static final long serialVersionUID = 3258413928311830323L;

    protected Oval oval;
    
    public OvalTool() {
        try {
            setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("net/rptools/maptool/client/image/Tool_Draw_Circle.gif"))));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void paintOverlay(ZoneRenderer renderer, Graphics2D g) {
        if (oval != null) {
        	Pen pen = getPen();
          pen.setThickness((float)(pen.getThickness() * renderer.getScale()));
        	
            if (pen.isEraser()) {
                pen = new Pen(pen);
                pen.setEraser(false);
                pen.setColor(Color.white.getRGB());
                pen.setBackgroundColor(Color.white.getRGB());
            }

            oval.draw(g, pen, 0, 0);
            
            Point start = oval.getStartPoint();
            Point end = oval.getEndPoint();
            
            ToolHelper.drawBoxedMeasurement(renderer, g, new ScreenPoint(start.x, start.y), new ScreenPoint(end.x, end.y));
        }
    }

    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        if (oval == null) {
            oval = new Oval(x, y, x, y);
        } else {
            oval.getEndPoint().x = x;
            oval.getEndPoint().y = y;
            
            ZonePoint startPoint = ZonePoint.fromScreenPoint(zoneRenderer, (int) oval.getStartPoint().getX(), (int) oval.getStartPoint().getY()); 
            ZonePoint endPoint = ZonePoint.fromScreenPoint(zoneRenderer, (int) oval.getEndPoint().getX(), (int) oval.getEndPoint().getY());

            oval.getStartPoint().setLocation(startPoint.x, startPoint.y);
            oval.getEndPoint().setLocation(endPoint.x, endPoint.y);
            
            completeDrawable(zoneRenderer.getZone().getId(), getPen(), oval);
            oval = null;
        }

    	setIsEraser(SwingUtilities.isRightMouseButton(e));
    }

    public void mouseReleased(MouseEvent e) { 
    	
    }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
    	
    	if (oval != null) {
	        int x = e.getX();
	        int y = e.getY();
	
	        if (oval != null) {
	            oval.getEndPoint().x = x;
	            oval.getEndPoint().y = y;
	        }
	        
	        zoneRenderer.repaint();
    	}
    }
    
    /* (non-Javadoc)
	 * @see net.rptools.maptool.client.tool.drawing.AbstractDrawingTool#getPen()
	 */
	protected Pen getPen() {
		Pen pen = super.getPen();
        pen.setBackgroundMode(Pen.MODE_TRANSPARENT);

		return pen;
	}

  /**
   * @see net.rptools.maptool.client.ui.Tool#resetTool()
   */
  @Override
  protected void resetTool() {

    oval = null;
    zoneRenderer.repaint();
  }
}
