package ims.framework.cn.events;

import java.io.Serializable;

public class DynamicGridNodeExpandedCollapsed implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
    public DynamicGridNodeExpandedCollapsed(int controlID, int row, int rowId, boolean expanded, boolean checked)
    {
        this.controlID = controlID;
        this.row = row;
        this.rowId = rowId;
        this.expanded = expanded;
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
    public boolean isExpanded()
    {
        return this.expanded;
    }
    public boolean isChecked()
    {
        return this.checked;
    }
    
    private int controlID;
    private int row;
    private int rowId;
    private boolean expanded;
    private boolean checked;
}
