/*
 * $Id: TokenToolFrame.java,v 1.1 2005/05/10 03:21:08 tcroft Exp $
 *
 * Copyright (C) 2005, Digital Motorworks LP, a wholly owned subsidiary of ADP.
 * The contents of this file are protected under the copyright laws of the
 * United States of America with all rights reserved. This document is
 * confidential and contains proprietary information. Any unauthorized use or
 * disclosure is expressly prohibited.
 */
package net.rptools.tokentool;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import net.rptools.common.util.ImageUtil;

public class TokenToolFrame extends JFrame {

    private TokenCompositionPanel compositionPanel;
    private JFileChooser saveChooser;
    
    public TokenToolFrame() {

    	super("TokenTool");
        setSize(300, 300);
        setLocation(50, 0); // TODO: make this more intelligent
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setLayout(new BorderLayout());
        
        compositionPanel = new TokenCompositionPanel();
        try { // TEMPORARY
            compositionPanel.setOverlay(ImageUtil.getImage("net/rptools/tokentool/image/overlay/circle.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        
        add(BorderLayout.CENTER, compositionPanel);

        saveChooser = new JFileChooser();
    }
    
    public JFileChooser getSaveFileChooser() {
        return saveChooser;
    }
}
