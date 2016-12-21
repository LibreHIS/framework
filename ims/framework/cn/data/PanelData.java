package ims.framework.cn.data;

public class PanelData implements IControlData
{
	private static final long serialVersionUID = 4294560328995004543L;
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public String getValue()
	{
		return this.value;
	}
	public void setValue(String value)
	{
		if(value == null)
			value = "";
		
		if(!this.valueChanged)
		{
			if(this.value == null)
				this.valueChanged = true;
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
	private String value = null;
	
	private boolean visibleChanged = false;
	private boolean valueChanged = false;
}
