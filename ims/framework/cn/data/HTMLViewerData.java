package ims.framework.cn.data;

import ims.framework.utils.SizeInfo;
import ims.framework.utils.TextileString;

public class HTMLViewerData implements IControlData
{
	private static final long serialVersionUID = -504493124742016510L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isEmbeddedObject()
	{
		return this.embeddedObject;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public String getHTML()
	{
		return this.value;
	}
	public void setEnabled(boolean value)
	{
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setEmbeddedObject(boolean value)
	{
		if(!this.embeddedObjectChanged)
			this.embeddedObjectChanged = this.embeddedObject != value;
		
		this.embeddedObject = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public void setHTML(String value)
	{
		if(!this.valueChanged)
		{
			if(this.value == null)
				this.valueChanged = value != null;
			else 
				this.valueChanged = !this.value.equals(value);
		}
			        
		this.value = value;
	}
	public void setHTML(TextileString value)
	{
		if(!this.valueChanged)
		{
			if(this.value == null)
				this.valueChanged = value != null;
			else 
				this.valueChanged = !this.value.equals(value.toString());
		}
			    	       
		this.value = value.toString();
	}	
	public void setCurrentSize(SizeInfo value)
	{
		currentSize = value;
	}
	public SizeInfo getCurrentSize()
	{
		return currentSize;
	}    
	public void setEnabledUnchanged()
	{
		this.enabledChanged = false;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}
	public void setVisibleUnchanged()
	{
		this.visibleChanged = false;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setValueUnchanged()
	{
		this.valueChanged = false;
	}
	public boolean isValueChanged()
	{
		return valueChanged;
	}
	public void setEmbeddedObjectUnchanged()
	{
		this.embeddedObject = false;
	}
	public boolean isEmbeddedObjectChanged()
	{
		return embeddedObject;
	}

	private boolean enabled = true;
	private boolean embeddedObject = false;
	private boolean visible = true;
	private String value = "";	
	private SizeInfo currentSize;
	
	private boolean enabledChanged = false;
	private boolean embeddedObjectChanged = false;	
	private boolean visibleChanged = false;
	private boolean valueChanged = false;	
}
