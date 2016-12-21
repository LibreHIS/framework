package ims.framework.controls;

import ims.framework.Control;

public abstract class BarControl extends Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public void addResource(BarControlResource resource);
	abstract public void addResource(int total, int completed, String message);
	abstract public void clear();
	
	protected void free()
	{
		super.free();
	}
}
