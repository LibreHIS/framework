package ims.framework;

import java.lang.reflect.Constructor;

public class ContextBridgeFlyweightFactory // MUST BE PROTECTED !!!
{
	public ContextBridge create(Class cl, Context ctx, boolean isLocal) throws Exception
	{
		ContextBridge result;
		if (isLocal)
			result = (ContextBridge) cl.newInstance();
		else
		{
			Constructor constructor = cl.getDeclaredConstructor((Class[])null);
			constructor.setAccessible(true);
			result = (ContextBridge)constructor.newInstance((Object[])null);
		}

		result.setContext(ctx);
		return result;
	}
	
	// Singleton
	private ContextBridgeFlyweightFactory()
	{
	}
	public static ContextBridgeFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final ContextBridgeFlyweightFactory Singleton = new ContextBridgeFlyweightFactory();
	}
}