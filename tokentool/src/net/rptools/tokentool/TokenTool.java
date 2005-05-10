/*
 * $Id: TokenTool.java,v 1.1 2005/05/10 03:21:08 tcroft Exp $
 *
 * Copyright (C) 2005, Digital Motorworks LP, a wholly owned subsidiary of ADP.
 * The contents of this file are protected under the copyright laws of the
 * United States of America with all rights reserved. This document is
 * confidential and contains proprietary information. Any unauthorized use or
 * disclosure is expressly prohibited.
 */
package net.rptools.tokentool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;


public class TokenTool {

    private static TokenToolFrame tokenToolFrame;
    
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Exception e) {
            System.err.println("Exception during look and feel setup: " + e);
        }

        
        tokenToolFrame = new TokenToolFrame();
        tokenToolFrame.setVisible(true);
    }
    
    public static void saveToken(BufferedImage token) {
     
        if (tokenToolFrame.getSaveFileChooser().showSaveDialog(tokenToolFrame) == JFileChooser.APPROVE_OPTION) {
            
            File file = tokenToolFrame.getSaveFileChooser().getSelectedFile();
            if (!file.getName().toUpperCase().endsWith(".PNG")) {
                file = new File(file.getAbsolutePath() + ".png");
            }
            if (file.exists()) {
                // TODO: Confirm doing this
            }
            
            try {
                ImageIO.write(token, "png", file);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
