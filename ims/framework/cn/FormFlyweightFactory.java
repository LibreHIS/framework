package ims.framework.cn;

import java.io.Serializable;

public class FormFlyweightFactory implements Serializable
{
	private static final long serialVersionUID = 1L;
	public Form getForm()
	{
		return new Form();
	}

	// Singleton
	private FormFlyweightFactory()
	{
	}
	public static FormFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final FormFlyweightFactory Singleton = new FormFlyweightFactory();
	}
}
