package ims.framework.controls;

import java.io.Serializable;

abstract public class MarkerControlBitmap implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	abstract protected String getID();
	
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (!(obj instanceof MarkerControlBitmap))
			return false;
		return this.getID().equals(((MarkerControlBitmap)obj).getID());
	}
	public int hashCode()
	{
		return super.hashCode();
	}
}
