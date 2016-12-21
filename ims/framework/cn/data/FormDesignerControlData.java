package ims.framework.cn.data;

public class FormDesignerControlData implements IControlData
{
	private static final long serialVersionUID = 376608968040585607L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
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
	public void setEnabledChanged(boolean enabledChanged)
	{
		this.enabledChanged = enabledChanged;
	}
	public boolean isEnabledChanged()
	{
		return enabledChanged;
	}

	public void setVisibleChanged(boolean visibleChanged)
	{
		this.visibleChanged = visibleChanged;
	}
	public boolean isVisibleChanged()
	{
		return visibleChanged;		
	}
	public void setFormXml(String formXml)
	{
		this.formXml = formXml;
	}
	public String getFormXml()
	{
		return formXml;
	}
	
	private boolean enabled = true;
	private boolean visible = true;	
	private String formXml = null;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;	
}
