/*
 * Created on 21-Sep-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ims.framework.controls;

import java.io.Serializable;
import java.util.ArrayList;
import ims.vo.ImsCloneable;

/**
 * @author jmacenri
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DrawingControlGroupCollection implements ImsCloneable, Serializable
{
	private static final long serialVersionUID = 1L;
	ArrayList<DrawingControlGroup> coll = new ArrayList<DrawingControlGroup>();
	
	public DrawingControlGroupCollection()
	{
	}
	
	public DrawingControlGroupCollection(ArrayList<DrawingControlGroup> coll)
	{
		this.coll = coll;
	}

	public Object clone()
	{
		DrawingControlGroupCollection clone = new DrawingControlGroupCollection();
		clone.addAll(this);
		return clone;
	}
	private void addAll(DrawingControlGroupCollection groupColl)
	{
		this.coll.addAll(groupColl.getCollection());
	}
	private ArrayList<DrawingControlGroup> getCollection()
	{
		return this.coll;
	}	
	public int size()
	{
		return this.coll.size();
	}

	public void add(DrawingControlGroup group)
	{
		this.coll.add(group);
	}
	
	public DrawingControlGroup get(int index)
	{
		return this.coll.get(index);
	}
}
