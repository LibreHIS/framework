package ims.framework;

public class LayerFlyweightFactory
{
	public Layer createLayer(Class cl, AbstractBridge form, UIFactory uiFactory) throws Exception
	{
		Layer result = (Layer) cl.newInstance();
		result.setContext(form, uiFactory);
		return result;
	}

	// Singleton
	private LayerFlyweightFactory()
	{
	}
	public static LayerFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final LayerFlyweightFactory Singleton = new LayerFlyweightFactory();
	}

}
