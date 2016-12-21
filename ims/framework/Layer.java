package ims.framework;

import java.io.Serializable;
import java.util.ArrayList;

abstract public class Layer implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public void setContext(AbstractBridge container, UIFactory factory)
	{
		this.container = container;
		this.factory = factory;
	}
	public void free()
	{
		this.container = null;
		this.factory = null;
		this.layers.clear();
	}
	
	protected void addContainer(Container container, LayerBridge layer)
	{
		this.container.addControl(container);
		this.layers.add(layer);
	}
	protected AbstractBridge container;
	protected UIFactory factory;
	protected ArrayList<LayerBridge> layers = new ArrayList<LayerBridge>();
}
