package ims.framework.cn;

import java.io.Serializable;

abstract public class FrameworkErrorCodes  implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final int SESSION_EXPIRED = 0;	
	public static final int FRAMEWORK = 1;
	public static final int CONFIGURATION = 2;
	public static final int PRESENTATION_LOGIC = 3;
	public static final int CODING = 4;
	public static final int PARSE_EXCEPTION = 5;
	public static final int SMARTCARD_AUTHENTICATION = 6;
}
