package ims.framework.utils;

public final class RegisteredSpellCheckers
{
	private int id;
	private String name;	
	
	public static RegisteredSpellCheckers NONE = new RegisteredSpellCheckers(0, "None");
	public static RegisteredSpellCheckers JAZZY = new RegisteredSpellCheckers(1, "Jazzy");
	
	private RegisteredSpellCheckers(int id, String name)
	{
		this.id = id;
		this.name = name;				
	}
	
	public static RegisteredSpellCheckers[] getAll()
	{
		return getAllInternal();
	}
	public static RegisteredSpellCheckers parse(int id)
	{
		for(int x = 0; x < getAll().length; x++)
		{
			if(getAll()[x].id == id)
				return getAll()[x];
		}
		
		return null;
	}	
	
	public int getID()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}	
	public String toString()
	{
		return name;
	}
	public boolean compareTo(Object obj)
	{
		if(obj instanceof RegisteredSpellCheckers)
			return ((RegisteredSpellCheckers)obj).id == id;
		return false;
	}
	private static RegisteredSpellCheckers[] getAllInternal()
	{
		return new RegisteredSpellCheckers[] { JAZZY }; 
	}
	
}
