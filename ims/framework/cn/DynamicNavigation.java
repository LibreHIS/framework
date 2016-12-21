package ims.framework.cn;

import java.io.Serializable;

import ims.base.interfaces.IModifiable;
import ims.configuration.InitConfig;
import ims.domain.SessionData;
import ims.framework.GenericChangeableCollection;
import ims.framework.enumerations.FormAccess;
import ims.framework.enumerations.NavigationStyle;
import ims.framework.interfaces.IAppImage;
import ims.framework.interfaces.INavForm;
import ims.framework.interfaces.INavRootGroup;
import ims.framework.interfaces.INavSecondGroup;
import ims.framework.interfaces.INavigation;
import ims.framework.utils.StringUtils;

public final class DynamicNavigation extends ims.framework.DynamicNavigation implements Serializable, IModifiable
{
	private static final long serialVersionUID = 1L;
	
	class NavigationItem implements Serializable, IModifiable
	{
		private static final long serialVersionUID = 1L;
		
		public NavigationItem(ims.framework.FormAccessLevel navigationAccess, int id, String caption, IAppImage image)
		{
			this(navigationAccess, id, caption, image, null);
		}
		public NavigationItem(ims.framework.FormAccessLevel navigationAccess, int id, String caption, INavForm form)
		{
			this(navigationAccess, id, caption, (form.getAppForm() == null ? null : form.getAppForm().getImage()), form);
		}
		private NavigationItem(ims.framework.FormAccessLevel navigationAccess, int id, String caption, IAppImage image, INavForm form)
		{
			this.navigationAccess = navigationAccess; 
			this.id = id;
			this.caption = caption;		
			this.image = image;
			this.form = form;
		}
		public IAppImage getImage()
		{
			return this.image;
		}
		public INavForm getForm(int id)
		{
			if(this.id == id && this.form != null)
				return form;
			
			if(items == null)
				return null;
			
			for(int x = 0; x < items.size(); x++)
			{
				INavForm navForm = items.get(x).getForm(id);
				if(navForm != null)
					return navForm;
			}
			
			return null;
		}
		public NavigationItem addChildGroup(int id, String caption, IAppImage image)
		{
			return addChild(id, caption, image);
		}
		public NavigationItem addChildForm(int id, INavForm form)
		{			
			return addChild(id, form.getNodeText(), form);
		}
		private NavigationItem addChild(int id, String caption, IAppImage image)
		{
			NavigationItem item = new NavigationItem(navigationAccess, id, caption, image);
			items.add(item);
			return item;
		}
		private NavigationItem addChild(int id, String caption, INavForm form)
		{
			NavigationItem item = new NavigationItem(navigationAccess, id, caption, form);
			items.add(item);
			return item;
		}
		
		public boolean isVisible() throws Exception
		{
			if(shouldBeVisible != null)
				return shouldBeVisible.booleanValue();
			
			if(this.form == null)
			{
				int count = this.items.size();				
				for(int x = 0; x < count; x++)
				{
					if(this.items.get(x).isVisible())
					{
						shouldBeVisible = Boolean.TRUE;
						return true;
					}
				}
				
				shouldBeVisible = Boolean.FALSE;
				return false;
			}
			
			shouldBeVisible = new Boolean(navigationAccess.isAccessibleFromNavigation(this.form, sessData));  // FWUI-1770
			return shouldBeVisible.booleanValue(); 
		}		
		public void renderForm(StringBuffer sb)
		{
			shouldRenderForm = false;
			int childCount = this.items.size();
			
			sb.append("<node id=\"");
			sb.append(this.id);
			
			if(image != null)
			{
				sb.append("\" img=\"");			
				sb.append(image.getImagePath());
			}
			else if(form == null)
			{
				sb.append("\" img=\"g/folder1.png");							
			}
			else 
			{
				sb.append("\" img=\"g/form_48.png");							
			}
			sb.append("\" caption=\"");
			sb.append(StringUtils.encodeXML(this.caption));
			if(childCount > 0)
			{
				sb.append("\" group=\"true");
				sb.append("\">");				
				for(int x = 0; x < childCount; x++)
				{
					this.items.get(x).renderForm(sb);
				}
				sb.append("</node>");
			}
			else
			{
				sb.append("\"/>");				
			}
		}
		public void preRenderData() throws Exception
		{			
			boolean shouldBeVisible = isVisible();
			
			if(!wasChanged)
				wasChanged = visible != shouldBeVisible;
			visible = shouldBeVisible;
			
			if(visible)
			{
				int count = this.items.size();
				for(int x = 0; x < count; x++)
				{
					this.items.get(x).preRenderData();
				}									
			}
		}
		public void resetVisibleState()
		{
			shouldBeVisible = null;
			
			int child = this.items.size();
			for(int x = 0; x < child; x++)
			{
				this.items.get(x).resetVisibleState();
			}
		}
		/*public void renderData(StringBuffer sb) throws Exception
		{
			int childCount = this.items.size();
			
			sb.append("<node id=\"");
			sb.append(this.id);
			sb.append("\" visible=\"");
			sb.append(visible ? "true" : "false");
			if(visible)
			{
				if(childCount > 0)
				{
					sb.append("\">");				
					for(int x = 0; x < childCount; x++)
					{
						this.items.get(x).renderData(sb);
					}
					sb.append("</node>");
				}
				else
				{
					sb.append("\"/>");				
				}
			}
			else
			{
				sb.append("\"/>");				
			}			
		}*/
		public void renderData(StringBuffer sb) throws Exception
		{
			int childCount = this.items.size();
			
			sb.append("<node id=\"");
			sb.append(this.id);
			/*if(form != null && form.getAppForm() != null && form.getAppForm().getImage() != null)
			{
				sb.append("\" img=\"");			
				form.getAppForm().getImage().getImagePath();
			}*/
			sb.append("\" visible=\"");
			sb.append(visible ? "true" : "false");
			sb.append("\" />");
			
			if(visible)
			{
				for(int x = 0; x < childCount; x++)
				{
					this.items.get(x).renderData(sb);
				}
			}			
		}		
		
		private ims.framework.FormAccessLevel navigationAccess;
		private String caption;
		private int id;
		private INavForm form;
		private IAppImage image;
		private GenericChangeableCollection<NavigationItem> items = new GenericChangeableCollection<NavigationItem>();
		private boolean visible = true;
		private boolean wasChanged = true;
		private Boolean shouldBeVisible;

		public void markUnchanged()
		{
			wasChanged = false;				
			items.markUnchanged();
		}
		public boolean wasChanged()
		{
			return wasChanged || items.wasChanged();
		}
	}
	
	public DynamicNavigation(SessionData sessData, INavigation dynamicNavigation, ims.framework.FormAccessLevel navigationAccess)
	{
		this.sessData = sessData;
		this.dynamicNavigation = dynamicNavigation; 
		this.navigationAccess = navigationAccess;		
		
		build(dynamicNavigation, navigationAccess);
	}
	public INavForm getForm(int id)
	{
		if(this.items == null)
			return null;
		
		for(int x = 0; x < items.size(); x++)
		{
			INavForm form = items.get(x).getForm(id);
			if(form != null)
				return form;
		}
		
		return null;
	}
	public Integer getUniqueNavigationId(int formId)
	{
		uniqueCount = 0;
		return getUniqueNavigationId(formId, items);
	}
	private Integer getUniqueNavigationId(int formId, GenericChangeableCollection<NavigationItem> items)
	{
		int id = 0;
		
		for(int x = 0; x < items.size(); x++)
		{
			NavigationItem navItem = items.get(x);
			INavForm existingForm = navItem.form;
			if(existingForm != null && existingForm.getAppForm().getFormId() == formId)
			{
				uniqueCount++;
				
				// form is not unique in the navigation
				if(uniqueCount > 1)
					return null;
				id = navItem.id;  
			}
			else
			{
				Integer subId = getUniqueNavigationId(formId, navItem.items);
				if(uniqueCount > 1)
					return null;
				
				if(subId != null)
					id = subId.intValue();									
			}
		}
		
		// form not found
		if(id == 0)
			return null;
		
		return new Integer(id);
	}
	void build(INavigation dynamicNavigation, ims.framework.FormAccessLevel navigationAccess)
	{
		this.items.clear();
		this.nextId = 0;
		
		int count = dynamicNavigation.getNavRootGroups().length;
		for(int x = 0; x < count; x++)
		{
			INavRootGroup group = dynamicNavigation.getNavRootGroups()[x];
			NavigationItem item = new NavigationItem(navigationAccess, nextId++, group.getGroupName(), group.getNavGroupImage());			
			build(item, group);
			items.add(item);
		}
	}
	void build(NavigationItem parentItem, INavRootGroup group)
	{
		int groupsCount = group.getNavGroups().length;
		int formsCount = group.getNavForms().length;		
		int count = groupsCount + formsCount;
		
		for(int x = 0; x < count; x++)
		{
			boolean found = false;
			
			for(int i = 0; i < groupsCount; i++)
			{
				INavSecondGroup secondGroup = group.getNavGroups()[i];
				if(secondGroup.getPositionIndex() == x)
				{
					found = true;
					build(parentItem.addChildGroup(nextId++, secondGroup.getGroupName(), secondGroup.getNavGroupImage()), secondGroup);
					break;
				}				
			}
			
			if(!found)
			{
				for(int i = 0; i < formsCount; i++)
				{
					INavForm form = group.getNavForms()[i];
					if(form.getPositionIndex() == x)
					{						
						found = true;
						parentItem.addChildForm(nextId++, form);
						break;
					}
				}
			}
			
			if(!found)
				throw new RuntimeException("Navigation item at index " + String.valueOf(x) + " was not found for the root group " + group.getGroupName());
		}
	}
	void build(NavigationItem parentItem, INavSecondGroup group)
	{
		int count = group.getNavForms().length;
		
		for(int x = 0; x < count; x++)
		{
			INavForm form = group.getNavForms()[x];
			parentItem.addChildForm(nextId++, form);
		}
	}
	public boolean shouldRenderForm()
	{
		return shouldRenderForm;
	}
	public void renderForm(StringBuffer sb) throws Exception
	{
		render(sb, false);		
	}
	public void preRenderData() throws Exception
	{
		for(int x = 0; x < items.size(); x++)
		{
			items.get(x).preRenderData();
		}
		for(int x = 0; x < items.size(); x++)
		{
			items.get(x).resetVisibleState();
		}
	}
	public void renderData(StringBuffer sb) throws Exception
	{
		render(sb, true);
	}
	void render(StringBuffer sb, boolean data) throws Exception
	{
		sb.append("<navigation" + (InitConfig.getIsTabLayout() ? " type=\"tabnavigation\" screenItemID=\"navigationBG\" screenItemID2=\"navigation\"" : "") + ">");
		
		if(data)
		{
			sb.append("<modify");
		}
		else
		{
			sb.append("<create");
			
			if(this.dynamicNavigation.getNavigationStyle() != null && this.dynamicNavigation.getNavigationStyle() == NavigationStyle.Image)
			{
				sb.append(" type=\"iconnavigation\" ");
			}
			else
			{
				sb.append(" type=\"navigation\" ");
			}
		}			
		
		if(InitConfig.isNavigationCollapsible())
		{
			if(InitConfig.isNavigationCollapsible())
			{
				sb.append(" collapsed=\"");
				if(sessData.navigationCollapsed.get())
				{
					sb.append("true");	
				}
				else
				{
					sb.append("false");
				}
				sb.append("\"");
			}
			else
			{
				sb.append(" canExpandCollapse=\"false\"");
			}
			
		}
		sb.append(">");			
		
		for(int x = 0; x < items.size(); x++)
		{			
			if(data)
			{
				items.get(x).renderData(sb);				
			}
			else
			{
				items.get(x).renderForm(sb);
			}
		}
				
		if(data)
			sb.append("</modify>");
		else
			sb.append("</create>");
		
		if(!data)
		{
			preRenderData();
			
			sb.append("<modify " + (!InitConfig.isNavigationCollapsible() ? "canExpandCollapse=\"false\"" : "") + ">");
			for(int x = 0; x < items.size(); x++)
			{			
				items.get(x).renderData(sb);								
			}
			sb.append("</modify>");
		}
				
		sb.append("</navigation>");
	}
	public INavigation getNavigation()
	{
		return dynamicNavigation;
	}
	public FormAccess getFormAccess(INavForm navForm) throws Exception
	{
		return navigationAccess.getFormAccess(navForm, sessData);  // FWUI-1770
	}
	public void markChanged()
	{
		wasChanged = true;
	}	
	public void markUnchanged()
	{
		wasChanged = false;
		items.markUnchanged();
	}
	public boolean wasChanged()
	{
		return wasChanged || items.wasChanged();
	}
	
	GenericChangeableCollection<NavigationItem> items = new GenericChangeableCollection<NavigationItem>();
	INavigation dynamicNavigation;
	SessionData sessData;
	ims.framework.FormAccessLevel navigationAccess;	
	boolean shouldRenderForm = true;
	int nextId;
	int uniqueCount;
	private boolean wasChanged = true;
}
