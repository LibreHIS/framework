package ims.framework.cn.data;


public class FileUploaderData implements IControlData
{
	private static final long serialVersionUID = 6890820965484607744L;
	public boolean isEnabled()
	{
		return this.enabled;
	}
	public boolean isVisible()
	{
		return this.visible;
	}
	public boolean isOverwriteOnUpload()
	{
		return this.overwriteOnUpload;
	}
	public boolean isEnabledOverwriteOnUpload()
	{
		return this.enabledOverwriteOnUpload;
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
	public void setOverwriteOnUpload(boolean value)
	{
		if(!this.overwriteOnUploadChanged)
			this.overwriteOnUploadChanged = this.overwriteOnUpload != value;
		
		this.overwriteOnUpload = value;
	}
	public void setEnabledOverwriteOnUpload(boolean value)
	{
		if(!this.enabledOverwriteOnUploadChanged)
			this.enabledOverwriteOnUploadChanged = this.enabledOverwriteOnUpload != value;
		
		this.enabledOverwriteOnUpload = value;
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
	public void setOverwriteOnUploadUnchanged()
	{
		this.overwriteOnUpload = false;
	}
	public boolean isOverwriteOnUploadChanged()
	{
		return overwriteOnUpload;
	}
	public void setEnabledOverwriteOnUploadUnchanged()
	{
		this.enabledOverwriteOnUpload = true;
	}
	public boolean isEnabledOverwriteOnUploadChanged()
	{
		return true;
	}
	private boolean enabled = true;
	private boolean visible = true;
	private boolean overwriteOnUpload = false;
	private boolean enabledOverwriteOnUpload = true;
	
	private boolean enabledChanged = false;
	private boolean visibleChanged = false;
	private boolean overwriteOnUploadChanged = false;
	private boolean enabledOverwriteOnUploadChanged = true;
}
