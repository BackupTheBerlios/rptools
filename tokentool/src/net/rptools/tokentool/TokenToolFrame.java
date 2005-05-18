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

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.rptools.common.swing.JSplitPaneEx;
import net.rptools.common.util.ImageUtil;

public class TokenToolFrame extends JFrame {

    private TokenCompositionPanel compositionPanel;
    private JFileChooser saveChooser;
    
    public TokenToolFrame() {

    	super("TokenTool");
        setSize(550, 400);
        setLocation(50, 0); // TODO: make this more intelligent
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setLayout(new BorderLayout());
        
        compositionPanel = new TokenCompositionPanel();
        
		JSplitPaneEx splitPane = new JSplitPaneEx();
		splitPane.setOrientation(JSplitPaneEx.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(75);
        
		splitPane.setLeftComponent(new JScrollPane(new OverlayPanel()));
		splitPane.setRightComponent(compositionPanel);
		
        setJMenuBar(new AppMenuBar());
        add(BorderLayout.CENTER, splitPane);

        saveChooser = new JFileChooser();
    }

	public void setOverlay(BufferedImage image) {
		compositionPanel.setOverlay(image);
	}
	
    public BufferedImage getComposedToken() {
        return compositionPanel.getComposedToken();
    }
    
    public File showSaveDialog() {
        
        int action = saveChooser.showSaveDialog(TokenTool.getFrame());
        return action == JFileChooser.APPROVE_OPTION ? saveChooser.getSelectedFile() : null;
    }
}
