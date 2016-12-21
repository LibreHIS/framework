package ims.framework.controls;

import ims.framework.utils.Color;

public class DynamicGridCellOptions 
{
	Color textColor;
	Color backColor;
	boolean bold;
	boolean readOnly;
	
	public static DynamicGridCellOptions DEFAULT = new DynamicGridCellOptions(null, null, false, false);
	public static DynamicGridCellOptions READ_ONLY_DATA_CELL = new DynamicGridCellOptions(null, Color.LightGray, false, true);
	public static DynamicGridCellOptions LABEL_CELL = new DynamicGridCellOptions(Color.Blue, Color.LightGray , false, true);
	public static DynamicGridCellOptions EDITABLE_DATA_CELL = new DynamicGridCellOptions(null, null, false, false);
	
	public Color getBackColor() 
	{
		return backColor;
	}
	public boolean isBold() 
	{
		return bold;
	}
	public boolean isReadOnly() 
	{
		return readOnly;
	}
	public Color getTextColor() 
	{
		return textColor;
	}
	
	private DynamicGridCellOptions(Color textColor, Color backColor, boolean bold, boolean readOnly)
	{
		this.textColor = textColor;
		this.backColor = backColor;
		this.bold = bold;
		this.readOnly = readOnly;
	}
}
