/* The MIT License
 * 
 * Copyright (c) 2005 David Rice, Trevor Croft
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
package net.rptools.maptool.client;

public class AppState {

    private static boolean dropTokenAsInvisible;
    private static boolean showGrid;
    private static boolean showTokenNames;

    public static boolean isDropTokenAsInvisible() {
        return dropTokenAsInvisible;
    }

    public static void setDropTokenAsInvisible(boolean dropTokenAsInvisible) {
        AppState.dropTokenAsInvisible = dropTokenAsInvisible;
    }

    public static boolean isShowGrid() {
    	return showGrid;
    }
    
    public static void setShowGrid(boolean flag) {
    	showGrid = flag;
    }

    public static void setShowTokenNames(boolean flag) {
    	showTokenNames = flag;
    }
    
    public static boolean isShowTokenNames() {
    	return showTokenNames;
    }
}
