package ims.framework.controls;

import java.io.Serializable;

public class GridFlyweightFactory implements Serializable
{
	private static final long serialVersionUID = 1L;
	public GridBridge createGridBridge(Class cl, Grid grid) throws Exception
	{
		GridBridge result = (GridBridge) cl.newInstance();
		result.setContext(grid);
		return result;
	}

	// Singleton
	private GridFlyweightFactory()
	{
	}
	public static GridFlyweightFactory getInstance()
	{
		return SingletonHolder.Singleton;
	}
	private static final class SingletonHolder
	{
		static final GridFlyweightFactory Singleton = new GridFlyweightFactory();
	}
}