package ims.framework.controls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ims.framework.enumerations.Position;
import ims.framework.exceptions.CodingRuntimeException;
import ims.framework.utils.Color;
import ims.framework.utils.Image;
import ims.framework.utils.ImagePath;

public class Bed implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Deprecated
	public static Bed WHITE = new Bed(0, new ImagePath(-10001, "g/bed-1.emz"), "");
	@Deprecated
	public static Bed BLACK = new Bed(1, new ImagePath(-10002, "g/bed-black-1.emz"), "");
	@Deprecated
	public static Bed BLUE = new Bed(2, new ImagePath(-10003, "g/bed-blue-1.emz"), "");
	@Deprecated
	public static Bed DARKBLUE = new Bed(3, new ImagePath(-10004, "g/bed-darkblue-1.emz"), "");
	@Deprecated
	public static Bed DARKGRAY = new Bed(4, new ImagePath(-10005, "g/bed-darkgray-1.emz"), "");
	@Deprecated
	public static Bed DARKGREEN = new Bed(5, new ImagePath(-10006, "g/bed-darkgreen-1.emz"), "");
	@Deprecated
	public static Bed GREY = new Bed(6, new ImagePath(-10007, "g/bed-gray-1.emz"), "");
	@Deprecated
	public static Bed GREEN = new Bed(7, new ImagePath(-10008, "g/bed-green-1.emz"), "");
	@Deprecated
	public static Bed LIGHTBLUE = new Bed(8, new ImagePath(-10009, "g/bed-lightblue-1.emz"), "");
	@Deprecated
	public static Bed ORANGE = new Bed(9, new ImagePath(-10010, "g/bed-orange-1.emz"), "");
	@Deprecated
	public static Bed RED = new Bed(10, new ImagePath(-10011, "g/bed-red-1.emz"), "");
	@Deprecated
	public static Bed YELLOW = new Bed(11, new ImagePath(-10012, "g/bed-yellow-1.emz"), "");
	@Deprecated
	public static Bed PINK = new Bed(12, new ImagePath(-10013, "g/bed-pink-1.emz"), "");
	
	//Type=BED
	public static Bed WHITE_BED = new Bed(13, new ImagePath(-10001, "g/bed-1.emz"), "");
	public static Bed BLACK_BED = new Bed(14, new ImagePath(-10002, "g/bed-black-1.emz"), "");
	public static Bed BLUE_BED = new Bed(15, new ImagePath(-10003, "g/bed-blue-1.emz"), "");
	public static Bed DARKBLUE_BED = new Bed(16, new ImagePath(-10004, "g/bed-darkblue-1.emz"), "");
	public static Bed DARKGRAY_BED = new Bed(17, new ImagePath(-10005, "g/bed-darkgray-1.emz"), "");
	public static Bed DARKGREEN_BED = new Bed(18, new ImagePath(-10006, "g/bed-darkgreen-1.emz"), "");
	public static Bed GREY_BED = new Bed(19, new ImagePath(-10007, "g/bed-gray-1.emz"), "");
	public static Bed GREEN_BED = new Bed(20, new ImagePath(-10008, "g/bed-green-1.emz"), "");
	public static Bed LIGHTBLUE_BED = new Bed(21, new ImagePath(-10009, "g/bed-lightblue-1.emz"), "");
	public static Bed ORANGE_BED = new Bed(22, new ImagePath(-10010, "g/bed-orange-1.emz"), "");
	public static Bed RED_BED = new Bed(23, new ImagePath(-10011, "g/bed-red-1.emz"), "");
	public static Bed YELLOW_BED = new Bed(24, new ImagePath(-10012, "g/bed-yellow-1.emz"), "");
	public static Bed PINK_BED = new Bed(25, new ImagePath(-10013, "g/bed-pink-1.emz"), "");
	
	//Type=CHAIR
	public static Bed WHITE_CHAIR = new Bed(26, new ImagePath(-10014, "g/chair-1.emz"), "");
	public static Bed BLACK_CHAIR = new Bed(27, new ImagePath(-10015, "g/chair-black-1.emz"), "");
	public static Bed BLUE_CHAIR = new Bed(28, new ImagePath(-10016, "g/chair-blue-1.emz"), "");
	public static Bed DARKBLUE_CHAIR = new Bed(29, new ImagePath(-10017, "g/chair-darkblue-1.emz"), "");
	public static Bed DARKGRAY_CHAIR = new Bed(30, new ImagePath(-10018, "g/chair-darkgray-1.emz"), "");
	public static Bed DARKGREEN_CHAIR = new Bed(31, new ImagePath(-10019, "g/chair-darkgreen-1.emz"), "");
	public static Bed GREY_CHAIR = new Bed(32, new ImagePath(-10020, "g/chair-gray-1.emz"), "");
	public static Bed GREEN_CHAIR = new Bed(33, new ImagePath(-10021, "g/chair-green-1.emz"), "");
	public static Bed LIGHTBLUE_CHAIR = new Bed(34, new ImagePath(-10022, "g/chair-lightblue-1.emz"), "");
	public static Bed ORANGE_CHAIR = new Bed(35, new ImagePath(-10023, "g/chair-orange-1.emz"), "");
	public static Bed RED_CHAIR = new Bed(36, new ImagePath(-10024, "g/chair-red-1.emz"), "");
	public static Bed YELLOW_CHAIR = new Bed(38, new ImagePath(-10025, "g/chair-yellow-1.emz"), "");
	public static Bed PINK_CHAIR = new Bed(38, new ImagePath(-10026, "g/chair-pink-1.emz"), "");
	
	public Bed(int id ,Image type, String vml)
	{
		this(id, type, vml, Position.RIGHT, Position.LEFT);
	}
	public Bed(Integer id, Image type, String vml, Position textPosition, Position imagePosition)
	{
		this.id = id;
		this.type = type;
		this.vml = vml;
		this.textPosition = textPosition;
		this.imagePosition = imagePosition;
		
		if(type.getImagePath().contains("chair"))
			color = Bed.WHITE_CHAIR;
		if(type.getImagePath().contains("bed"))
			color = Bed.WHITE_BED;
	}
	public String getVML()
	{
		return this.vml;
	}
	public void setVML(String vml)
	{
		this.vml = vml;
	}
	public Image getType()
	{
		return this.type;
	}
	public String getTooltip()
	{
		return this.tooltip;
	}
	public void setTooltip(String value)
	{
		this.tooltip = value;
	}
	public void addAttachedImage(Image image, String tooltip)
	{
		addAttachedImage(-1, image, tooltip, false);
	}
	public void addAttachedImage(int id, Image image, String tooltip)
	{
		addAttachedImage(id, image, tooltip, true);
	}
	public void addAttachedImage(int id, Image image, String tooltip, boolean postBack)
	{
		if(image == null)
			throw new CodingRuntimeException("Invalid image");
		
		attachedImages.add(image);
		attachedImagesTooltips.add(tooltip);
		
		if (id != -1 && attachedImagesIds.contains(Integer.valueOf(id)))
			throw new CodingRuntimeException("Attached image id must be unique");
		
		attachedImagesIds.add(id);
		attachedImagesPostback.add(postBack);
	}
	public void clearAttachedImages()
	{
		attachedImages.clear();
		attachedImagesTooltips.clear();
		attachedImagesIds.clear();
		attachedImagesPostback.clear();
	}
	public ArrayList<Image> getAttachedImages()
	{
		return attachedImages;
	}
	public ArrayList<String> getAttachedImagesTooltips()
	{
		return attachedImagesTooltips;
	}
	public ArrayList<Integer> getAttachedImagesIds()
	{
		return attachedImagesIds;
	}
	public ArrayList<Boolean> getAttachedImagesPostback()
	{
		return attachedImagesPostback;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String value)
	{
		text = value;
	}
	public Position getTextPosition()
	{
		return textPosition;
	}
	public void setTextPosition(Position value)
	{
		textPosition = value;
	}
	public Position getImagePosition()
	{
		return imagePosition;
	}
	public void setImagePosition(Position value)
	{
		imagePosition = value;
	}
	public Bed getColor()
	{
		return color;
	}
	public void setColor(Bed value)
	{
		if(type.getImagePath().contains("chair"))
			color = value == null ? Bed.WHITE_CHAIR : value;
		else if(type.getImagePath().contains("bed"))
			color = value == null ? Bed.WHITE_BED : value;
		
		if(color.equals(Bed.BLACK) || color.equals(Bed.BLACK_BED))
		{
			setNumberColor(Color.White);
		}
		else if(color.equals(Bed.BLUE) || color.equals(Bed.BLUE_BED))
		{
			setNumberColor(Color.White);
		}
		else if(color.equals(Bed.DARKBLUE) || color.equals(Bed.DARKBLUE_BED))
		{
			setNumberColor(Color.White);
		}
		else if(color.equals(Bed.DARKGRAY) || color.equals(Bed.DARKGRAY_BED))
		{
			setNumberColor(Color.White);
		}
		else if(color.equals(Bed.DARKGREEN) || color.equals(Bed.DARKGREEN_BED))
		{
			setNumberColor(Color.White);
		}
		else if(color.equals(Bed.GREEN) || color.equals(Bed.GREEN_BED))
		{
			setNumberColor(Color.Black);
		}
		else if(color.equals(Bed.GREY) || color.equals(Bed.GREY_BED))
		{
			setNumberColor(Color.Black);
		}
		else if(color.equals(Bed.LIGHTBLUE) || color.equals(Bed.LIGHTBLUE_BED))
		{
			setNumberColor(Color.Black);
		}
		else if(color.equals(Bed.ORANGE) || color.equals(Bed.ORANGE_BED))
		{
			setNumberColor(Color.Black);
		}
		else if(color.equals(Bed.RED) || color.equals(Bed.RED_BED))
		{
			setNumberColor(Color.White);
		}
		else if(color.equals(Bed.WHITE) || color.equals(Bed.WHITE_BED))
		{
			setNumberColor(Color.Black);
		}
		else if(color.equals(Bed.YELLOW) || color.equals(Bed.YELLOW_BED))
		{
			setNumberColor(Color.Black);
		}
		else if(color.equals(Bed.PINK) || color.equals(Bed.PINK_BED))
		{
			setNumberColor(Color.Black);
		}
		else
		{
			setNumberColor(Color.Black);
		}
	}
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer value)
	{
		id = value;
	}
	public Integer getClickedImageId()
	{
		return clickedImageID;
	}
	public void setClickedImageId(Integer value)
	{
		clickedImageID = value;
	}
	public Object getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(Object value)
	{
		identifier = value;
	}
	public Color getNumberColor()
	{
		return numberColor;
	}
	public void setNumberColor(Color value)
	{
		numberColor = value;
	}
	public String getNumber()
	{
		return number == null ? "" : number;
	}
	public void setNumber(String value)
	{
		number = value == null ? "" : value;
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof Bed)
			return ((Bed)obj).getId() == getId();
		return false;
	}
	public boolean canBeDeleted()
	{
		return canBeDeleted;
	}
	public void setCanBeDeleted(boolean value)
	{
		canBeDeleted = value;
	}
	
	private Integer id = null;
	private Object identifier = null;
	private Integer clickedImageID = null;
	private Image type;
	private ArrayList<Image> attachedImages = new ArrayList<Image>();
	private ArrayList<String> attachedImagesTooltips = new ArrayList<String>();	
	private ArrayList<Integer> attachedImagesIds = new ArrayList<Integer>();
	private ArrayList<Boolean> attachedImagesPostback = new ArrayList<Boolean>();

	private String text;
	private Color numberColor = Color.Black;
	private Bed color;	
	private String vml;
	private String number;
	private String tooltip;
	private boolean canBeDeleted = true;
	private Position textPosition = Position.RIGHT;
	private Position imagePosition = Position.LEFT;
}
