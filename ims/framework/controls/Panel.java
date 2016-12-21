package ims.framework.controls;

import ims.framework.Control;

public abstract class Panel extends Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public String getValue();
    abstract public void setValue(String value); 
}
