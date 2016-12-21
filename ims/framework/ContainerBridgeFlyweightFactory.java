package ims.framework;

public class ContainerBridgeFlyweightFactory // MUST BE PROTECTED !!!
{
	public ContainerBridge createContainerBridge(Class cl, Container container, UIFactory uiFactory) throws Exception
	{
		ContainerBridge result = (ContainerBridge) cl.newInstance();
		result.setContext(container, uiFactory);
		return result;
	}
	
	// Singleton
	private ContainerBridgeFlyweightFactory()
	{
	}
	public static ContainerBridgeFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final ContainerBridgeFlyweightFactory Singleton = new ContainerBridgeFlyweightFactory();
	}
}