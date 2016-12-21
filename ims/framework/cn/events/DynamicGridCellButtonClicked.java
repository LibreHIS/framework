package ims.framework.cn.events;

import java.io.Serializable;

public class DynamicGridCellButtonClicked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
    
	public DynamicGridCellButtonClicked(int controlID, int rowId, int column)
    {
        this.controlID = controlID;
        this.rowId = rowId;
        this.column = column;
        
    }
    
    public DynamicGridCellButtonClicked(int controlID, int rowId, int column, int tableRowId, int tableCellId)
    {
        this.controlID = controlID;
        this.rowId = rowId;
        this.column = column;
        this.tableRowId = tableRowId;
        this.tableCellId = tableCellId;
    }
    
    public int getControlID()
    {
        return this.controlID;
    }
    public int getRowId()
    {
        return this.rowId;
    }
    public int getColumn()
    {
        return this.column;
    }    
    public int getTableRowId()
    {
        return this.tableRowId;
    }    
    public int getTableCellId()
    {
        return this.tableCellId;
    }    
    private int controlID;
    private int rowId;
    private int column;
    private int tableRowId;
    private int tableCellId;
}
