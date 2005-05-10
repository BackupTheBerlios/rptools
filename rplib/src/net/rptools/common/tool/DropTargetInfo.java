/*
 * $Id: DropTargetInfo.java,v 1.1 2005/05/10 18:16:43 tcroft Exp $
 *
 * Copyright (C) 2005, Digital Motorworks LP, a wholly owned subsidiary of ADP.
 * The contents of this file are protected under the copyright laws of the
 * United States of America with all rights reserved. This document is
 * confidential and contains proprietary information. Any unauthorized use or
 * disclosure is expressly prohibited.
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
