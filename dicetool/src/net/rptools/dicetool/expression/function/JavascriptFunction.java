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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import net.rptools.dicetool.DiceTool;
import net.rptools.dicetool.resultset.ResultSet;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;
import org.nfunk.jep.function.PostfixMathCommandI;


/**
 * @author drice
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class JavascriptFunction extends PostfixMathCommand {
    private final Function javascriptFunction;

    public JavascriptFunction(Function func) {
        numberOfParameters = -1;
        this.javascriptFunction = func;
    }

    /**
     * Calculates the result of summing up all parameters, which are assumed to
     * be of the Double type.
     */
    public void run(Stack stack) throws ParseException {
        Context cx = Context.enter();
        try {
            Object functionArgs[] = getParameters(stack);
            Scriptable scope = cx.initStandardObjects();

            scope.put("resultSet", scope, Context.toObject(ResultSet
                    .getCurrent(), scope));
            scope.put("row", scope, Context.toObject(ResultSet.getCurrent()
                    .getCurrentRow(), scope));
            scope.put("vars", scope, Context.toObject(ResultSet.getCurrent()
                    .getCurrentRow().getExpression().getVariables(), scope));
            scope.put("rand", scope, Context.toObject(DiceTool.RANDOM, scope));
            scope.put("out", scope, Context.toObject(System.out, scope));

            Object result = javascriptFunction.call(cx, scope, scope,
                    functionArgs);

            stack.push(new Double(Context.toNumber(result)));
        } catch (JavaScriptException e) {
            throw new ParseException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Context.exit();
        }
    }

    private Object[] getParameters(Stack stack) {
        Object[] ret = new Object[curNumberOfParameters];

        for (int i = curNumberOfParameters - 1; i >= 0; i--) {
            ret[i] = stack.pop();
        }

        return ret;
    }

    private static final Map<String, PostfixMathCommandI> loadJavascriptFunctions(
            InputStream in) {
        Map<String, PostfixMathCommandI> ret = new HashMap<String, PostfixMathCommandI>();
        Context cx = Context.enter();
        try {
            cx.setCompileFunctionsWithDynamicScope(true);
            Scriptable scope = cx.initStandardObjects();

            cx.evaluateReader(scope, new InputStreamReader(in), "<cmd>", 1,
                    null);

            // ScriptableObject.defineClass(scope, ResultSet.class);

            Object fObj = scope.get("registerFunctions", scope);

            if (!(fObj instanceof Function))
                throw new IOException(
                        "registerFunctions is undefined or not a function.");

            Object functionArgs[] = {};
            Function f = (Function) fObj;
            Object result = f.call(cx, scope, scope, functionArgs);

            Map m = (Map) Context.toType(result, Map.class);

            for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();

                ret.put((String) entry.getKey(), new JavascriptFunction(
                        (Function) entry.getValue()));
            }
        } catch (JavaScriptException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            Context.exit();
        }
        return ret;
    }

    public static final Map<String, PostfixMathCommandI> getJavascriptFunctions() {
        Map<String, PostfixMathCommandI> ret = new HashMap<String, PostfixMathCommandI>();

        ret.putAll(loadJavascriptFunctions(JavascriptFunction.class
                .getResourceAsStream("functions.js")));
        try {
            ret.putAll(loadJavascriptFunctions(new FileInputStream(
                    "functions.js")));
        } catch (FileNotFoundException e) {
            System.err.println("File: functions.js not found!");
        }

        return ret;
    }
}
