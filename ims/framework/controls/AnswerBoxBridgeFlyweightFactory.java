package ims.framework.controls;

import java.io.Serializable;

public class AnswerBoxBridgeFlyweightFactory implements Serializable
{
	private static final long serialVersionUID = 1L;

	public AnswerBoxBridge createAnswerBoxBridge(Class cl, AnswerBox value) throws Exception
	{
		AnswerBoxBridge result = (AnswerBoxBridge) cl.newInstance();
		result.setContext(value);
		return result;
	}
	// Singleton
	private AnswerBoxBridgeFlyweightFactory()
	{
	}
	public static AnswerBoxBridgeFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final AnswerBoxBridgeFlyweightFactory Singleton = new AnswerBoxBridgeFlyweightFactory();
	}
}
