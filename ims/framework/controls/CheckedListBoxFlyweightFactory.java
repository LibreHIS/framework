package ims.framework.controls;

import java.io.Serializable;

public class CheckedListBoxFlyweightFactory implements Serializable
{
	private static final long serialVersionUID = 1L;

	public CheckedListBoxBridge createCheckedListBoxBridge(Class cl, CheckedListBox value) throws Exception
	{
		CheckedListBoxBridge result = (CheckedListBoxBridge) cl.newInstance();
		result.setContext(value);
		return result;
	}

	// Singleton
	private CheckedListBoxFlyweightFactory()
	{
	}
	public static CheckedListBoxFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final CheckedListBoxFlyweightFactory Singleton = new CheckedListBoxFlyweightFactory();
	}
}