package ims.framework.enumerations;

import java.io.Serializable;

public class CharacterCasing implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final CharacterCasing NORMAL = new CharacterCasing(0);
    public static final CharacterCasing UPPER = new CharacterCasing(1);
    public static final CharacterCasing LOWER = new CharacterCasing(2);
    
    private CharacterCasing(int id)
    {
        this.id = id;
    }   
    public String toString()
    {
        switch(this.id)
        {
        	case 0: 
        	default:
        		return "Normal";
        	case 1: 
        		return "Upper";
        	case 2: 
        		return "Lower";
        }
    }
    public String render()
    {
        return toString().toLowerCase();
    }
    public boolean equals(Object obj)
    {
    	if(obj != null && obj instanceof CharacterCasing)
    		return this.id == ((CharacterCasing)obj).id;
    	return false;
    }
    public int hashCode()
    {
    	return super.hashCode();
    }
    
    private int id;
}
