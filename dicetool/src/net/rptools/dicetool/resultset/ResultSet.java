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
package net.rptools.dicetool.resultset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rptools.dicetool.dice.Expression;
import net.rptools.dicetool.dice.SimpleExpression;


/**
 * @author drice
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ResultSet {
	private static ThreadLocal<ResultSet> threadLocal = new ThreadLocal<ResultSet>();
	
	private final List<Row> rows = new ArrayList<Row>();
	private int currentRow = -1;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	public ResultSet() {
		setCurrent(this);
	}
    
    public void addExpression(String label, String definition, Map variables, int value) {
        addExpression(new SimpleExpression(label, definition, variables, Integer.valueOf(value)));
    }

	public void addExpression(Expression e) {
		Row row = new Row(e);
		rows.add(row);
		currentRow = rows.size() - 1;
	
		row.setResult(e.getValue());
	}

	public boolean hasAttribute(String key) { return attributes.containsKey(key); }
	public Object getAttribute(String key) { return attributes.get(key); }
	public void setAttribute(String key, Object value) { attributes.put(key, value); }

	public List<Row> getRows() { return this.rows; }
	public Row getCurrentRow() { return (Row) this.rows.get(currentRow); }
	public int getCurrentRowIndex() { return currentRow; }

	public static boolean hasCurrent() { return threadLocal.get() != null; }
	public static ResultSet getCurrent() { return (ResultSet) threadLocal.get(); }
	public static void setCurrent(ResultSet rs) { threadLocal.set(rs); }
}
