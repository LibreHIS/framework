package ims.framework.controls;

import java.util.ArrayList;

import ims.framework.Control;
import ims.framework.delegates.AnswerBoxValueSet;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;

abstract public class AnswerBox extends Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public void addOption(AnswerBoxOption option);

	abstract public AnswerBoxOption getValue();
	abstract public ArrayList getValues();
	abstract public void setValue(AnswerBoxOption value);
	abstract public void clear();
	abstract public void setRequired(boolean value);
	abstract public boolean isRequired();
		
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}
	public void setValueSetEvent(AnswerBoxValueSet delegate)
	{
	    this.valueSetDelegate = delegate;
	}
	protected final void setContext(Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, String text, boolean canBeEmpty, int imgHeight, boolean required)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.text = text;
		this.canBeEmpty = canBeEmpty;
		this.imgHeight = imgHeight;
		this.required = required;
	}
	protected void free()
	{
		super.free();
		
		this.text = null;
		this.valueChangedDelegate = null;
		this.valueSetDelegate = null;
	}    
	protected String text;
	protected ValueChanged valueChangedDelegate = null;
	protected AnswerBoxValueSet valueSetDelegate = null;
	protected boolean canBeEmpty;
	protected int imgHeight;
	protected boolean required = false;
}
