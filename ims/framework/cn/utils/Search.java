package ims.framework.cn.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import ims.configuration.InitConfig;
import ims.domain.SessionData;
import ims.framework.FormName;
import ims.framework.SessionConstants;
import ims.framework.cn.UIEngine;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.interfaces.IAppForm;
import ims.framework.interfaces.IDynamicNavigation;
import ims.framework.interfaces.INavForm;
import ims.framework.interfaces.ISearch;
import ims.framework.interfaces.ISearchResult;
import ims.framework.utils.ImagePath;
import ims.framework.utils.SearchResult;

public class Search implements ISearch
{
	private static final long serialVersionUID = -1L;
	private transient HttpSession session;
	private transient UIEngine uiEngine;
	
	private static final class ConcreteFormName extends FormName
	{
		private static final long serialVersionUID = 1L;
		
		public ConcreteFormName(int value)
		{
			super(value);
		}
		public ConcreteFormName(int value, String name)
		{
			super(value, name);
		}
	}

	public Search(HttpSession session, UIEngine uiEngine)
	{
		this.session = session;
		this.uiEngine = uiEngine;
	}
	
	public ISearchResult[] search(String text)
	{
		if(text == null || text.trim().length() < 2)
			return new ISearchResult[0];
		
		List<ISearchResult> result = new ArrayList<ISearchResult>();
		
		SessionData sessData = (SessionData) session.getAttribute(SessionConstants.SESSION_DATA);
		
		IDynamicNavigation navigation = sessData.currentDynamicNavigation.get();
		int index = 1;		
		if(navigation == null || navigation.getNavigation() == null)
		{
			throw new CodingRuntimeException("Navigation is not available");
		}
		for(int x = 0; x < navigation.getNavigation().getAllForms().length; x++)
		{
			int descriptionScore = 0;
			int navFormScore = 0;
			int score = 0;
			
			INavForm navForm = navigation.getNavigation().getAllForms()[x];			
			IAppForm form = navForm.getAppForm();
			
			if(navForm.getIdentifiers() != null && navForm.getIdentifiers().length > 0)
			{				
				String description = null;
				if(form.getFormId() == InitConfig.getAssessmentContainerFormId())
					description = "User Assessment";
				else if(form.getFormId() == InitConfig.getReportViewerFormId())
					description = "User Report";
				
				if(description != null)
				{
					descriptionScore = getScore(description, text, true);
					navFormScore = getScore(navForm.getNodeText(), text, true);
					score = descriptionScore + navFormScore * 2;
					
					if(score > 0)
					{
						if(!linkInResult(result, navForm, false) && uiEngine.canOpen(new ConcreteFormName(form.getFormId())))
						{
							result.add(new SearchResult(index++, score, new ImagePath(1, "arrow-3.gif"), navForm.getNodeText(), description, navForm));
						}
					}
				}
			}
			else if((form.canBeInNavigation() || form.canBeInTopButtons()) && !form.isComponent() && !form.isDialog())
			{
				descriptionScore = getScore(form.getDescription(), text, true);
				navFormScore = getScore(navForm.getNodeText(), text, true);				
				score = descriptionScore + navFormScore * 2;
				
				if(score > 0)
				{
					if(!linkInResult(result, navForm, true) && uiEngine.canOpen(new ConcreteFormName(form.getFormId())))
					{
						result.add(new SearchResult(index++, score, new ImagePath(1, "gui_48.png"), form.getCaption(), form.getDescription(), navForm));
					}
				}
			}
		}
		
		result = sortByScore(result);
				
		ISearchResult[] returnResult = new ISearchResult[result.size()]; 
		result.toArray(returnResult);
		return returnResult;		
	}
	
	private List<ISearchResult> sortByScore(List<ISearchResult> result)
	{
		for(int x = 0; x < result.size(); x++)
		{
			for(int y = 0; y < result.size(); y++)
			{
				if(result.get(x).getScore() > result.get(y).getScore())
				{
					ISearchResult move = result.get(x); 					
					result.set(x, result.get(y));
					result.set(y, move);
				}
			}
		}
		
		return result;
	}	

	private int getScore(String source, String search, boolean exclusive)
	{
		if(source == null || search == null || source.trim().length() == 0 || search.trim().length() == 0)
			return 0;
		
		source = source.toLowerCase();
		search = search.toLowerCase();
		
		String[] items = search.split(" ");
		
		int score = 0;
		int maxScore = items.length;
		
		for(int x = 0; x < items.length; x++)
		{
			if(items[x].length() > 1 || exclusive)
			{
				if(source.contains(items[x]))
					score += (maxScore - x);
				else if(exclusive)
					return 0;
			}
		}
		
		return score;
	}

	private boolean linkInResult(List<ISearchResult> result, INavForm link, boolean isRegularForm) 
	{
		for(int x = 0; x < result.size(); x++)
		{
			if(result.get(x).getLink().equals(link))
				return true;
			if(isRegularForm && result.get(x).getLink().getAppForm().getFormId() == link.getAppForm().getFormId())
				return true;
		}
		
		return false;
	}	
}
