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
package net.rptools.dicetool.expression.function;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Stack;

import net.rptools.dicetool.resultset.ResultSet;
import net.rptools.dicetool.resultset.Row;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;


/**
 * @author drice
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ForegroundColor extends PostfixMathCommand {
	private static Random random = new SecureRandom();
	
	public ForegroundColor() {
		numberOfParameters = 4;
	}
	
	/**
	 * Calculates the result of summing up all parameters, which are assumed to
	 * be of the Double type.
	 */
	public void run(Stack stack) throws ParseException {
		// Check if stack is null
		if (null == stack) {
			throw new ParseException("Stack argument null");
		}
		
		int result 	= getIntParameter(stack.pop());
		int blue 	= getIntParameter(stack.pop());
		int green 	= getIntParameter(stack.pop());
		int red 	= getIntParameter(stack.pop());
		
		ResultSet rs = ResultSet.getCurrent();
		Row row = rs.getCurrentRow();
		
		row.setForegroundColor(new Color(red, green, blue));
		
		// push the result on the inStack
		stack.push(new Double(result));
	}
	
	protected static int getIntParameter(Object param) throws ParseException {
		if (param instanceof Number) {
			return ((Number) param).intValue();
		} else {
			throw new ParseException("Invalid parameter type");
		}
	}
}
