/*
 * $Id: TokenCompositor.java,v 1.1 2005/05/10 03:21:08 tcroft Exp $
 *
 * Copyright (C) 2005, Digital Motorworks LP, a wholly owned subsidiary of ADP.
 * The contents of this file are protected under the copyright laws of the
 * United States of America with all rights reserved. This document is
 * confidential and contains proprietary information. Any unauthorized use or
 * disclosure is expressly prohibited.
 */
package net.rptools.tokentool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import net.rptools.common.util.ImageUtil;

public class TokenCompositor {

    private static final int BACKGROUND_COLOR = 0xffff00ff;

    public static BufferedImage composeToken(BufferedImage overlayImage, BufferedImage tokenImage, int offsetX, int offsetY, double scale) {
        
        if (overlayImage == null || tokenImage == null) {
            throw new IllegalArgumentException("Must have both an overlay and a token");
        }
        
        BufferedImage composedImage = new BufferedImage(overlayImage.getWidth(), overlayImage.getHeight(), Transparency.BITMASK);
        
        Graphics g = null;
        try {
            g = composedImage.getGraphics();
            
            g.setColor(Color.white);
            g.fillRect(0, 0, overlayImage.getWidth(), overlayImage.getHeight());
            g.drawImage(tokenImage, -offsetX, -offsetY, (int)(tokenImage.getWidth() * scale), (int)(tokenImage.getHeight() * scale), null);
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
                if ((color & BACKGROUND_COLOR) == BACKGROUND_COLOR) {
                    
                    // Get the respective background pixel
                    color = 0x00;
                }
                
                translatedOverlayImage.setRGB(x, y, color);
            }
        }
        
        return translatedOverlayImage;
    }
        
}
