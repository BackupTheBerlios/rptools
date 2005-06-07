package net.rptools.tokentool;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import net.rptools.common.ImageTransferable;
import net.rptools.common.swing.ImagePanel;
import net.rptools.common.swing.SelectionListener;
import net.rptools.common.util.ImageUtil;

public class OverlayPanel extends ImagePanel implements SelectionListener, DropTargetListener {

	public OverlayPanel() {
		setModel(new OverlayPanelModel());
		setSelectionMode(SelectionMode.SINGLE);
		
		addSelectionListener(this);

        // DnD
        new DropTarget(this, this);
    }
	
	////
	// SELECTION LISTENER
	public void selectionPerformed(List<Object> selectedList) {

		// There should be exactly one
		if (selectedList.size() != 1) {
			return;
		}
		
		Integer imageIndex = (Integer) selectedList.get(0);
		
		TokenTool.getFrame().setOverlay((BufferedImage) getModel().getImage(imageIndex.intValue()));
	}
    
    ////
    // DROP LISTENER
    public void dragEnter(DropTargetDragEvent dtde) {}
    public void dragExit(DropTargetEvent dte) {}
    public void dragOver(DropTargetDragEvent dtde) {}
    public void dropActionChanged(DropTargetDragEvent dtde) {}
    public void drop(DropTargetDropEvent dtde) {
        
        Transferable transferable = dtde.getTransferable();
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        
        // TODO: Combine this code with TokenCompositionPanel
        if (transferable.isDataFlavorSupported(ImageTransferable.FLAVOR)) {

            try {
                TokenTool.addOverlayImage((BufferedImage) transferable.getTransferData(ImageTransferable.FLAVOR));
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
            
            for (File file : list) {
                try {
                    TokenTool.addOverlayImage(file);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
        }        
        
    }
}
