package ims.framework.cn.data;

public class WavPlayerData implements IControlData
{
	private static final long serialVersionUID = 4765622008092971577L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public String getValue()
	{
		return this.value == null ? "" : this.value;
	}
	public void setEnabled(boolean value)
	{
		if(!this.enabledChanged)
			this.enabledChanged = this.enabled != value;
		
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		if(!this.visibleChanged)
			this.visibleChanged = this.visible != value;
		
		this.visible = value;
	}
	public void setValue(String value)
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

	private boolean enabled = true;
	private boolean visible = true;
	private String value = null;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean valueChanged = false;
}
