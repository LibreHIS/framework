package ims.framework;

public abstract class FormConfigFlags
{
	private ConfigFlag configFlag = null;
	
	public FormConfigFlags(ConfigFlag configFlag)
	{
		this.configFlag = configFlag;
	}
	
	protected Object get(String key)
	{
		return configFlag.get(key);
	}
}
