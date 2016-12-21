package ims.framework;

import java.lang.reflect.Constructor;

public class ImageReferencesFlyweightFactory // MUST BE PROTECTED !!!
{
	public Object create(Class cl) throws Exception
	{
		Constructor constructor = cl.getDeclaredConstructor((Class[])null);
		constructor.setAccessible(true);
		return constructor.newInstance((Object[])null);
	}

	// Singleton
	private ImageReferencesFlyweightFactory()
	{
	}
	public static ImageReferencesFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final ImageReferencesFlyweightFactory Singleton = new ImageReferencesFlyweightFactory();
	}
}