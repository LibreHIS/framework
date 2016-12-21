package ims.framework.controls;

/**
 * @author mmihalec
 */
public interface DynamicGridColumnCollection 
{
    int size();
	DynamicGridColumn get(int index);
    DynamicGridColumn getByIdentifier(Object value);    
    int indexOf(DynamicGridColumn column);
	DynamicGridColumn newColumn(String caption);
    DynamicGridColumn newColumn(String caption, boolean readOnly);
    DynamicGridColumn newColumn(String caption, Object identifier);
    DynamicGridColumn newColumn(String caption, Object identifier, boolean readOnly);
    DynamicGridColumn newColumnAt(int index, String caption);
    DynamicGridColumn newColumnAt(int index, String caption, boolean readOnly);
    DynamicGridColumn newColumnAt(int index, String caption, Object identifier);
    DynamicGridColumn newColumnAt(int index, String caption, Object identifier, boolean readOnly);
    boolean remove(DynamicGridColumn column);
	void clear();	
}
