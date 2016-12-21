package ims.framework.controls;

import java.io.Serializable;

import ims.framework.utils.Color;
import ims.framework.utils.Image;

/**
 * @author mchashchin
 */
final public class GridComboBox implements Serializable
{
	private static final long serialVersionUID = 1L;
	public GridComboBox(Grid grid, int index)
	{
		this.grid = grid;
		this.columnIndex = index;
	}
	public void newRow(Object value, String text)
	{
		newRow(value, text, null, null);
	}
    public void newRow(Object value, String text, Image image)
    {
        newRow(value, text, image, null);
    }
    public void newRow(Object value, String text, Color textColor)
    {
        newRow(value, text, null, textColor);
    }
    //
    // image and textColor are ignored for now, 
    // will implement this later when jsCN support is added
    // @mmihalec
    //
    public void newRow(Object value, String text, Image image, Color textColor)
    {
    	this.grid.columnNewRow(this.columnIndex, value, text, image, textColor);
    }
	public void clear()
	{
		this.grid.columnClear(this.columnIndex);
	}
	public int size()
	{
		return this.grid.columnSize(this.columnIndex);
	}
	
	private Grid grid;
	private int columnIndex; 
} 
