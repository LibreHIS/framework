package ims.framework;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;
import ims.framework.exceptions.ConfigurationException;

public abstract class Menu implements IModifiable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public abstract int getID();	
	public abstract void add(MenuItem item);	
	public abstract int count();	
	public abstract MenuItem get(int index);
	public abstract void clear();
	public abstract void free();	
	public abstract void render(StringBuffer sb, boolean formIsReadOnly) throws ConfigurationException;
}
