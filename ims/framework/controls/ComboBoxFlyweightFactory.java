package ims.framework.controls;

import java.io.Serializable;

public class ComboBoxFlyweightFactory implements Serializable 	
{
	private static final long serialVersionUID = 1L;
	
	public ComboBoxBridge createComboBoxBridge(Class cl, ComboBox value) throws Exception
	{
		ComboBoxBridge result = (ComboBoxBridge) cl.newInstance();
		result.setContext(value);
		return result;
	}

	// Singleton
	private ComboBoxFlyweightFactory()
	{
	}
	public static ComboBoxFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final ComboBoxFlyweightFactory Singleton = new ComboBoxFlyweightFactory();
	}
}