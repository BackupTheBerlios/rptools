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
package net.rptools.dicetool.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.rptools.dicetool.resultset.ResultSet;
import net.rptools.dicetool.resultset.Row;


/**
 * 
 * @author drice
 */
public class RowTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -4240626548954778007L;

    private static final String[] HEADERS = new String[] { "label", "roll",
            "total" };

    public static final int BG_COLOR_1_NDX = 0;

    public static final int BG_COLOR_2_NDX = 1;

    public static final int BG_COLOR_EMPTY = 2;

    public static final int FG_COLOR_NDX = 3;

    public static final int FG_COLOR_MAX_NDX = 4;

    public static final int FG_COLOR_MIN_NDX = 5;

    public static final Color[] SCHEME_LIGHT = new Color[] { Color.WHITE,
            new Color(230, 230, 230), new Color(230, 230, 255), Color.BLACK,
            Color.RED, Color.BLUE };

    public static final Color[] SCHEME_DARK = new Color[] { Color.WHITE,
            Color.LIGHT_GRAY, Color.CYAN, Color.BLACK, Color.RED, Color.BLUE };

    private static final int MAX_ROWS = 1000;

    private int cachedRowSize = 10;

    private List<Row> rows = new LinkedList<Row>();

    private int numberDieGroups = 0;

    private long minTotal = 0;

    private long randomTotal = 0;

    private long maxTotal = 0;

    private final Color[] scheme;

    private final Object blankRow;

    /** Creates a new instance of DieGroupTableModel */
    public RowTableModel(Color[] scheme) {
        this.scheme = scheme;
        blankRow = new Object();
    }

    public String getColumnName(int i) {
        return HEADERS[i];
    }

    public int getColumnCount() {
        return HEADERS.length;
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Row row = getRow(rowIndex);

        if (row == null)
            return null;

        Object value = null;

        switch (columnIndex) {
        case 0:
            value = row.getLabel();
            break;
        case 1:
            value = row.getResult();
            break;
        case 2:
            if (row.getTotal() != null)
                value = row.getTotal();
            break;
        }

        return new CellColorWrapper(value, row.getForegroundColor(), row
                .getBackgroundColor());
    }

    public Row getRow(int rowIndex) {
        Object o = rows.get(rowIndex);

        if (o == null || o == blankRow)
            return null;

        Row row = (Row) o;

        return row;
    }

    public void clear() {
        rows.clear();

        fireTableChanged(null); // notify everyone that we have a new table.
    }

    public int addResultSet(ResultSet rs) {
        numberDieGroups++;
        Color fc = scheme[FG_COLOR_NDX];
        Color bc = numberDieGroups % 2 == 0 ? scheme[BG_COLOR_1_NDX]
                : scheme[BG_COLOR_2_NDX];

        List<Row> tempRows = new ArrayList<Row>();

        if (rs == null) {
            tempRows.add(null);
        } else {
            int count = 0;
            for (Row row : rs.getRows()) {

                if (row.getBackgroundColor() == null) {
                    if (count % 2 == 1) {
                        row.setBackgroundColor(new Color(220, 220, 220));
                    } else {
                        row.setBackgroundColor(Color.WHITE);
                    }
                }

                if (row.getForegroundColor() == null) {
                    row.setForegroundColor(Color.BLACK);
                }

                tempRows.add(row);
                count++;
            }
        }

        this.rows.add(0, null);

        Collections.reverse(tempRows);

        // Add the temporary rows ArrayList to the Model
        for (Row r : tempRows) {
            this.rows.add(0, r);
        }

        // trim the list to MAX_ROWS
        if (this.rows.size() > MAX_ROWS) {
            this.rows.subList(MAX_ROWS, this.rows.size()).clear();
        }

        fireTableChanged(null); // notify everyone that we have a new table.

        return tempRows.size();
    }

    /**
     * @return
     */
    public long getMaxTotal() {
        return maxTotal;
    }

    /**
     * @return
     */
    public long getMinTotal() {
        return minTotal;
    }

    /**
     * @return
     */
    public long getRandomTotal() {
        return randomTotal;
    }

}
