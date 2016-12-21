package ims.framework;

import java.io.Serializable;

import ims.framework.interfaces.ILocation;

public class Location implements ILocation, Serializable
{
	private static final long serialVersionUID = 1L;
	
	int id;
	String name;
	public Location(ILocation location)
	{
		this(location.getID(), location.getName());
	}
	public Location(int id, String name)
	{
		if(name == null)
			throw new RuntimeException("Invalid location name");
		
		this.id = id;
		this.name = name;
	}
	public int getID() 
	{			
		return id;
	}
	public String getName() 
	{
		return name;
	}
	public boolean equals(Object obj)
	{
		if (obj instanceof ILocation) 
		{
			ILocation compare = (ILocation) obj;
			return id == compare.getID() && name.equals(compare.getName());
		}
		
		return false;
	}
}