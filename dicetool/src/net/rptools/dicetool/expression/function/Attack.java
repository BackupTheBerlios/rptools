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
import java.util.Map;
import java.util.Stack;

import net.rptools.dicetool.resultset.ResultSet;
import net.rptools.dicetool.resultset.Row;

import org.nfunk.jep.ParseException;
/**
 * @author drice
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Attack extends Roll {
	public Attack() {
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
		int critThreat = 20;
		if (curNumberOfParameters == 0) {
			// okay
		} else if (curNumberOfParameters == 1) {
			critThreat = getIntParameter(stack.pop());
		} else {
			throw new ParseException("Illegal number of parameters");
		}
		// push the result on the inStack
		
		int result = doRoll(1, 20, critThreat);
		
		Map vars = ResultSet.getCurrent().getCurrentRow().getExpression().getVariables();
		
		Number attmod = (Number) vars.get("att");
		
		if (attmod != null) {
			result += attmod.intValue();
		}
        
        Number pa = (Number) vars.get("pa");
        
        if (pa != null) {
            result += pa.intValue();
        }
		
		stack.push(new Integer(result));
	}
	protected int doRoll(int times, int range, int crit) {
		Row row = ResultSet.getCurrent().getCurrentRow();
		int value = super.doRoll(times, range);

		if (value == 1) {
			row.setLabel("miss");
			row.setForegroundColor(Color.BLUE);
		} else if (value == 20) {
			row.setLabel("hit/threat");
			row.setForegroundColor(Color.RED);
		} else if (value >= crit) {
			row.setLabel("threat");
		}
		return value;
	}
}
