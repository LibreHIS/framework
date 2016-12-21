package ims.framework.enumerations;

import java.io.Serializable;

/**
 * @author mmihalec
 */
public class ControlAnchoring implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final ControlAnchoring TOP = new ControlAnchoring(1);	
	private static final ControlAnchoring LEFT = new ControlAnchoring(10);
	private static final ControlAnchoring RIGHT = new ControlAnchoring(100);
	private static final ControlAnchoring BOTTOM = new ControlAnchoring(1000);
		
	public static final ControlAnchoring TOPLEFT = new ControlAnchoring(ControlAnchoring.TOP.getID() + ControlAnchoring.LEFT.getID());
	public static final ControlAnchoring TOPRIGHT = new ControlAnchoring(ControlAnchoring.TOP.getID() + ControlAnchoring.RIGHT.getID());	
	public static final ControlAnchoring BOTTOMLEFT = new ControlAnchoring(ControlAnchoring.BOTTOM.getID() + ControlAnchoring.LEFT.getID());
	public static final ControlAnchoring BOTTOMRIGHT = new ControlAnchoring(ControlAnchoring.BOTTOM.getID() + ControlAnchoring.RIGHT.getID());
	
	public static final ControlAnchoring TOPLEFTRIGHT = new ControlAnchoring(ControlAnchoring.TOP.getID() + ControlAnchoring.LEFT.getID() + ControlAnchoring.RIGHT.getID());
	public static final ControlAnchoring TOPBOTTOMRIGHT = new ControlAnchoring(ControlAnchoring.TOP.getID() + ControlAnchoring.BOTTOM.getID() + ControlAnchoring.RIGHT.getID());
	public static final ControlAnchoring TOPBOTTOMLEFT = new ControlAnchoring(ControlAnchoring.TOP.getID() + ControlAnchoring.BOTTOM.getID() + ControlAnchoring.LEFT.getID());
	public static final ControlAnchoring BOTTOMLEFTRIGHT = new ControlAnchoring(ControlAnchoring.BOTTOM.getID() + ControlAnchoring.LEFT.getID() + ControlAnchoring.RIGHT.getID());
	
	public static final ControlAnchoring ALL = new ControlAnchoring(ControlAnchoring.TOP.getID() + ControlAnchoring.LEFT.getID() + ControlAnchoring.RIGHT.getID() + ControlAnchoring.BOTTOM.getID());	
	
	private ControlAnchoring(int id)
	{
		this.id = id;
	}
	
	public int getID()
	{
		return this.id;
	}	
	public void render(StringBuffer sb)
	{
		sb.append(toString());
	}	
	public String toString()
	{
		return String.valueOf(this.id);
	}
	public boolean isAnchoredLeft()
	{
		int stateID = id;
		
		if(stateID < 1000 && stateID >= 100)
		{
			stateID = stateID % 100;
		}	
		else if(stateID >= 1000)
		{
			stateID = stateID % 1000;
			stateID = stateID % 100;
		}
		
		return stateID >= 10;			
	}	
	public boolean isAnchoredTop()
	{
		int stateID = id;
		
		if(stateID < 100 && stateID >= 10)
		{
			stateID = stateID % 10;
		}
		else if(stateID < 1000 && stateID >= 100)
		{
			stateID = stateID % 100;
			stateID = stateID % 10;
		}
		else if(stateID >= 1000)
		{
			stateID = stateID % 1000;
			stateID = stateID % 100;
			stateID = stateID % 10;
		}
		
		return stateID == 1;			
	}
	public boolean isAnchoredRight()
	{
		int stateID = id;
		
		if(stateID >= 1000)
		{
			stateID = stateID % 1000;
		}
		
		return stateID >= 100;			
	}
	public boolean isAnchoredBottom()
	{
		return id >= 1000;			
	}
	
	private int id;
}
