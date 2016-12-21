package ims.framework.controls;

import java.io.Serializable;

public class GridRowCollectionBridge implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected GridRowCollectionBridge(GridRowCollection collection)
	{
		this.collection = collection;
	} 
	
	public int size()
	{
		return this.collection.size();
	}
	public void remove(int index)
	{
		this.collection.remove(index);
	}
	public void clear()
	{
		this.collection.clear();
	}
	
	protected GridRowCollection collection;
}
