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

import java.awt.Color;

import net.rptools.dicetool.dice.Expression;



public class Row {
	private String label;
	private final Expression expression;
	
	private Object result;
	private Object total;

	private String reRollExpression; 
	
	private Color foregroundColor;
	private Color backgroundColor;
	
	Row(Expression expression) {
		this.expression = expression;
	}

	public String getLabel() {
		if (label != null) {
			return label;
		} else {
			return expression.getLabel();
		}
	}
	
	public void setLabel(String label) { this.label = label; }

	public Expression getExpression() { return expression; }

	public void setReRollExpression(String reRollExpression) { this.reRollExpression = reRollExpression; }
	public String getReRollExpression() {
		if (reRollExpression != null) {
			return reRollExpression;
		} else {
			return expression.getLabel();
		}
	}
	
	public Object getResult() { return result; }
	public void setResult(Object result) { this.result = result; }

	public Object getTotal() { return total; }
	public void setTotal(Object total) { this.total = total; }

	public Color getForegroundColor() { return foregroundColor; }
	public void setForegroundColor(Color color) {
		this.foregroundColor = color;
	}
	
	public void setForegroundColor(int red, int green, int blue) {
		setForegroundColor(new Color(red, green, blue));
	}

	public Color getBackgroundColor() { return backgroundColor; }
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	public void setBackgroundColor(int red, int green, int blue) {
		setBackgroundColor(new Color(red, green, blue));
	}
	
	public String format() {
		StringBuffer sb = new StringBuffer(128);
		
		sb.append(expression.getLabel());
		if (reRollExpression != null) {
			sb.append(" - [").append(reRollExpression).append("]");
		}
		
		return sb.toString();
	}
}