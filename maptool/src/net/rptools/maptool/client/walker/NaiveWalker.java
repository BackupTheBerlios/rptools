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
package net.rptools.maptool.client.walker;

import java.util.ArrayList;
import java.util.List;

import net.rptools.maptool.client.CellPoint;
import net.rptools.maptool.model.Zone;

public class NaiveWalker extends AbstractZoneWalker {

	public NaiveWalker (Zone zone) {
		super(zone);
	}
	
	private int distance;
	
	@Override
	protected List<CellPoint> calculatePath(CellPoint start, CellPoint end) {

		List<CellPoint> list = new ArrayList<CellPoint>();
		
		int x = start.x;
		int y = start.y;
		
		int count = 0;
		while (true && count < 100) {
			
			list.add(new CellPoint(x, y));
			
			if (x == end.x && y == end.y) {
				break;
			}
			
			if (x < end.x) x++;
			if (x > end.x) x--;
			if (y < end.y) y++;
			if (y > end.y) y--;
			
			count ++;
		}

		distance = (list.size()-1) * 5;
		return list;
	}

	@Override
	public int getDistance() {

		return distance;
	}
}
