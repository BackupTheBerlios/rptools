package net.rptools.tokentool;

import java.awt.image.BufferedImage;
import java.util.List;

import net.rptools.common.swing.ImagePanel;
import net.rptools.common.swing.SelectionListener;

public class OverlayPanel extends ImagePanel implements SelectionListener {

	public OverlayPanel() {
		setModel(new OverlayPanelModel());
		setSelectionMode(SelectionMode.SINGLE);
		
		addSelectionListener(this);
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
}
