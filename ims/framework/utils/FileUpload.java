package ims.framework.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import ims.configuration.ConfigFlag;
import ims.framework.enumerations.SystemLogLevel;
import ims.framework.enumerations.SystemLogType;
import ims.framework.interfaces.ISystemLogWriter;

import java.io.IOException;

public class FileUpload
{
	private String urlString;	
	private static final int TIMEOUT = 1000 * 60 * 15;
	private static final int MAX_BUFFER_LIMIT_NO_WARNING = 1024*1024;
	byte[] result = null;
	ISystemLogWriter logWriter;
	private static final Logger		LOG		= Logger.getLogger(FileUpload.class);
	
	public FileUpload(ISystemLogWriter logWriter, String urlString)
	{
		this.logWriter = logWriter;
		this.urlString = urlString;
	}
	
	public String upload(byte[] file, String fileName)
	{
		return upload(file, fileName, ConfigFlag.GEN.FILE_UPLOAD_DIR.getValue());
	}

	public String upload(byte[] file, String fileName, String localFolder)
	{
		if(ConfigFlag.GEN.FILE_UPLOAD_DIR.getValue() == null  ||
				(ConfigFlag.GEN.FILE_UPLOAD_DIR.getValue() != null &&
						ConfigFlag.GEN.FILE_UPLOAD_DIR.getValue().length() == 0))
		{
			throw new RuntimeException("ConfigFlag GEN.FILE_UPLOAD_DIR is null");			
		}
		
		HttpClient conn = null;	  				
		StringBuffer sb = new StringBuffer(500);
	
		PostMethod filePost = new PostMethod(urlString);
		conn = new HttpClient(new MultiThreadedHttpConnectionManager());
		conn.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT);		
		conn.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
		conn.getParams().setIntParameter(HttpMethodParams.BUFFER_WARN_TRIGGER_LIMIT, MAX_BUFFER_LIMIT_NO_WARNING);
		  	  	  
		Part[] data = 
		{
		    new StringPart("name", localFolder), 
		    new StringPart("filename", fileName),	           
		    new FilePart(fileName, new ByteArrayPartSource(fileName, file))	           
		};
		  		
		filePost.setRequestEntity(new MultipartRequestEntity(data, filePost.getParams()));
	  		
	    int iGetResultCode;
		try
		{
			iGetResultCode = conn.executeMethod(filePost);
			if (iGetResultCode == HttpStatus.SC_OK) 
			{
				sb.append("Upload complete, status=" + iGetResultCode);
				sb.append("\nUpload complete, response=" + HttpStatus.getStatusText(iGetResultCode));
				sb.append("\nUpload complete, response=" + filePost.getResponseBodyAsString());
				
				LOG.info(sb.toString());
				if (logWriter != null)
				{
					logWriter.createSystemLogEntry(SystemLogType.FILE_UPLOADING, SystemLogLevel.INFORMATION, sb.toString());
				}
				
				if (filePost.getResponseBodyAsString() != null &&
						filePost.getResponseBodyAsString().length() != 0 &&
							filePost.getResponseBodyAsString() != "")  	     		   
				    			return parse(filePost.getResponseBodyAsString());
				
			}
			else 
			{
				sb.append("Upload failed, response=" + HttpStatus.getStatusText(iGetResultCode));
				sb.append("Upload failed, response=" + filePost.getResponseBodyAsString());
				LOG.error("Upload failed, response=" + HttpStatus.getStatusText(iGetResultCode));
				if (logWriter != null)
				{
					logWriter.createSystemLogEntry(SystemLogType.FILE_UPLOADING, SystemLogLevel.ERROR, "Upload failed, response=" + HttpStatus.getStatusText(iGetResultCode));
				}
			}
		} 
		catch (HttpException e)
		{			 			 
			 LOG.error("From ServletCom CLIENT REQUEST:" + e);
			 if (logWriter != null)
			 {
				 logWriter.createSystemLogEntry(SystemLogType.FILE_UPLOADING, SystemLogLevel.ERROR, e.toString());
			 }
		} 
		catch (IOException e)
		{			
			LOG.error("From ServletCom CLIENT REQUEST:" + e);
			if (logWriter != null)
			{
				logWriter.createSystemLogEntry(SystemLogType.FILE_UPLOADING, SystemLogLevel.ERROR, e.toString());
			}
		}
		finally
		{
			filePost.releaseConnection();
		}
	  	
	  	return null;		
	}

	private String parse(String xml)
	{
		if (xml == null)
			return null;
		
		return (xml.split("\""))[1];	
	}
}