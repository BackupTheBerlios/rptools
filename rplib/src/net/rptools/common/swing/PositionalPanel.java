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
package net.rptools.common.swing;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 */
public class PositionalPanel extends JPanel {

	public PositionalPanel() {
		setLayout(new PositionalLayout());
	}
	
	public void addImpl(Component comp, Object constraints, int index) {
		
		if (!(constraints instanceof PositionalLayout.Position)) {
			throw new IllegalArgumentException("Use add(Component, PositionalLayout.Position)");
		}
		
		super.addImpl(comp, constraints, index);

		if (((PositionalLayout.Position) constraints) == PositionalLayout.Position.CENTER) {
		
			setComponentZOrder(comp, getComponents().length - 1);
		} else {
			setComponentZOrder(comp, 0);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#isOptimizedDrawingEnabled()
	 */
	public boolean isOptimizedDrawingEnabled() {
		return false;
	}
}
