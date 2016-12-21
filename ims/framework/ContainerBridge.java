package ims.framework;

public abstract class ContainerBridge extends AbstractBridge
{
	private static final long serialVersionUID = 1L;
	
	void setContext(Container container, UIFactory factory)
	{
		this.container = container;
		this.factory = factory;
	}
	protected void free()
	{
		this.container = null;
		this.factory = null;
		super.freeCollections();
	}

	public void setScrollToTop(boolean value)
    {
        this.container.setScrollToTop(value);
    }
	public void setEnabled(boolean value)
	{
		this.container.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		this.container.setVisible(value);
	}
	
	protected void addControl(Control control)
	{
		this.container.addControl(control);
	}
	protected Object getControl(int index)
	{
		return this.container.controls.get(index);
	}
	
	protected Container container;
	protected UIFactory factory;
}
