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
package net.rptools.dicetool.dice;

import java.util.Iterator;
import java.util.Map;

import org.nfunk.jep.JEP;
import org.nfunk.jep.function.PostfixMathCommandI;

/**
 *
 * @author  drice
 */
public class RollableExpression extends SimpleExpression {
    private boolean valueSet;
    private Object value;
    
	private JEP parser;
	private JEP minParser;
	private JEP maxParser;
	
	public RollableExpression(String label, String definition, Map functions, Map variables) {
        super(label, definition, variables, new Integer(0));
		
		parser = new JEP();
		parser.addStandardFunctions();
		for (Iterator i = functions.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry) i.next();
			parser.addFunction((String) entry.getKey(), (PostfixMathCommandI) entry.getValue());
		}
		
		parser.getSymbolTable().putAll(variables);
		
		parser.parseExpression(definition);
		
		if (parser.hasError()) 
			throw new ExceptionInInitializerError("Error parsing expression.");
	}
	
	public Object getValue() {
        if (!valueSet) {
            value = parser.getValueAsObject();
            valueSet = true;
        }

        return value;
	}
}
