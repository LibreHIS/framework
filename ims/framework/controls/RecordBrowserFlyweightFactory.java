package ims.framework.controls;

import java.io.Serializable;

public class RecordBrowserFlyweightFactory  implements Serializable
{
	private static final long serialVersionUID = 1L;
	public RecordBrowserBridge createRecordBrowserBridge(Class cl, RecordBrowser value) throws Exception
	{
		RecordBrowserBridge result = (RecordBrowserBridge) cl.newInstance();
		result.setContext(value);
		return result;
	}

	// Singleton
	private RecordBrowserFlyweightFactory()
	{
	}
	public static RecordBrowserFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final RecordBrowserFlyweightFactory Singleton = new RecordBrowserFlyweightFactory();
	}
}