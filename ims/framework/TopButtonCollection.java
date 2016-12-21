package ims.framework;

import ims.base.interfaces.IModifiable;
import ims.framework.interfaces.ITopButton;

import java.io.Serializable;

public final class TopButtonCollection implements IModifiable, Serializable 
{
	private static final long serialVersionUID = 1L;	
	private boolean wasChanged = false;
	
	private GenericChangeableCollection<TopButton> items = new GenericChangeableCollection<TopButton>();
	
	public TopButtonCollection()
	{
	}
	
	public boolean add(TopButton item)
	{
		if(item == null)
			throw new RuntimeException("Invalid top button");
		
		if(contains(item))
			return false;
		
		items.add(item);
		wasChanged = true;
		return true;
	}
	public boolean contains(TopButton item)	
	{
		return items.contains(item);
	}
	public void remove(TopButton item)
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
	public TopButton get(int index)
	{
		return items.get(index);		
	}
	public ITopButton[] getItems()
	{
		ITopButton[] buttons = new ITopButton[items.size()];
		
		for(int x = 0; x < items.size(); x++)
		{
			buttons[x] = items.get(x);
		}
		
		return buttons;
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
