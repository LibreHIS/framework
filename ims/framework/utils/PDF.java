package ims.framework.utils;

import ims.framework.enumerations.ImageType;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.*;
import javax.management.RuntimeErrorException;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

import java.awt.image.*;

public class PDF 
{
	public ArrayList<OutputStream> convertToImages(byte[] inputBuffer, ImageType outputType, int dpi, int scalingFactor) throws IOException 
	{
		return convertToImages(inputBuffer, outputType, dpi, scalingFactor, -1);
	}
	public ArrayList<OutputStream> convertToImages(byte[] inputBuffer, ImageType outputType, int dpi) throws IOException 
	{
		return convertToImages(inputBuffer, outputType, dpi, 100, -1);
	}
	public ArrayList<OutputStream> convertToImages(byte[] inputBuffer, ImageType outputType) throws IOException 
	{
		return convertToImages(inputBuffer, outputType, 300, 100, -1);
	}
	public ArrayList<OutputStream> convertToImages(byte[] inputBuffer) throws IOException 
	{
		return convertToImages(inputBuffer, ImageType.JPG, 300, 100, -1);
	}
	public ArrayList<OutputStream> convertToImages(byte[] inputBuffer, ImageType outputType, int dpi, int scalingFactor, int maxDimension) throws IOException 
	{
		if(inputBuffer == null || inputBuffer.length == 0)
			return null;
		
		long startTime;
		
		ArrayList<OutputStream> outputStreams = new ArrayList<OutputStream>();		
		PdfDecoder decode_pdf = new PdfDecoder(true);
		
		startTime = System.currentTimeMillis();
		System.out.println("");
		System.out.println("*********************************");
		System.out.println("** Start PDF2Images conversion **");
		System.out.println("*********************************");
		
		try
		{
			FontMappings.setFontReplacements();
		  	decode_pdf.useHiResScreenDisplay(true);
		  	
		  	decode_pdf.setExtractionMode(dpi);
		  	decode_pdf.openPdfArray(inputBuffer);
	      
		  	int numPages = decode_pdf.getPageCount();
		  	System.out.println("* Convert " +  numPages + " pdf pages to " + outputType.toString() );			
		  	
		  	for (int i = 0; i < numPages; i++) 
		  	{	
		  		long pageConversionStartTime = System.currentTimeMillis();
		  		
		  		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();      	
		  		BufferedImage image_to_save = decode_pdf.getPageAsImage(i+1);
	      		
	     	  	if(scalingFactor !=100 || maxDimension != -1)
	     	  	{
	     	  		int newWidth = image_to_save.getWidth() * scalingFactor/100;
		       	    int newHeight = image_to_save.getHeight() * scalingFactor/100;
		
		       	    Image scaledImage;
		       	    if(maxDimension != -1 && (newWidth > maxDimension || newHeight > maxDimension))
		       	    {
		       	        if(newWidth > newHeight)
		       	        {
		       	            newWidth = maxDimension;
		       	            scaledImage= image_to_save.getScaledInstance(newWidth,-1,BufferedImage.SCALE_SMOOTH);
		       	        }
		       	        else 
		       	        {
		       	            newHeight = maxDimension;
		       	            scaledImage= image_to_save.getScaledInstance(-1,newHeight,BufferedImage.SCALE_SMOOTH);
		       	        }
		       	    } 
		       	    else 
		       	    {
		       	        scaledImage= image_to_save.getScaledInstance(newWidth,-1,BufferedImage.SCALE_SMOOTH);
		       	    }
		
		  	        image_to_save = new BufferedImage(scaledImage.getWidth(null),scaledImage.getHeight(null) , BufferedImage.TYPE_INT_RGB);
		
		  	        Graphics2D g2 = image_to_save.createGraphics();
		       	    g2.drawImage(scaledImage, 0, 0, null);	       	      	       	    
	     	  	}
	     	  	
	     	  	ImageIO.write((RenderedImage)image_to_save, outputType.toString(), outputStream);	
	     	  	outputStreams.add(outputStream);	     	  	
	     	  	System.out.println("* Page " + (i+1)  + " conversion time: " + (System.currentTimeMillis() - pageConversionStartTime)  + " ms");
		  	}	      		  	
		}
		catch (PdfException exception)
		{
			throw new RuntimeErrorException(null, exception.getMessage()); 
		}
	  	finally
	  	{
	  		decode_pdf.closePdfFile();	  		
	  	}
		
  		System.out.println("* Total  conversion time: " +  (System.currentTimeMillis()  - startTime) + " ms");
  		System.out.println("*********************************");
  		System.out.println("");
  		
	  	return outputStreams;
	}	
}
