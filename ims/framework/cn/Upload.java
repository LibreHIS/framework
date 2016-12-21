package ims.framework.cn;

import ims.configuration.ConfigFlag;
import ims.configuration.EnvironmentConfig;
import ims.framework.interfaces.IUploadServlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class Upload extends HttpServlet implements IUploadServlet
{
	private static final long serialVersionUID = -7592929424792915821L;
	private static final org.apache.log4j.Logger LocalLogger = ims.utils.Logging.getLogger(Upload.class);
	public static final String UPLOADED_FILENAME = "UploadedFilename";	

	 public void init(ServletConfig cfg) throws javax.servlet.ServletException 
	 {
		 super.init(cfg);
	     log("Upload Servlet started...");
	     try {
			ims.configuration.JNDI.setUploadServlet(this);
	     }
	     catch (Exception e) 
		 {
	    	 log("Upload Servlet failed to initialize: " + e.toString());
		     throw new ServletException(e);
		 }   
	 }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{	
		if (LocalLogger.isInfoEnabled())
			LocalLogger.info("File Upload Request: ");

		List<UploadFileParams> params = new ArrayList<UploadFileParams>();
		Map<String, UploadFileContent> contents = new HashMap<String, UploadFileContent>();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<response>");

		String uploadDir = ConfigFlag.GEN.FILE_UPLOAD_DIR.getValue();
		String rootPath;
		String mountPoint = EnvironmentConfig.getFileUploadMountpoint();
		if (mountPoint.equals(""))
		{
			rootPath = getServletContext().getRealPath(uploadDir);
		}
		else
		{
			if (uploadDir.startsWith("/"))
				uploadDir = uploadDir.substring(1);
				
			if (mountPoint.endsWith("/"))
				rootPath = mountPoint + uploadDir;
			else
				rootPath = mountPoint + "/" + uploadDir;
		}

		ensureRootPathExists(rootPath);
		String uploadParamsXml = "";
		
		//TODO: Total upload max size needs to be configurable
		MultipartParser parser = new MultipartParser(request, 50000000);
		String uniqueID = null;
		boolean overwrite = false;

		Part part = null;
		FilePart filePart = null;
		
		while ((part = parser.readNextPart()) != null)
		{
			if (part.isParam())
			{
				ParamPart paramPart = (ParamPart) part;
				if (paramPart.getName().equals("ims_file_upload_params"))
				{
					uploadParamsXml = paramPart.getStringValue();
				}
				
				if (paramPart.getName().equals("uniqueID"))
					uniqueID = paramPart.getStringValue();
				
				if (paramPart.getName().equals("overwrite"))
					overwrite = new Boolean(paramPart.getStringValue());
			}
			else if (part.isFile())
			{
				filePart = (FilePart) part;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				filePart.writeTo(byteArrayOutputStream);
				UploadFileContent upf = new UploadFileContent();
				upf.contentType = filePart.getContentType();
				upf.content = byteArrayOutputStream.toByteArray();
				upf.fileName = filePart.getFileName();
				
				contents.put(filePart.getFilePath(), upf);
			}
		}
		
		if (!uploadParamsXml.equals(""))
		{			
			try
			{
				Document doc = new SAXReader().read(new ByteArrayInputStream(uploadParamsXml.getBytes()));
				List l = doc.selectNodes("/files/file");
				for (int i = 0; i < l.size(); i++)
				{
					UploadFileParams up = new UploadFileParams();
					Element el = (Element)l.get(i);
					up.controlId = el.attributeValue("id");
					up.fileName = el.attributeValue("filename");
					up.overwrite = new Boolean(el.attributeValue("overwrite","true")).booleanValue();
					up.maxSize = Integer.parseInt(el.attributeValue("max_size","10000000"));	
					up.allowedTypes = el.attributeValue("allowed_types", "gif,jpg,jpeg,xml,zip");
					params.add(up);
				}				
			}
			catch (DocumentException e)
			{
				e.printStackTrace();

				sb.append(" error=\"");
				sb.append(e.getMessage());
				sb.append("\"");				
				sb.append("</response>");
				
				respond(response, sb);
				return;
			}						
		}
		
		if (params.size() == 0 && contents.size() > 0)
		{
			Iterator iter = contents.keySet().iterator();
			while (iter.hasNext())
			{
				String fullFileName = (String)iter.next();
				UploadFileContent upcontent = contents.get(fullFileName);
				String storeName = rootPath + "/" + upcontent.fileName;
				if (upcontent.contentType != null && (upcontent.contentType.equals("text/xml") || upcontent.contentType.equals("application/x-zip-compressed")))
				{
					storeName = rootPath + "/ObjectImports/" + upcontent.fileName;
					ensureRootPathExists(rootPath + "/ObjectImports");
				}

				String newStoreName;
				
				try
				{
					newStoreName = getNewFilename(storeName, overwrite);					
					writeFile(upcontent.content, newStoreName);		
					
					Object uploadedFilenames = this.getServletContext().getAttribute(UPLOADED_FILENAME);
					if (uploadedFilenames == null || Boolean.FALSE.equals(uploadedFilenames instanceof HashMap))
						uploadedFilenames = new HashMap<String, String>();	
					
					((HashMap<String, String>)uploadedFilenames).put(uniqueID, FilenameUtils.getName(newStoreName));										
					this.getServletContext().setAttribute(UPLOADED_FILENAME, uploadedFilenames);										
				}
				catch(IOException e)
				{
					LocalLogger.error("Failed to write file with name = " + storeName, e);
					throw e;
				} 
				catch (Exception e) 
				{					
					LocalLogger.error(e.getMessage());
					return;
				}
		
				sb.append("<item name=\"");
				sb.append("/" + uploadDir + "/" + FilenameUtils.getName(newStoreName));
				sb.append("\"");
				
				sb.append(" type=\"");
				sb.append(upcontent.contentType);
				sb.append("\" />");					
			}					
		}
		
		for (int i = 0; i < params.size(); i++)
		{			
			UploadFileParams up = params.get(i);			
			UploadFileContent upcontent = contents.get(up.fileName);
			
			String storeName = rootPath + "/" + upcontent.fileName;
			String newFileName = getNewFilename(storeName, up.overwrite);
			
			if (upcontent.content.length > up.maxSize)
			{
				sb.append(" error=\"");
				sb.append("Max file size exceeded. Size of " + up.fileName + " = " + upcontent.content.length + ". Max allowed for control id " + up.controlId + " = " + up.maxSize);
				sb.append("\"");				
				sb.append("</response>");
				
				respond(response, sb);
				return;
			}
			if (up.allowedTypes.indexOf(upcontent.contentType) == -1)
			{
				sb.append(" error=\"");
				sb.append("File content type invalid. Type for " + up.fileName + " = " + upcontent.contentType + ". Allowed types for control id " + up.controlId + " = " + up.allowedTypes);
				sb.append("\"");				
				sb.append("</response>");
				
				respond(response, sb);
				return;
			}	
			writeFile(upcontent.content, newFileName);

			sb.append("<item name=\"");
			sb.append("/" + uploadDir + "/" + upcontent.fileName);
			sb.append("\"");
			
			sb.append(" id=\"");
			sb.append(up.controlId);
			sb.append("\"");
			
			sb.append(" type=\"");
			sb.append(upcontent.contentType);
			sb.append("\" />");					
		}
		
		sb.append("</response>");		
		
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		String encoding = request.getHeader("Accept-Encoding");
		if (encoding != null && encoding.toLowerCase().indexOf("gzip") > -1)
		{
			response.setHeader("Content-Encoding", "gzip");
			GZIPOutputStream gzos = new GZIPOutputStream(response.getOutputStream());
			gzos.write(sb.toString().getBytes("UTF-8"));
			gzos.close();
		}
		else
		{
			Writer writer = new java.io.OutputStreamWriter(response.getOutputStream(), "UTF-8");
			writer.write(sb.toString());
			writer.close();			
		}
	}
		
	private void ensureRootPathExists(String rootPath)
	{
		File file = new File(rootPath);
		if (!file.exists())
		{
			file.mkdirs();
		}		
	}

	private void writeFile(byte[] content, String fileName) throws IOException
	{
		if (LocalLogger.isInfoEnabled())
			LocalLogger.info("Writing File named " + fileName);
		FileOutputStream out = new FileOutputStream(fileName, false);
		out.write(content);
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
	
	private void respond(HttpServletResponse response, StringBuffer sb) throws IOException
	{
		response.setContentType("text/xml");
		javax.servlet.ServletOutputStream out = response.getOutputStream();
		String outMsg = sb.toString();
		System.out.println(outMsg);
		out.print(outMsg);
		out.close();
		if (LocalLogger.isInfoEnabled())
			LocalLogger.info("File Upload Response: " + outMsg);
	}
	
	public synchronized String getNewFilename(String fileName, boolean overwriteExisting)
	{
		File f = new File(fileName);
		if (overwriteExisting || !f.exists()) return fileName;	
		
		int dotIndex = fileName.lastIndexOf(".");
		String ext = fileName.substring(dotIndex);
		int i = 1;
		String newFileName = fileName.substring(0,dotIndex) + "_" + getIntString(i, 5) + ext;
		f = new File(newFileName);
		while (f.exists() && i < 100000)
		{
			i++;
			newFileName = fileName.substring(0,dotIndex) + "_" + getIntString(i, 5) + ext;
			f = new File(newFileName);			
		}
		return newFileName;		
	}
	
	private String getIntString(int val, int size)
	{
		char[] blank = new char[size];
		for (int i = 0; i < size; i++)
		{
			blank[i] = '0';
		}
		String s = new String(blank);		
		String valStr = "" + val;
		if (valStr.length() >= size) return valStr;
		return s.substring(0,s.length() - valStr.length()) + valStr;
	}
	
	private class UploadFileParams
	{
		public String controlId;
		public String fileName = "";
		public boolean overwrite = true;
		public int maxSize = 10000000;
		public String allowedTypes = "gif,jpg,jpeg";
	}
	
	private class UploadFileContent
	{
		public String fileName;
		public String contentType;
		public byte[] content;
	}

	public synchronized String getUploadedFilename(String uniqueID) 
	{
		Object uploadedFilenames = this.getServletContext().getAttribute(UPLOADED_FILENAME);
		if (uploadedFilenames != null && uploadedFilenames instanceof HashMap)
			return ((HashMap<String, String>)this.getServletContext().getAttribute(UPLOADED_FILENAME)).get(uniqueID);
		
		return null;
	}
}
