/* The MIT License
 * 
 * Copyright (c) 2004,2005 David Rice
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
package net.rptools.dicetool.ui;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTable;

import net.rptools.dicetool.resultset.Row;

/**
 * @author drice
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ResultTable extends JTable {
    private static final long serialVersionUID = 2156919317369449594L;
    private final MainFrame tool;
	public ResultTable(MainFrame tool) {
		super();
		this.tool = tool;
	}
	
	
	
	public String getToolTipText(MouseEvent event) {
		int row = rowAtPoint(event.getPoint());
		
		return getToolTip(row);
	}
	
	public Point getToolTipLocation(MouseEvent event) {
		int row = rowAtPoint(event.getPoint());
		int col = columnAtPoint(event.getPoint());
		
		String s = getToolTip(row);
		
		if (s == null || s.length() == 0)
			return null;

		Point pt = getCellRect(row, col, true).getLocation();
		pt.translate(-1, -2);
		return pt;
	}
	
	public String getToolTip(int rowIndex) {
		RowTableModel m = (RowTableModel) this.getModel();

		Row r = m.getRow(rowIndex);

		if (r == null)
			return null;

		return r.format();
	}
}