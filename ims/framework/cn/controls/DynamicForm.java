package ims.framework.cn.controls;

import java.lang.reflect.InvocationTargetException;

import ims.framework.Control;
import ims.framework.Form;
import ims.framework.FormLoader;
import ims.framework.FormName;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.DynamicFormData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.IMenuEvent;
import ims.framework.delegates.DynamicFormModeChanged;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.enumerations.DialogResult;
import ims.framework.enumerations.FormMode;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;
import ims.framework.utils.SizeInfo;

public class DynamicForm extends ims.framework.controls.DynamicForm implements IVisualControl
{ 
	private int tabIndex;
	private DynamicFormData data;
	private ims.framework.cn.FormLoader loader; 
	private Form parentForm;
	private static final long serialVersionUID = 1L;
	
	public void setReadOnly(boolean value)
	{
		data.setReadOnly(value);
	}
	public boolean isReadOnly()
	{
		return data.isReadOnly();
	}
	public void setContext(FormLoader loader, Form parentForm, Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor);		
		this.tabIndex = tabIndex;	
		this.loader = (ims.framework.cn.FormLoader)loader;
		this.parentForm = parentForm;
	}
	public boolean fireEvent(IMenuEvent event) throws PresentationLogicException
	{
		return this.data.fireEvent(event);
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{
		if(data.getForm() != null)
		{
			return ((ims.framework.cn.Form)data.getForm().getForm()).fireEvent(event);
		}
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<container id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"");
		sb.append(tabIndex);
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}	
		sb.append("\" />");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<container id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{		
			sb.append("\" visible=\"");		
			sb.append(super.visible ? "true" : "false");
			data.setVisibleChanged(false);
		}
		sb.append("\" >");
		
		if(data.getForm() != null)
		{			
			if(data.getFormWasChanged())
			{
				((ims.framework.cn.Form)data.getForm().getForm()).renderForm(sb, 0, "");
				data.markFormAsUnchanged();
			}			
			if(((ims.framework.cn.Form)data.getForm().getForm()).wasChanged())
			{
				((ims.framework.cn.Form)data.getForm().getForm()).renderData(sb);
			}
		}		
		else
		{
			sb.append("<form/>");
		}
		
		sb.append("</container>");
	}
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (DynamicFormData)data;
				
		if(!isNew && this.data.getFormName() != null)
			open(this.data.getFormName(), this.data.getLastParams(), false);
		
		/*if(this.data.isVisible() != null)
			super.visible = this.data.isVisible().booleanValue();
		if(this.data.isEnabled() != null)
			super.enabled = this.data.isEnabled().booleanValue();*/
	}
	public void markUnchanged()
	{				
		
	}
	public boolean wasChanged()
	{
		return true;
		/*
		if(data.getForm() != null)
		{
			return ((ims.framework.cn.Form)data.getForm().getForm()).wasChanged();
		}
		
		return false;
		*/
	}
	@Override
	public void open(FormName form)
	{
		open(form, null, true);		
	}
	@Override	
	public void open(FormName form, Object[] params)
	{
		if(form == null)
		{
			clear();
		}
		else
		{
			open(form, params, true);
		}
	}
	@Override
	public void clear()
	{
		this.data.clear();
	}
	@Override
	public FormName getPreviousNonDialogFormName()
	{
		return this.data.getPreviousNonDialogFormName();
	}
	public void open(FormName form, Object[] params, boolean isNew)
	{				
		try
		{			
			ims.framework.cn.Form formInstance = this.data.open(loader, form, params, this.id * 300, new SizeInfo(width, height), this, -1, isNew, parentForm);
			if(formInstance != null)
			{
				formInstance.setFormModeChangedToDynamicFormEvent(new DynamicFormModeChanged()
				{
					private static final long serialVersionUID = 1L;					
					public void handle(FormMode formMode)
					{
						if(formModeChangeDelegate != null)
						{
							formModeChangeDelegate.handle(formMode);
						}
					}					
				});
				
				if(isNew && formModeChangeDelegate != null)
				{
					formModeChangeDelegate.handle(formInstance.getMode());
				}
			}			
		}
		catch (Exception e)
		{
			Throwable cause = e;
			
			if(e instanceof InvocationTargetException)
				cause = e.getCause();
			
			this.data.clear();
			throw new RuntimeException("Unable to load the dynamic form. The error was: \r\n\r\n" + cause.toString() + "\r\n\r\nForm ID: " + String.valueOf(form.getID()), e);			
		}
	}	
	public Control findControl(int controlID) 
	{
		return this.data.findControl(controlID);
	}
	public boolean fireFormDialogClosedEvent(int formName, DialogResult result, String callerIdentifier) throws PresentationLogicException 
	{
		return this.data.fireFormDialogClosedEvent(formName, result, callerIdentifier);
	}	
	public FormMode getOpenedFormMode()
	{
		return data.getOpendFormMode();
	}
	public FormName getOpenedForm()
	{
		return data.getFormName();
	}
	@Override
	public void saveData() throws Exception 
	{
		data.saveData();
	}
	@Override
	public boolean supportsDataOperations() 
	{
		return data.supportsDataOperations();
	}
	@Override
	public String[] validateData() 
	{
		return data.validateData();
	}
}
