package net.rptools.tokentool;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.rptools.common.swing.ImagePanelModel;
import net.rptools.common.util.FileUtil;
import net.rptools.common.util.ImageUtil;

public class OverlayPanelModel implements ImagePanelModel {

	private File[] imageFiles;
	private Map<File, BufferedImage> imageMap;
	
	public OverlayPanelModel() {
		
		imageFiles = AppConstants.OVERLAY_DIR.listFiles(ImageUtil.SUPPORTED_IMAGE_FILE_FILTER);
		imageMap = new HashMap<File, BufferedImage>();
	}
	
	public int getImageCount() {
		return imageFiles.length;
	}

	public Transferable getTransferable(int index) {
		return null;
	}

	public Object getID(int index) {
		return new Integer(index);
	}

	public Image getImage(Object ID) {
		
		return null;
	}

	public Image getImage(int index) {
		BufferedImage image = imageMap.get(imageFiles[index]);
		if (image == null) {
			try {
				image = ImageUtil.getImage(imageFiles[index]);
				imageMap.put(imageFiles[index], image);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		return image;
	}

}
