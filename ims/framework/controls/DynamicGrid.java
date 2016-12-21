package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.DynamicGridCellButtonClicked;
import ims.framework.delegates.DynamicGridCellTextSubmited;
import ims.framework.delegates.DynamicGridCellValueChanged;
import ims.framework.delegates.DynamicGridColumnHeaderClicked;
import ims.framework.delegates.DynamicGridRowChecked;
import ims.framework.delegates.DynamicGridRowExpandedCollapsed;
import ims.framework.delegates.DynamicGridRowSelectionChanged;
import ims.framework.delegates.DynamicGridRowSelectionCleared;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.FormMode;
import ims.framework.utils.Color;

/**
 * @author mmihalec
 */
public abstract class DynamicGrid extends Control
{
	private static final long serialVersionUID = 1L;
	
    /**
     * Selects a row in the grid.
     * @param row
     * @return True if the selection was successfull, false if the row was not found.
     */
    public abstract boolean setSelectedRow(DynamicGridRow row);
    /**
     * Gets the current selected row.
     * @return Selected row or null if no row is selected.
     */
    public abstract DynamicGridRow getSelectedRow();
    /**
     * Returns the value of the current selected row.
     */    
    public abstract Object getValue();
    /**
     * Selects the row by row value.
     */
    public abstract boolean setValue(Object value);
    /**
     * Sets the grid tooltip. 
     */
    public abstract void setTooltip(String value);
    /**
     * Returns the grid tooltip.
     */
    public abstract String getTooltip();
    /**
     * Returns the grid columns.
     */    
	public abstract DynamicGridColumnCollection getColumns();
	/**
     * Returns the grid rows.
     */    
    public abstract DynamicGridRowCollection getRows();
    /**
     * Sets the grid text color.
     */    
    public abstract void setTextColor(Color value);
    /**
     * Returns the current grid text color.
     */    
    public abstract Color getTextColor();
    /**
     * Sets the grid back color.
     */   
    public abstract void setBackColor(Color value);
    /**
     * Returns the current grid back color.
     */        
    public abstract Color getBackColor();    
	/**
     * Specifies if the grid rows are selectable.
     */    
    public abstract void setSelectable(boolean value);
    /**
     * Specifies if the grid rows are unselectable.
     */    
    public abstract void setUnselectable(boolean value);
    /**
     * Returns true if the grid rows are selectable. 
     */    
    public abstract boolean isSelectable();
    /**
     * Returns true if the grid rows are unselectable. 
     */    
    public abstract boolean isUnselectable();
    /**
     * Clears the entire grid (all rows and columns).
     */    
    public abstract void clear();
    /**
     * Sets the default row options. For every row created those properties will apply.
     * @param value
     */
    public abstract void setDefaultRowOptions(DynamicGridRowOptions value);
    /**
     * Gets the default options for rows.
     * @return
     */
    public abstract DynamicGridRowOptions getDefaultRowOptions();
    /**
     * Resets the default row options.
     *
     */
    public abstract void resetDefaultRowOptions();   
    /**
     * Returns true if the grid is read only.
     * @return
     */
    public boolean isReadOnly()
    {
        return this.readOnly;
    }
    /**
     * Specifies if the grid is read only.
     * @param value
     */
    public void setReadOnly(boolean value)
    {
        if (this.form.getFormIsChangingMode() || (this.form.getMode().equals(FormMode.VIEW) && this.viewMode.equals(ControlState.UNKNOWN) || (this.form.getMode().equals(FormMode.EDIT) && this.editMode.equals(ControlState.UNKNOWN))))
            this.readOnly = value;
        else
            super.flagIlegalControlModeChange("ReadOnly", value);
    }
    /**
     * Specifies if the grid will display check boxes on rows. Only valid when the first column is tree.
     * @param value
     */
    public abstract void showCheckBoxes(boolean value);
    /**
     * Specifies the grid header height.
     * @param value
     */
    public abstract void setHeaderHeight(int value);    
    /**
     * Resets the scroll position.
     *
     */
    public abstract void resetScrollPosition();
    /**
     * Specifies the header maximum height
     *
     */
    public abstract void setHeaderMaxHeight(int value);
    /**
     * Specifies the header value
     *
     */
    public abstract void setHeaderValue(String value);
    /**
     * Specifies the footer maximum height
     *
     */
    public abstract void setFooterMaxHeight(int value);
    /**
     * Specifies the footer value
     *
     */
    public abstract void setFooterValue(String value);
    /**
     * Specifies if checking/unchecking of a row check box will cause a post back
     *
     */
    public abstract void setCheckBoxesAutoPostBack(boolean value);
    
    //PDS Stuff
    public abstract void setRowSelectionChangedEventRequirePdsAuthentication(boolean value);
    
	protected void free()
	{
		super.free();
		
        this.cellButtonClickedDelegate = null;
        this.cellTextSubmitedDelegate = null;
        this.columnHeaderClickedDelegate = null;
        this.rowSelectionChangedDelegate = null;
        this.rowSelectionClearedDelegate = null;
        this.cellValueChangedDelegate = null;
        this.rowCheckedDelegate = null;
        this.rowExpandedCollapsedDelegate = null;
	}
    
    public void setDynamicGridCellButtonClickedEvent(DynamicGridCellButtonClicked delegate)
    {
        this.cellButtonClickedDelegate = delegate;
    }
    public void setDynamicGridCellTextSubmitedEvent(DynamicGridCellTextSubmited delegate)
    {
        this.cellTextSubmitedDelegate = delegate;
    }
    public void setDynamicGridColumnHeaderClickedEvent(DynamicGridColumnHeaderClicked delegate)
    {
        this.columnHeaderClickedDelegate = delegate; 
    }
    public void setDynamicGridRowSelectionChangedEvent(DynamicGridRowSelectionChanged delegate)
    {
        this.rowSelectionChangedDelegate = delegate;
    }
    public void setDynamicGridRowSelectionClearedEvent(DynamicGridRowSelectionCleared delegate)
    {
        this.rowSelectionClearedDelegate = delegate;
    }
    public void setDynamicGridCellValueChangedEvent(DynamicGridCellValueChanged delegate)
    {
        this.cellValueChangedDelegate = delegate;
    }
    public void setDynamicGridRowCheckedEvent(DynamicGridRowChecked delegate)
    {
        this.rowCheckedDelegate = delegate;
    }
    public void setDynamicGridRowExpandedCollapsedEvent(DynamicGridRowExpandedCollapsed delegate)
    {
        this.rowExpandedCollapsedDelegate = delegate;
    } 
    
    protected int headerHeight = 24;
    protected boolean readOnly = true; 
    protected DynamicGridCellButtonClicked cellButtonClickedDelegate;
    protected DynamicGridRowSelectionCleared rowSelectionClearedDelegate;
    protected DynamicGridCellTextSubmited cellTextSubmitedDelegate;
    protected DynamicGridColumnHeaderClicked columnHeaderClickedDelegate;
    protected DynamicGridRowSelectionChanged rowSelectionChangedDelegate;
    protected DynamicGridCellValueChanged cellValueChangedDelegate;
    protected DynamicGridRowChecked rowCheckedDelegate;
    protected DynamicGridRowExpandedCollapsed rowExpandedCollapsedDelegate;
}
