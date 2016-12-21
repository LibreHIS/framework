package ims.framework.controls;

import java.io.Serializable;

final public class GridMutableAnswerBox implements Serializable
{
	private static final long serialVersionUID = 1L;
	public GridMutableAnswerBox(GridRow row, int column)
	{
		this.row = row;
		this.column = column;
	} 
	public void addOption(AnswerBoxOption option)
	{
		this.row.addAnswerBoxOption(this.column, option);
	}
	public void clear()
	{
		this.row.clearAnswerBox(this.column);
	}
	public AnswerBoxOption getValue()
	{
		return this.row.getSelectedAnswerBoxOption(this.column);
	}
	public void setValue(AnswerBoxOption value)
	{
		this.row.setSelectedAnswerBoxOption(this.column, value);
	}

	private GridRow row;
	private int column;
}
