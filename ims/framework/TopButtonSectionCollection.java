package ims.framework;

import ims.base.interfaces.IModifiable;

import java.io.Serializable;

public final class TopButtonSectionCollection implements IModifiable, Serializable 
{
	private static final long serialVersionUID = 1L;	
	private boolean wasChanged = false;
	
	private GenericChangeableCollection<TopButtonSection> items = new GenericChangeableCollection<TopButtonSection>();
	
	public TopButtonSectionCollection()
	{
	}
	
	public boolean add(TopButtonSection item)
	{
		if(item == null)
			throw new RuntimeException("Invalid top button section");
		
		if(contains(item))
			return false;
		
		items.add(item);
		wasChanged = true;
		return true;
	}
	public boolean contains(TopButtonSection item)	
	{
		return items.contains(item);
	}
	public void remove(TopButtonSection item)
	{
		if(!wasChanged)
			wasChanged = contains(item);
		
		items.remove(item);
	}
	public void clear()
	{
		if(!wasChanged)
			wasChanged = items.size() > 0;
		items.clear();
	}
	public int size()
	{
		return items.size();
	}
	public TopButtonSection get(int index)
	{
		return items.get(index);		
	}

	public void markUnchanged()
	{
		wasChanged = false;
		items.markUnchanged();
	}
	public boolean wasChanged()
	{
		return wasChanged || items.wasChanged();
	}
}
