package ims.framework.controls;

import ims.framework.Control;

abstract public class SearchableComboBox extends Control
{
	private static final long serialVersionUID = 1L;
	
	protected void free()
	{
		super.free();
	}    

	abstract public void add(Object value, String text);
	abstract public Object getValue();
	
	abstract public void clearOptions();
	abstract public void setCriteria(String value);
}