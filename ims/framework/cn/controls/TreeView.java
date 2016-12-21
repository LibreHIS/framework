package ims.framework.cn.controls;

import ims.framework.Control;
import ims.framework.cn.IVisualControl;
import ims.framework.cn.Menu;
import ims.framework.cn.data.IControlData;
import ims.framework.cn.data.TreeViewData;
import ims.framework.cn.events.IControlEvent;
import ims.framework.cn.events.TreeNodeChecked;
import ims.framework.cn.events.TreeNodeDropped;
import ims.framework.cn.events.TreeNodeEdited;
import ims.framework.cn.events.TreeNodeExpanded;
import ims.framework.cn.events.TreeViewSelected;
import ims.framework.cn.events.TreeViewUnselected;
import ims.framework.controls.TreeNode;
import ims.framework.controls.TreeNodeCollection;
import ims.framework.enumerations.ControlAnchoring;
import ims.framework.enumerations.ControlState;
import ims.framework.exceptions.ConfigurationException;
import ims.framework.exceptions.PresentationLogicException;

public class TreeView extends ims.framework.controls.TreeView implements IVisualControl
{
	private static final long serialVersionUID = 1L;

	public void setContext(Control parentControl, int id, int x, int y, int width, int height, int tabIndex, ControlState viewMode, ControlState editMode, ControlAnchoring anchor, boolean autoPostBack, boolean checkBoxes, boolean autoPostBackForCheckBoxes, Menu menu, boolean autoExpandSelection, boolean allowDragDrop, boolean canUnselect, boolean required, boolean shadow)
	{
		super.setContext(parentControl, id, x, y, width, height, viewMode, editMode, anchor, menu);
		this.tabIndex = tabIndex;
		this.autoPostBack = autoPostBack;
		this.checkBoxes = checkBoxes;
		this.autoPostBackForCheckBoxes = autoPostBackForCheckBoxes;
		this.autoExpandSelection = autoExpandSelection;
		this.allowDragDrop = allowDragDrop;
		this.canUnselect = canUnselect;
		this.required = required;
		this.shadow = shadow;
	}
	protected void free()
	{
		super.free();
		
		this.data = null;		
	}
	public void setEnabled(boolean value)
	{
		super.setEnabled(value);
		this.data.setEnabled(value);
	}
	public void setVisible(boolean value)
	{
		super.setVisible(value);
		this.data.setVisible(value);
	}
	public void clear()
	{
		this.data.clear();		
	}

	public void collapseAll()
	{
		this.data.collapseAll();
	}

	public void expandAll() 
	{
		this.data.expandAll();
	}

	public TreeNodeCollection getNodes()
	{
		return this.data.getNodes();
	}

	public TreeNode getSelectedNode()
	{
		return this.data.getSelectedNode();
	}

	public void clearSelection()
	{
		this.data.setSelection("");
	}

	public Object getValue()
	{
		ims.framework.controls.TreeNode node = this.data.getSelectedNode();
		return node == null ? null : node.getValue();
	}

	public void setValue(Object value)
	{
		this.data.setValue(value);			
	}
	
	public TreeNode getNodeByValue(Object value)
	{
		return this.data.getNodeByValue(value);
	}	


	public void restore(IControlData data, boolean isNew)
	{
		this.data = (TreeViewData)data;
		super.enabled = this.data.isEnabled();
		super.visible = this.data.isVisible();
		
		if(isNew)
		{
			this.data.setAllowDragDrop(this.allowDragDrop);
			this.data.setUnselectable(this.canUnselect);
		}
		else
		{
			this.allowDragDrop = this.data.getDragDropAllowed();
			this.canUnselect = this.data.isUnselectable();
		}
		
		this.data.init(this.checkBoxes);		
	}
	public boolean fireEvent(IControlEvent event) throws PresentationLogicException
	{		
		if(event instanceof TreeNodeEdited)
		{
			TreeNodeEdited typedEvent = (TreeNodeEdited)event;
			boolean nodesChanged = this.data.isNodesChanged();
			boolean rootNodesChanged = this.data.isRootNodesChanged();
			
			TreeNode node = this.data.setNodeText(typedEvent.getSelection(), typedEvent.getText());
			if(node == null)
				throw new RuntimeException("Unable to find edited node");
			
			if(!nodesChanged)
				this.data.setNodesUnchanged();
			if(!rootNodesChanged)
				this.data.setRootNodesUnchanged();
			
			if(super.treeViewNodeEditedDelegate != null)
				super.treeViewNodeEditedDelegate.handle(node);
			
			return true;
		}
		else if(event instanceof TreeViewSelected)
		{
			String newSelection = ((TreeViewSelected)event).getSelection();
			//JME: 20060310: TreeNodeDropped event occuring just before this causes the selection event to be ignored. Without 'if' it will happen anyway
			//if(!this.data.getSelection().equals(newSelection))
			{
				boolean selectionChanged = this.data.isSelectionChanged();
				
				this.data.setSelection(newSelection);
				
				if(!selectionChanged)
					this.data.setSelectionUnchanged();
				
				if(super.treeViewSelectionChangedDelegate != null)
				{	
					super.treeViewSelectionChangedDelegate.handle(this.data.getSelectedNode());
				}
			}
			
			return true;
		}
		else if(event instanceof TreeViewUnselected)
		{
			boolean selectionChanged = this.data.isSelectionChanged();
			
			this.data.setSelection("");
			
			if(!selectionChanged)
				this.data.setSelectionUnchanged();
			
			if(super.treeViewSelectionClearedDelegate != null)
				super.treeViewSelectionClearedDelegate.handle();
			
			return true;
		}
		else if (event instanceof TreeNodeExpanded)
		{
			boolean nodesChanged = this.data.isNodesChanged();
			boolean rootNodesChanged = this.data.isRootNodesChanged();
			
			TreeNodeExpanded tmp = (TreeNodeExpanded)event;			
			this.data.setExpanded(tmp.getSelection(), tmp.isExpanded());
			
			if(!nodesChanged)
				this.data.setNodesUnchanged();
			if(!rootNodesChanged)
				this.data.setRootNodesUnchanged();
			
			if(super.treeViewNodeExpandCollapseDelegate != null)
			{	
				super.treeViewNodeExpandCollapseDelegate.handle(this.data.getNodeByIndex(tmp.getSelection()));
			}
			
			return true;
		}
		else if (event instanceof TreeNodeChecked)
		{
			boolean nodesChanged = this.data.isNodesChanged();
			boolean rootNodesChanged = this.data.isRootNodesChanged();
			
			TreeNodeChecked tmp = (TreeNodeChecked)event;
			this.data.setChecked(tmp.getSelection(), tmp.isChecked());
			
			if(!nodesChanged)
				this.data.setNodesUnchanged();
			if(!rootNodesChanged)
				this.data.setRootNodesUnchanged();
			
			if(super.treeViewCheckDelegate != null)
			{	
				super.treeViewCheckDelegate.handle(this.data.getNodeByIndex(tmp.getSelection()));
			}
			
			return true;
		}
		else if (event instanceof TreeNodeDropped)
		{					
			TreeNodeDropped tmp = (TreeNodeDropped)event;			
			if(tmp.getSelection().equals(tmp.getNewIndex()))
				return true;
			
			boolean selectionChanged = this.data.isSelectionChanged();
			boolean nodesChanged = this.data.isNodesChanged();
			boolean rootNodesChanged = this.data.isRootNodesChanged();

			ims.framework.controls.TreeNode previousParentNode = this.data.getNodeByIndex(tmp.getSelection()).getParent();
			boolean moved = this.data.dragNode(tmp.getSelection(), tmp.getNewIndex());
			
			if(!selectionChanged)
				this.data.setSelectionUnchanged();
			if(!nodesChanged)
				this.data.setNodesUnchanged();
			if(!rootNodesChanged)
				this.data.setRootNodesUnchanged();
			
			if(moved && super.treeViewNodeDropDelegate != null)
			{
				super.treeViewNodeDropDelegate.handle(this.data.getSelectedNode(), previousParentNode);
			}
			
			return true;
		}
		
		return false;
	}
	public void renderControl(StringBuffer sb)
	{
		sb.append("<tree id=\"a");
		sb.append(super.id);
		sb.append("\" x=\"");
		sb.append(super.x);
		sb.append("\" y=\"");
		sb.append(super.y);
		sb.append("\" width=\"");
		sb.append(super.width);
		sb.append("\" height=\"");
		sb.append(super.height);
		sb.append("\" tabIndex=\"");
		sb.append(this.tabIndex);
		
		if(required)
		{
			sb.append("\" required=\"true");
		}
		if(shadow)
		{
			sb.append("\" shadow=\"true");
		}

		sb.append("\" dragDrop=\"");
		sb.append(this.allowDragDrop ? "true" : "false");
		
		if(super.anchor != ControlAnchoring.TOPLEFT)
		{
			sb.append("\" anchor=\"");
			sb.append(super.anchor);
		}
		if(this.autoPostBack)
		{
			sb.append("\" autoPostBack=\"true");
		}
		if(this.checkBoxes)
		{
			sb.append("\" checkBoxes=\"true");					
		}
		if(this.autoPostBackForCheckBoxes)
		{
			sb.append("\" autoPostBackForCheckBoxes=\"true");
		}
		if(!this.autoExpandSelection)
		{
			sb.append("\" selectExpands=\"false");
		}
		if(super.menu != null)
		{
			sb.append("\" menuID=\"");
			sb.append(super.menu.getID());
		}
		
		sb.append("\"/>");
	}
	public void renderData(StringBuffer sb) throws ConfigurationException
	{
		sb.append("<tree id=\"a");
		sb.append(super.id);
		
		if(data.isVisibleChanged())
		{
			sb.append("\" visible=\"");
			sb.append(this.data.isVisible() ? "true" : "false");
			data.setVisibleUnchanged();
		}
		
		if(this.data.isVisible())
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
				{
					sb.append("\" enabled=\"");
					sb.append(this.data.isEnabled() ? "true" : "false");
					data.setEnabledUnchanged();
				}
			}
			
			if(data.isAllowDragDropChanged())
			{
				sb.append("\" dragDrop=\"");
				sb.append(this.allowDragDrop ? "true" : "false");
				data.setAllowDragDropUnchanged();
			}
			
			if(data.isUnselectableChanged())
			{
				sb.append("\" allowClearSelection=\"");
				sb.append(this.canUnselect ? "true" : "false");
				data.setUnselectableUnchanged();
			}
		
			TreeNode selectedNode = this.data.getSelectedNode();
			if(data.isSelectionChanged())
			{
				if(selectedNode == null)
				{
				    sb.append("\" selection=\"-1");
				}
				else
				{
				    sb.append("\" selection=\"");
				    sb.append(this.data.getSelection());
				}
				
				data.setSelectionUnchanged();
			}
			
			if(selectedNode != null)
			{
				if(this.editSelectedNode)
				{
			    	sb.append("\" beginEdit=\"true");
			    	this.editSelectedNode = false;
			    }
			}
			
			if(data.isNodesChanged() || data.isRootNodesChanged())
			{			
				sb.append("\">");			
			    this.data.render(sb, this.allowDragDrop);		    
				sb.append("</tree>");
				
				data.setNodesUnchanged();
				data.setRootNodesUnchanged();
			}
			else
			{
				sb.append("\" />");
			}
		}
		else
		{
			sb.append("\" />");
		}
	}	

	public void moveUp() 
	{
		if (canMoveCurrentUp())
		{
			this.data.moveUp();
		}
	}
	public void moveDown() 
	{
		if (canMoveCurrentDown())
		{
			this.data.moveDown();
		}
	}
	public void moveTo(int toIndex) 
	{
		this.data.moveTo(toIndex);
	}
	public void swap(int index1, int index2) 
	{
	}
	public boolean canMoveCurrentUp() 
	{
		return this.data.canMoveCurrentUp();
	}
	public boolean canMoveCurrentDown() 
	{
		return this.data.canMoveCurrentDown();
	}
	public boolean wasChanged() 
	{
		if(data.isVisibleChanged())
			return true;
		
		if(visible)
		{
			if(!hasAnyParentDisabled())
			{
				if(data.isEnabledChanged())
					return true;
			}
			
			if(data.isAllowDragDropChanged())
				return true;
		
			if(this.editSelectedNode)
				return true;
			
			if(data.isUnselectableChanged())
				return true;
			
			if(data.isSelectionChanged())
				return true;
			
			if(data.isNodesChanged())
				return true;
			
			if(data.isRootNodesChanged())
				return true;
		}
		
		return false;
	}
	public void markUnchanged() 
	{		
	}
	public void setAllowDragDrop(boolean value) 
	{
		this.allowDragDrop = value;
		this.data.setAllowDragDrop(value);		
	}
	public boolean isDragDropAllowed() 
	{
		return this.allowDragDrop;		
	}
	public void setUnselectable(boolean value) 
	{
		this.canUnselect = value;
		this.data.setUnselectable(value);
	}
	public boolean isUnselectable() 
	{
		return this.canUnselect;
	}
	public void beginEditSelectedNode() 
	{
		this.editSelectedNode = true;
	}
	
	private TreeViewData data;
	private int tabIndex;
	private boolean autoPostBack = false;
	private boolean canUnselect = false;
	private boolean required = false;
	private boolean shadow = false;
		
	private boolean checkBoxes;
	private boolean autoPostBackForCheckBoxes;	
	private boolean autoExpandSelection = true;
	private boolean allowDragDrop = false;
	private boolean editSelectedNode = false;	
}
