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

import java.util.Stack;

import net.rptools.dicetool.DiceTool;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;


/**
 * @author drice
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Roll extends PostfixMathCommand {
	public Roll() {
		numberOfParameters = -1;
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
		int times = 1;
		int randomRange = 20;
		double result = 0;
		if (curNumberOfParameters == 1) {
			randomRange = getIntParameter(stack.pop());
		} else if (curNumberOfParameters == 2) {
			randomRange = getIntParameter(stack.pop());
			times = getIntParameter(stack.pop());
		} else {
			throw new ParseException("Illegal number of parameters");
		}
		// push the result on the inStack
		stack.push(new Integer(doRoll(times, randomRange)));
	}
	
	protected static int getIntParameter(Object param) throws ParseException {
		if (param instanceof Number) {
			return ((Number) param).intValue();
		} else {
			throw new ParseException("Invalid parameter type");
		}
	}
	
	protected int doRoll(int times, int range) {
		int result = 0;
		for (int i = 0; i < times; i++) {
			result += DiceTool.RANDOM.nextInt(range) + 1;
		}
		return result;
	}
}
