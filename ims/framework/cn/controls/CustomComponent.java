package ims.framework.cn.controls;

import java.util.Iterator;

import ims.framework.Control;
import ims.framework.CustomEvent;
import ims.framework.UILogic;
import ims.framework.cn.Form;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.CustomComponentData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.IMenuEvent;
import ims.framework.cn.events.MessageBoxEvent;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.DialogResult;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class CustomComponent extends ims.framework.CustomComponent implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	public boolean wasChanged() 
	{
		saveFormMode();
		
		if(data.wasChanged())
			return true;
		
		Iterator i = form.getForm().getIterator();
		while(i.hasNext())
		{
			if(((Control) i.next()).wasChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{	
		// we don't mark child controls as rendered, the renderData method will do that
		data.markUnchanged();
	}
	public void setReadOnly(boolean value)
	{
		((Form)form.getForm()).setReadOnly(value);		
	}
	public void setEnabled(boolean value)
	{
		super.enabled = value;
		this.data.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		super.visible = value;
		this.data.setVisible(value);		
	}
	public UILogic getLogic()
	{
		return form.getUiLogic();
	}
	public void restore(IControlData data, boolean isNew) 
	{
		this.data = (CustomComponentData)data;
		if(this.data.isVisible() != null)
			super.visible = this.data.isVisible().booleanValue();
		if(this.data.isEnabled() != null)
			super.enabled = this.data.isEnabled().booleanValue();
		((Form)form.getForm()).restore(this.data.getFormData(), isNew);
		((Form)form.getForm()).setMode(this.data.getFormMode(), false);
	}
	public void fireFormOpenEvent() throws PresentationLogicException
	{
		((Form)form.getForm()).fireFormOpenEvent(new Object[0]);
	}
	public boolean fireFormDialogClosedEvent(int formName, DialogResult result, String callerIdentifier) throws PresentationLogicException
	{
		return ((Form)form.getForm()).fireFormDialogClosedEvent(formName, result, callerIdentifier);
	}
	public void fireMessageBoxClosedEvent(MessageBoxEvent event) throws PresentationLogicException
	{
		((Form)form.getForm()).fireMessageBoxClosedEvent(event);
	}
	public void fireCustomEventEvent(CustomEvent event) throws PresentationLogicException
	{
		((Form)form.getForm()).fireCustomEventEvent(event);
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException 
	{		
		if(((Form)form.getForm()).fireEvent(event))
		{
			if(valueChangedDelegate != null && ((Form)form.getForm()).wasChanged())
				valueChangedDelegate.handle();
			
			return true;
		}
		
		return false;
	}
	public boolean fireEvent(IMenuEvent event) throws PresentationLogicException 
	{
		return ((Form)form.getForm()).fireEvent(event);
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
		sb.append("\" scrollable=\"false");
		
		if(super.required)
		{
			sb.append("\" required=\"true");
		}
		
		sb.append("\" tabIndex=\"-1");
		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}		
		sb.append("\">");
		
		Iterator i = form.getForm().getIterator();
		while(i.hasNext())
		{
			((IVisualControl) i.next()).renderControl(sb);						
		}
		
		sb.append("</container>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException 
	{
		sb.append("<container id=\"a");
		sb.append(super.id);
		sb.append("\" visible=\"");		
		sb.append(super.visible ? "true" : "false");
		
		if(super.visible)
		{		
			if(!hasAnyParentDisabled())
			{
				sb.append("\" enabled=\"");		
				sb.append(super.enabled ? "true" : "false");
			}
			
			sb.append("\">");
			
			Iterator i = form.getForm().getIterator();
			while(i.hasNext())
			{
				Control control = (Control)i.next();
				if(control.wasChanged())
				{
					((IVisualControl) control).renderData(sb);
					control.markUnchanged();
				}
			}
			
			sb.append("</container>");
		}
		else
			sb.append("\" />");
		
		saveFormMode();
	}
	public Iterator getIterator()
	{
		return form.getForm().getIterator();
	}
	public void saveFormMode()
	{
		data.setFormMode(((Form)form.getForm()).getMode());
	}
	private CustomComponentData data;	
}
