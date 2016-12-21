package ims.framework;

import java.lang.reflect.Constructor;

public class FormReferencesFlyweightFactory // MUST BE PROTECTED !!!
{
	public Object create(Class cl) throws Exception
	{
		Constructor constructor = cl.getDeclaredConstructor((Class[])null);
		constructor.setAccessible(true);
		return constructor.newInstance((Object[])null);
	}

	// Singleton
	private FormReferencesFlyweightFactory()
	{
	}
	public static FormReferencesFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final FormReferencesFlyweightFactory Singleton = new FormReferencesFlyweightFactory();
	}
}