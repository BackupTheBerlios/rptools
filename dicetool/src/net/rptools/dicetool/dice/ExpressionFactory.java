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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.rptools.dicetool.expression.function.Attack;
import net.rptools.dicetool.expression.function.BackgroundColor;
import net.rptools.dicetool.expression.function.ForegroundColor;
import net.rptools.dicetool.expression.function.JavascriptFunction;
import net.rptools.dicetool.expression.function.Roll;
import net.rptools.dicetool.expression.function.Total;

import org.nfunk.jep.function.PostfixMathCommandI;


/**
 * @author drice
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ExpressionFactory {

	public static List<Expression> getExpressions(String definition, Map<String, Double> variables) {
		List<Expression> ret = new ArrayList<Expression>();
		definition = replaceRepeatPattern(definition, GROUP_LIST_DELIMITER, GROUP_LIST_REPEAT_PATTERN);
		
		String[] parts = definition.split(GROUP_LIST_DELIMITER);
		
		for (int i = 0; i < parts.length; i++) {
			String s1 = replaceRepeatPattern(parts[i], GROUP_DELIMITER, GROUP_REPEAT_PATTERN);
			
			String[] innerParts = s1.split(GROUP_DELIMITER);

			for (int j = 0; j < innerParts.length; j++) {
				StringBuffer sb = new StringBuffer(128);
				if (innerParts.length > 1) sb.append("total(").append(i).append(", ");
				sb.append(replaceDieRoll(innerParts[j]));
				if (innerParts.length > 1) sb.append(")");
				
				ret.add(getExpression(innerParts[j], sb.toString(), variables));
			}
		}

		return ret;
	}
	
	private static final Map<String, PostfixMathCommandI> FUNCTIONS = new HashMap<String, PostfixMathCommandI>();
	static {
		FUNCTIONS.put("roll", 	new Roll());
		FUNCTIONS.put("att", 	new Attack());
		FUNCTIONS.put("total",  new Total());
		FUNCTIONS.put("fg", 	new ForegroundColor());
		FUNCTIONS.put("bg", 	new BackgroundColor());
		
		FUNCTIONS.putAll(JavascriptFunction.getJavascriptFunctions());
	}
	
	public static Expression getExpression(String label, String definition, Map variables) {
        Expression xp = new RollableExpression(label, definition, FUNCTIONS, variables);
		
		return xp;
	}
	
	private static final Pattern DIE_PATTERN = Pattern.compile("(\\d*)[dD](\\d+)");
	private static String replaceDieRoll(String definition) {
		Matcher m = DIE_PATTERN.matcher(definition);
		
		StringBuffer sb = new StringBuffer(128);
		
		while (m.find()) {
			String times = m.group(1);
			String range = m.group(2);
			
			if (times == null || times.length() == 0)
				times = "1";
			String func = "roll(" + times + ", " + range + ")";
			m.appendReplacement(sb, func);
		}
		m.appendTail(sb);
		
		return sb.toString();
	}

	private static final String GROUP_LIST_DELIMITER 		= ";";
	private static final Pattern GROUP_LIST_REPEAT_PATTERN 	= Pattern.compile("(\\d+)\\{([^\\}]+)\\}");

	private static final String GROUP_DELIMITER 			= ":";
	private static final Pattern GROUP_REPEAT_PATTERN 		= Pattern.compile("(\\d+)\\[([^\\]]+)\\]");
	
	private static String replaceRepeatPattern(String str, String delimiter, Pattern repeatPat) {
		Matcher m = repeatPat.matcher(str);
		
		StringBuffer sb = new StringBuffer(str.length()*3);
		while (m.find()) {
			int num = Integer.parseInt(m.group(1));
			String value = m.group(2);
			
			StringBuffer sbInner = new StringBuffer(64);
			for (int i = 0; i < num; i++) {
				if (i > 0) sbInner.append(delimiter);

				sbInner.append(value);
			}
			m.appendReplacement(sb, sbInner.toString());
		}
		m.appendTail(sb);
		
		return sb.toString();
	}
}
