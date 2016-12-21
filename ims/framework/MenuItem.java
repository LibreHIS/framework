package ims.framework;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;

/**
 * @author mmihalec
 */
public abstract class MenuItem extends ReadOnlyMenuItem implements IModifiable, Serializable
{		
	private static final long serialVersionUID = 1L;
	
	public abstract void setText(String value);     
}
