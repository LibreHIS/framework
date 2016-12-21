package ims.framework.controls;

/**
 * @author mmihalec
 */
public interface DynamicGridRowCollection
{    
    /** 
     * Creates a new row. By default the row will be selectable and editable but it will not be selected.
     * @return New row
     */
	public DynamicGridRow newRow();	
	/** 
     * Creates a new row after an existing row. By default the row will be selectable and editable but it will not be selected.
     * @return New row
     */
	public DynamicGridRow newRowAfter(DynamicGridRow row);
	/** 
     * Creates a new row before an existing row. By default the row will be selectable and editable but it will not be selected.
     * @return New row
     */
	public DynamicGridRow newRowBefore(DynamicGridRow row);
    /**
     * Creates a new row. By default the row will be selectable and editable and it will be selected if autoSelect is true.
     * @param autoSelect
     * @return
     */
    public DynamicGridRow newRow(boolean autoSelect);
    /**
     * Creates a new row after an existing row. By default the row will be selectable and editable and it will be selected if autoSelect is true.
     * @param autoSelect
     * @return
     */
    public DynamicGridRow newRowAfter(DynamicGridRow row, boolean autoSelect);
    /**
     * Creates a new row before an existing row. By default the row will be selectable and editable and it will be selected if autoSelect is true.
     * @param autoSelect
     * @return
     */
    public DynamicGridRow newRowBefore(DynamicGridRow row, boolean autoSelect);
	/**
	 * @return The number of rows.
	 */
	public int size();
    /**
     * Returns the index of a specific row. If the row was not found it returns -1.
     * @param row
     * @return
     */
    public int indexOf(DynamicGridRow row);
	/**
	 * @param index
	 * @return The row at the specified index. If the index is invalid, the framework will throw an exception.
	 */    
	public DynamicGridRow get(int index);
	/**
	 * Deletes a row.
	 * @param index
	 */
	public void remove(DynamicGridRow row);
	/**
	 * Deletes all rows.
	 */
	public void clear();
    /**
     * Gets the parent row.
     * @return
     */
    public DynamicGridRow getParent();
    /**
     * Expands all rows
     */
    public void expandAll();
    /**
     * Collapses all rows
     */
    public void collapseAll();
}
