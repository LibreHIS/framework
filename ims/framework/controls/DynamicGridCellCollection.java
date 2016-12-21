package ims.framework.controls;

import ims.framework.enumerations.DynamicCellType;

/**
 * @author mmihalec
 */
public interface DynamicGridCellCollection 
{
    int size();

    // Unsafe to expose as some cells might be missing (not initialized). 
	// public DynamicGridCell get(int index);
    
    DynamicGridCell get(DynamicGridColumn column);
    int indexOf(DynamicGridCell cell);
	DynamicGridCell newCell(DynamicGridColumn column, DynamicCellType type);
	DynamicGridCell newCell(DynamicGridColumn column, DynamicCellType type, DynamicGridCellOptions options);
	boolean remove(DynamicGridCell cell);
	void clear();
}
