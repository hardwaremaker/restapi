package com.heliumv.tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.HVPDFExporter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrintManager;

public class JasperPrintHelper {	
	public static byte[] asPdf(JasperPrintLP print) throws JRException {
		return asPdfStream(print).toByteArray();
	}
	
	public static byte[] asJpeg(JasperPrintLP print) throws JRException, IOException {
		return asJpegStream(print).toByteArray();
	}
	
	public static byte[] asJpeg(JasperPrintLP print, int dpi) throws JRException, IOException {
		return asJpegStream(print, dpi).toByteArray();
	}

	public static ByteArrayOutputStream asPdfStream(JasperPrintLP print) throws JRException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(100000) ;
		HVPDFExporter exporter = new HVPDFExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print.getPrint());
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

		exporter.exportReport();
		return baos;
	}
	
	public static ByteArrayOutputStream asJpegStream(JasperPrintLP print) throws JRException, IOException {
		int pages = print.getPrint().getPages().size();
		int lastPage = 0;
		if(pages > 0) {
			lastPage = pages - 1;
		}
		BufferedImage img = (BufferedImage)JasperPrintManager.printPageToImage(print.getPrint(), lastPage, 1.0f);	
		int w = img.getWidth();
		int h = img.getHeight();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(100000);
		
		boolean written = ImageIO.write(img, "jpg", baos);	
		return baos;
	}
	
	public static ByteArrayOutputStream asJpegStream(JasperPrintLP print, int dpi) throws JRException, IOException {
		int pages = print.getPrint().getPages().size();
		int lastPage = 0;
		if(pages > 0) {
			lastPage = pages - 1;
		}
		
		BufferedImage img = (BufferedImage)JasperPrintManager.printPageToImage(print.getPrint(), lastPage, 1.0f * dpi / 72);	
		ByteArrayOutputStream baos = new ByteArrayOutputStream(100000);
		
		try {
	        // Image writer 
	        ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpeg").next();
	        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
	        imageWriter.setOutput(ios);

	        // Compression
	        JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
	        jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
	        jpegParams.setCompressionQuality(0.75f);

	        // Metadata (dpi)
	        IIOMetadata data = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(img), jpegParams);
	        IIOMetadataNode tree = (IIOMetadataNode) data.getAsTree("javax_imageio_jpeg_image_1.0");
	        IIOMetadataNode jfif = (IIOMetadataNode) tree.getElementsByTagName("app0JFIF").item(0);
	        jfif.setAttribute("Xdensity", Integer.toString(dpi));
	        jfif.setAttribute("Ydensity", Integer.toString(dpi));
	        jfif.setAttribute("resUnits", "1"); // density is dots per inch	
	        data.setFromTree("javax_imageio_jpeg_image_1.0", tree);

	        // Write and clean up
	        imageWriter.write(null, new IIOImage(img, null, data), jpegParams);
		    imageWriter.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return baos;
	}
}
