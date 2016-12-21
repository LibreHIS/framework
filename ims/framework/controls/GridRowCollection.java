package ims.framework.controls;

public interface GridRowCollection 
{ 
	public int size();
	public GridRow get(int index);
	
	public GridRow newRow();
	public GridRow newRow(boolean autoSelect);
	public GridRow newRowAt(int index);
	public GridRow newRowAt(int index, boolean autoSelect);
	public void remove(int index);
	public void clear();
		
}