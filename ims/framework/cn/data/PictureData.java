package ims.framework.cn.data;

import ims.framework.utils.Image;

public class PictureData implements IControlData
{
	private static final long serialVersionUID = 8752510026132733113L;
	public boolean isVisible()
	{
		return this.visible;
	}
	public Image getValue()
	{
		return this.value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public void setValue(Image value)
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
	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;
	}
	public void setValueChanged(boolean valueChanged)
	{
		this.valueChanged = valueChanged;
	}
	public boolean isValueChanged()
	{
		return valueChanged;
	}

	private boolean visible = true;
	private Image value = null;
	
	private boolean visibleChanged = false;
	private boolean valueChanged = false;
}
