package ims.framework.utils;

import ims.framework.interfaces.INavForm;
import ims.framework.interfaces.ISearchResult;
import ims.framework.utils.Image;

public class SearchResult implements ISearchResult
{
	private static final long serialVersionUID = 1L;
	
	private int id = 0;
	private int score = 0;
	private Image image;
	private String text;
	private String description;	
	private INavForm link;
	
	public SearchResult(int id, int score, String text, INavForm link)
	{
		this(id, score, null, text, null, link);
	}
	public SearchResult(int id, int score, Image image, String text, INavForm link)
	{
		this(id, score, image, text, null, link);
	}
	public SearchResult(int id, int score, String text, String description, INavForm link)
	{
		this(id, score, null, text, description, link);
	}
	public SearchResult(int id, int score, Image image, String text, String description, INavForm link)
	{
		this.id = id;
		this.score = score;
		this.image = image;
		this.text = text;
		this.description = description;
		this.link = link;		
	}

	public int getId()
	{
		return id;
	}
	public int getScore()
	{
		return score;
	}
	public String getDescription()
	{		
		return description;
	}
	public Image getImage()
	{
		return image;
	}
	public String getText()
	{
		return text;
	}	
	public INavForm getLink()
	{
		return link;
	}	
}
