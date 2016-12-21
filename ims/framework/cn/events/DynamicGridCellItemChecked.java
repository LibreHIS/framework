package ims.framework.cn.events;

import java.io.Serializable;

public class DynamicGridCellItemChecked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
    public DynamicGridCellItemChecked(int controlID, int row, int rowId, int column, int itemIndex, boolean checked)
    {
        this.controlID = controlID;
        this.row = row;
        this.rowId = rowId;
        this.column = column;
        this.itemIndex = itemIndex;
        this.checked = checked;
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
    public int getColumn()
    {
        return this.column;
    }
    public int getItemIndex()
    {
        return this.itemIndex;        
    }
    public boolean getIsChecked()
    {
        return this.checked;        
    }
    
    private int controlID;
    private int row;
    private int rowId;
    private int column;
    private int itemIndex;
    private boolean checked = false;
}
