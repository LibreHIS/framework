package ims.framework;

import java.io.Serializable;
import java.util.ArrayList;
import ims.base.interfaces.IModifiable;

public final class GenericChangeableCollection<Type extends IModifiable> implements IModifiable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	private boolean wasChanged = true;
	private ArrayList<Type> list = new ArrayList<Type>();
	
	public boolean add(Type item)
	{
		if(contains(item))
			return false;
				
		list.add(item);
		wasChanged = true;
		
		return true;
	}
	public int size()
	{
		return list.size();
	}
	public int indexOf(Type item)
	{
		return list.indexOf(item);
	}
	public boolean contains(Type item)
	{
		return list.contains(item);
	}
	public void remove(Type item)
	{
		if(contains(item))
		{			
			list.remove(item);
			wasChanged = true;
		}
	}	
	public void clear()
	{
		if(!wasChanged)
			wasChanged = list.size() > 0;
			
		list.clear();
	}
	public Type get(int index)
	{
		return list.get(index);
	}
	public boolean wasChanged() 
	{
		if(wasChanged)
			return true;
		
		for(int x = 0; x < list.size(); x++)
		{
			if(list.get(x).wasChanged())
				return true;					
		}
		
		return false;
	}
	public void markUnchanged() 
	{
		wasChanged = false;
		
		for(int x = 0; x < list.size(); x++)
		{
			list.get(x).markUnchanged();
		}
	}	
}
