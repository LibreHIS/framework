package ims.framework.enumerations;

import java.io.Serializable;

public class DynamicCellDecoratorMode implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final DynamicCellDecoratorMode DEFAULT = new DynamicCellDecoratorMode(1);
	public static final DynamicCellDecoratorMode NEVER = new DynamicCellDecoratorMode(2);
	public static final DynamicCellDecoratorMode ALWAYS = new DynamicCellDecoratorMode(3);
	
	private DynamicCellDecoratorMode(int id)
	{
		this.id = id;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public String toString()
	{
		if(this.id == NEVER.getID())
			return "never";
		if(this.id == ALWAYS.getID())
			return "always";
		return "default";
	}
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof DynamicCellDecoratorMode)
			return this.id == ((DynamicCellDecoratorMode)obj).id;
		return false;
	}
	public int hashCode()
    {
    	return super.hashCode();
    }
	
	private int id;
}
