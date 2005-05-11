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
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class TokenCompositor {

    private static final int BACKGROUND_COLOR = 0xff00ff;
	private static final int COLOR_MASK = 0xffffff;

    public static BufferedImage composeToken(BufferedImage overlayImage, BufferedImage tokenImage, int offsetX, int offsetY, double scale) {
        
        if (overlayImage == null) {
            throw new IllegalArgumentException("Must have both an overlay and a token");
        }
        
        BufferedImage composedImage = new BufferedImage(overlayImage.getWidth(), overlayImage.getHeight(), Transparency.OPAQUE);
        
        Graphics g = null;
        try {
            g = composedImage.getGraphics();
            
            g.setColor(Color.white);
            g.fillRect(0, 0, overlayImage.getWidth(), overlayImage.getHeight());
            if (tokenImage != null) {
                g.drawImage(tokenImage, -offsetX, -offsetY, (int)(tokenImage.getWidth() * scale), (int)(tokenImage.getHeight() * scale), null);
            }
            
            g.drawImage(overlayImage, 0, 0, null);
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
        
        return translateOverlay(composedImage);
    }
    
    public static BufferedImage translateOverlay(BufferedImage overlayImage) {

        if (overlayImage == null) {
            return null;
        }
        
        // Needs to be translucent
        BufferedImage translatedOverlayImage = new BufferedImage(overlayImage.getWidth(), overlayImage.getHeight(), Transparency.TRANSLUCENT);

        for (int y = 0; y < overlayImage.getHeight(); y++) {
            
            for (int x = 0; x < overlayImage.getWidth(); x++) {
                
                // Get the overlay pixel at the current location
                int color = overlayImage != null ? overlayImage.getRGB(x, y) : BACKGROUND_COLOR;

                // Background
                if ((color & COLOR_MASK) == BACKGROUND_COLOR) {
                    
                    color = 0x00; // Totally transparent
                }
                
                translatedOverlayImage.setRGB(x, y, color);
            }
        }
        
        return translatedOverlayImage;
    }
        
}
