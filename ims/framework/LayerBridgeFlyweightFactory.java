package ims.framework;

public class LayerBridgeFlyweightFactory // MUST BE PROTECTED !!!
{
	public LayerBridge createLayerBridge(Class cl, Container container, UIFactory uiFactory) throws Exception
	{
		LayerBridge result = (LayerBridge) cl.newInstance();
		result.setContext(container, uiFactory);
		return result;
	}

	// Singleton
	private LayerBridgeFlyweightFactory()
	{
	}
	public static LayerBridgeFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final LayerBridgeFlyweightFactory Singleton = new LayerBridgeFlyweightFactory();
	}
}