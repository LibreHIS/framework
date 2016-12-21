package ims.framework.cn.data;


import ims.base.interfaces.IModifiable;

public abstract class ChangeableData implements IModifiable
{
	protected boolean dataWasChanged = true;

	public boolean wasChanged() 
	{
		return this.dataWasChanged;
	}
	public void markUnchanged() 
	{
		this.dataWasChanged = false;
	}
}
