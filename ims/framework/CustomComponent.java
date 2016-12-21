package ims.framework;

import ims.framework.delegates.ControlFocused;
import ims.framework.delegates.DefaultControl;
import ims.framework.delegates.FormModeChanged;
import ims.framework.delegates.ValueChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.FormMode;
import ims.framework.exceptions.PresentationLogicException;

import java.io.Serializable;
import java.util.Iterator;

public abstract class CustomComponent extends Control implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	public abstract void saveFormMode();
	
	public final void setContext(FormUiLogic form, Control parentControl, int id, int x, int y, int width, int height, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, int tabIndex, boolean required)
	{
		this.form = form;
		this.form.getForm().setMode(FormMode.VIEW, false);
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);
		this.tabIndex = tabIndex;
		this.required = required;
		
		this.form.getForm().setFormValueChangedEvent(new ValueChanged()
		{
			private static final long serialVersionUID = 1L;
			
			public void handle() throws PresentationLogicException
			{
				fireValueChangedEvent();
			}
		});
		
		this.form.getForm().setControlFocusedEvent(new ControlFocused()
		{
			private static final long serialVersionUID = 1L;

			public void handle(Control control) 
			{
				CustomComponent.this.controlFocusedDelegate.handle(control);
			}
		});		
		
		this.form.getForm().setDefaultControlEvent(new DefaultControl()
		{
			private static final long serialVersionUID = 1L;

			public void handle(Control control) 
			{
				CustomComponent.this.defaultControlDelegate.handle(control);
			}
		});
		
		this.form.getForm().setFormModeChangedToComponentEvent(new FormModeChanged()
		{
			private static final long serialVersionUID = 1L;

			public void handle() 
			{
				saveFormMode();
			}	
		});
	}		
	public void setValueChangedEvent(ValueChanged delegate)
	{
		this.valueChangedDelegate = delegate;
	}
	protected void free()
	{		
		super.free();
		
		form = null;
		valueChangedDelegate = null;
	}
	public void fireValueChangedEvent() throws PresentationLogicException
	{
		if(valueChangedDelegate != null)
			valueChangedDelegate.handle();		
	}
	public abstract Iterator getIterator();	
	public abstract void setReadOnly(boolean value);
	
	protected FormUiLogic form;
	protected int tabIndex;
	protected boolean required = false;
	protected ValueChanged valueChangedDelegate = null;	
}
