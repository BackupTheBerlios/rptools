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
import java.util.HashMap;
import java.util.Map;
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
public class Total extends PostfixMathCommand {
	private static Random random = new SecureRandom();
	
	private static String CURRENT_GROUP_COUNT = Total.class.getName() + "#attrCurrentGroupCount";
	private static String COLOR_MAP = Total.class.getName() + "#attrColorMap";
	
	public Total() {
		numberOfParameters = 2;
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

		ResultSet rs = ResultSet.getCurrent();
		Row row = rs.getCurrentRow();
		
		// Get the result passed into this function
		int result = getIntParameter(stack.pop());

		// Get the total from the ResultSet attributes
		String group = String.valueOf(getIntParameter(stack.pop()));
		String groupKey = Total.class.getName() + "#" + group;
		int total = 0;
		if (rs.hasAttribute(groupKey)) {
			total = ((Double) rs.getAttribute(groupKey)).intValue();
		}
		total += result;
		
		rs.setAttribute(groupKey, new Double(total));
		row.setTotal(new Integer(total));

		row.setBackgroundColor(getCurrentColor(rs, groupKey));
		
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
	
	private Color getCurrentColor(ResultSet rs, String groupKey) {
		// Colorization of alternate groups
		int currentGroupCount = 0;
		if (rs.hasAttribute(CURRENT_GROUP_COUNT)) {
			currentGroupCount = ((Integer) rs.getAttribute(CURRENT_GROUP_COUNT)).intValue();
		}

		Map colorMap;
		if (!rs.hasAttribute(COLOR_MAP)) {
			rs.setAttribute(COLOR_MAP, new HashMap());
		}
		colorMap = (Map) rs.getAttribute(COLOR_MAP);
		
		if (!colorMap.containsKey(groupKey)) {
			colorMap.put(groupKey, currentGroupCount % 2 == 0 ? new Color(255, 255, 220) : new Color(255, 220, 255));
			rs.setAttribute(CURRENT_GROUP_COUNT, new Integer(++currentGroupCount));
		}
		
		return (Color) colorMap.get(groupKey);
	}
}
