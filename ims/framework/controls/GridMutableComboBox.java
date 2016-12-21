package ims.framework.controls;

import java.io.Serializable;

final public class GridMutableComboBox  implements Serializable
{
	private static final long serialVersionUID = 1L;
	public GridMutableComboBox(GridRow row, int column)
	{
		this.row = row;
		this.column = column;		
	} 
	public void newRow(Object value, String text)
	{
		this.row.newRow(this.column, value, text);
	}
	public void clear()
	{
		this.row.clear(this.column);
	}
	public Object getValue()
	{
		return this.row.getValue(this.column);
	}
	public void setValue(Object value)
	{
		this.row.setValue(this.column, value);
	}
	public void showOpened()
	{
		this.row.showOpened(this.column);
	}
	public String getEditedText()
    {
    	return this.row.getEditedText(this.column);
    }
    public void setEditedText(String value)
    {
    	this.row.setEditedText(this.column, value);
    }
    public int getMinNumberOfChars()
    {
    	return this.row.getMinNumberOfChars(this.column);
    }
    public void setMinNumberOfChars(int value)
    {
    	this.row.setMinNumberOfChars(this.column, value);
    }

	private GridRow row;
	private int column;
} 
