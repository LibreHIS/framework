package ims.framework.controls;

import java.io.Serializable;

public class RadioButtonBridgeFlyweightFactory  implements Serializable
{
	private static final long serialVersionUID = 1L;
	public RadioButtonBridge createRadioButtonBridge(Class cl, RadioButton value) throws Exception
	{
		RadioButtonBridge result = (RadioButtonBridge) cl.newInstance();
		result.setContext(value);
		return result;
	}

	// Singleton
	private RadioButtonBridgeFlyweightFactory()
	{
	}
	public static RadioButtonBridgeFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final RadioButtonBridgeFlyweightFactory Singleton = new RadioButtonBridgeFlyweightFactory();
	}
}