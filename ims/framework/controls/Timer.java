package ims.framework.controls;

import ims.base.interfaces.IModifiable;

import java.io.Serializable;

public abstract class Timer implements Serializable, IModifiable
{
	private static final long serialVersionUID = 1L;
	
	public abstract int getID();
	public abstract int getInterval();
	public abstract void setInterval(int value);
	public abstract boolean isEnabled();
	public abstract void setEnabled(boolean value);
	
	public final boolean equals(Object obj)
	{
		if(obj instanceof Timer)
			return ((Timer)obj).getID() == getID();
		return false;
	}
	public final int getHashCode()
	{
		return getID();
	}
	
	public abstract void render(StringBuffer sb);
}
