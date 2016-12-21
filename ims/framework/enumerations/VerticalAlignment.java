package ims.framework.enumerations;

import java.io.Serializable;

/**
 * @author ctomozei
 */
public class VerticalAlignment implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final VerticalAlignment TOP = new VerticalAlignment(0);
	public static final VerticalAlignment MIDDLE = new VerticalAlignment(1);
    public static final VerticalAlignment BOTTOM = new VerticalAlignment(2);
    
    private VerticalAlignment(int id)
    {
        this.id = id;
    }   
    public String toString()
    {
        switch(this.id)
        {
        	case 0: 
        	default:
        		return "top";
        	case 1: 
        		return "middle";
        	case 2: 
        		return "text-top";        	
        }
    }
    public String render()
    {
        return toString();
    }
    public boolean equals(Object obj)
    {
    	if(obj != null && obj instanceof VerticalAlignment)
    		return this.id == ((VerticalAlignment)obj).id;
    	return false;
    }
    public int hashCode()
    {
    	return super.hashCode();
    }
    
    private int id;
}
