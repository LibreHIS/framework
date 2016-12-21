package ims.framework;

import java.io.Serializable;

public final class ContextVariable implements Serializable			
{
private static final long serialVersionUID = 1L;
 

	public ContextVariable(String name, String key)
	{
		this.name = name;
		this.key = key;		
		this.dependencies = null;		
	}
	public ContextVariable(String name, String key, ContextVariable[] dependencies)
	{
		this.name = name;
		this.key = key;
		this.dependencies = dependencies;		
	}
	
	public void setValue(Context context, Object value)
	{
		Object previousValue = context.get(this.key);
		
		if(value != null)
		{
			context.put(this.key, value);
			
			if(hasDependencies() && (previousValue == null || !previousValue.equals(value)))
				clearDependencies(context);			
		}
		else
		{
			if(previousValue != null)
				clear(context);		
		}
	}
	
	public Object getValue(Context context)
	{	
		return context.get(this.key);
	}
	
	public boolean getValueIsNull(Context context)
	{
		return context.get(this.key) == null;
	}
	
	public void clear(Context context)
	{
		context.put(key, null);
		context.remove(this.key);	
		clearDependencies(context);
	}	
		
	private boolean hasDependencies()
	{
		return this.dependencies != null && this.dependencies.length > 0;
	}
	private void clearDependencies(Context context)
	{
		if(!hasDependencies())
			return;
		
		for(int x = 0; x < this.dependencies.length; x++)
		{
			this.dependencies[x].clear(context);
		}
	}
	public String getName()
	{
		return this.name;
	}
		
	private String name;
	private String key;
	private ContextVariable[] dependencies = null;	
}
