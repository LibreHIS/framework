	package ims.framework.cn;

import ims.domain.ContextEvalFactory;
import ims.domain.SessionData;
import ims.framework.FormAccessLevel;
import ims.framework.interfaces.ITopButton;
import ims.framework.interfaces.ITopButtonSection;
import ims.framework.utils.StringUtils;

public class TopButtonSection extends ims.framework.TopButtonSection
{
	private static final long serialVersionUID = 1L;	

	public TopButtonSection(ITopButtonSection section)
	{
		if(section == null)
			throw new RuntimeException("Invalid top button section");
		
		id = section.getITopButtonSectionID();
		text = section.getITopButtonSectionText() == null ? "" : section.getITopButtonSectionText();
		
		ITopButton[] buttons = section.getITopButtonSectionButtons();
		if(buttons != null)
		{
			for(int x = 0; x < buttons.length; x++)
			{
				items.add(new TopButton(buttons[x]));			
			}
		}
		
		visible = items.size() > 0;
	}
	public TopButtonSection(int id)
	{
		this(id, "", true);
	}
	public TopButtonSection(int id, String text, boolean visible)
	{
		super(id, text, visible);
	}
	public void preRenderContext(ContextEvalFactory contextEvalFactory, FormAccessLevel formAccessLevel, SessionData sessData, boolean currentFormIsReadOnly) throws Exception
	{
		for(int x = 0; x < items.size(); x++)
		{
			items.get(x).preRenderContext(contextEvalFactory, formAccessLevel, sessData, currentFormIsReadOnly);
		}
	}
	public void render(StringBuffer sb)
	{
		sb.append("<section id=\"");
		sb.append(id);
		
		if(visible)
		{
			sb.append("\" caption=\"");
			sb.append(StringUtils.encodeXML(text));
		}
		
		sb.append("\" visible=\"");
		sb.append(visible ? "true" : "false");
		
		if(visible)
		{
			sb.append("\">");
			
			for(int x = 0; x < items.size(); x++)
			{
				items.get(x).render(sb);			
			}
			
			sb.append("</section>");
		}
		else
		{
			sb.append("\" />");
		}
	}	
	public ims.framework.TopButton find(int id)
	{
		for(int x = 0; x < items.size(); x++)
		{
			if(items.get(x).getID() == id)
				return items.get(x);
		}
		
		return null;
	}
}
