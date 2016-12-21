package ims.framework.cn.controls;

import ims.framework.cn.IVisualControl;
import ims.framework.cn.data.FormDesignerControlData;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class FormDesignerControl extends ims.framework.controls.FormDesignerControl implements IVisualControl
{
	private static final long serialVersionUID = 1L;
	
	protected void free()
	{
		super.free();		
		this.data = null;		
	}	
	public void restore(IControlData data, boolean isNew)
	{
		this.data = (FormDesignerControlData)data;
		
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();	
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		return false;
	}
	public void renderControl(StringBuffer sb) throws ConfigurationException
	{
		
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		
	}
	public boolean wasChanged() 
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(data.isEnabledChanged())
				return true;		
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	
	private FormDesignerControlData data;
	
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	@Override
	public void setFormXml(String formXml)
	{
		data.setFormXml(formXml);
	}
	@Override
	public String getFormXml()
	{
		return data.getFormXml();
	}	
}
