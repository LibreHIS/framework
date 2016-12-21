package ims.framework;

public abstract class LayerBridge extends AbstractBridge
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
		
	public String getCaption()
	{
		return this.container.getCaption();
	}
    public void setScrollToTop(boolean value)
    {
        this.container.setScrollToTop(value);
    }
	public void setCaption(String value)
	{
		this.container.setCaption(value);
	}
	protected void addControl(Control control)
	{
		this.container.addControl(control);
	}
	protected Object getControl(int index)
	{
		return this.container.controls.get(index);
	}
	public void setHeaderEnabled(boolean value)
	{
		this.container.setHeaderEnabled(value);
	}
	public boolean isHeaderEnabled()
	{
		return this.container.isHeaderEnabled();
	}
	public void setHeaderVisible(boolean value)
	{
		this.container.setHeaderVisible(value);
	}
	public boolean isHeaderVisible()
	{
		return this.container.isHeaderVisible();
	}
	public void setVisible(boolean value)
	{
		this.container.setVisible(value);
	}
	public boolean isVisible()
	{
		return this.container.isVisible();
	}
	public void setEnabled(boolean value)
	{
		this.container.setEnabled(value);
	}
	public boolean isEnabled()
	{
		return this.container.isEnabled();
	}
	public boolean isInitialized()
	{		
		return this.container.isInitialized();
	}	
	
	protected Container container;
	protected UIFactory factory;
}
