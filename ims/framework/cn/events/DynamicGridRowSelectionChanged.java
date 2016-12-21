package ims.framework.cn.events;

import ims.framework.enumerations.MouseButton;

import java.io.Serializable;

public class DynamicGridRowSelectionChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DynamicGridRowSelectionChanged(int controlID, int row, int rowId, MouseButton mouseButton)
	{
		this.controlID = controlID;
        this.row = row;
		this.rowId = rowId;
		this.mouseButton = mouseButton;
	}
    
	public int getControlID()
	{
		return this.controlID;
	}
	public int getRow()
	{
		return this.row;
	}
    public int getRowId()
    {
        return this.rowId;
    }
    public MouseButton getMouseButton()
    {
        return this.mouseButton;
    }
    
	private int controlID;
	private int row;
    private int rowId;
    private MouseButton mouseButton;
}
