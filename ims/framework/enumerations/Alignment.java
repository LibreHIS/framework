package ims.framework.enumerations;

import java.io.Serializable;

/**
 * @author mmihalec
 */
public class Alignment implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final Alignment LEFT = new Alignment(0);
    public static final Alignment CENTER = new Alignment(1);
    public static final Alignment RIGHT = new Alignment(2);
    
    private Alignment(int id)
    {
        this.id = id;
    }   
    public String toString()
    {
        switch(this.id)
        {
        	case 0: 
        	default:
        		return "Left";
        	case 1: 
        		return "Center";
        	case 2: 
        		return "Right";
        }
    }
    public String render()
    {
        return toString();
    }
    public boolean equals(Object obj)
    {
    	if(obj != null && obj instanceof Alignment)
    		return this.id == ((Alignment)obj).id;
    	return false;
    }
    public int hashCode()
    {
    	return super.hashCode();
    }
    
    private int id;
}
