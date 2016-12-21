package ims.framework.cn.data;


public class FilePickerData implements IControlData
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
	public String getValue()
	{
		return this.value;
	}
	public void setEnabled(boolean value)
	{
		this.enabled = value;
	}
	public void setVisible(boolean value)
	{
		this.visible = value;
	}
	public void setValue(String value)
	{
		if (value != null && value.equals(""))
		{
			this.value = null;
		}
		else
		{
			this.value = value;
		}
	}
	public String getFileType()
	{
		return this.fileType;
	}
	public void setFileType(String fileType)
	{
		if (fileType != null && fileType.equals(""))
		{
			this.fileType = null;
		}
		else
		{
			this.fileType = fileType;
		}
	}	
	private boolean enabled = true;
	private boolean visible = true;
	private String value = null;
	private String fileType = null;
}
