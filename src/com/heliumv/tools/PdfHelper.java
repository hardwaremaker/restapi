package com.heliumv.tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PdfHelper {
	public static byte[] asPng(byte[] pdf) throws IOException {
		PDDocument doc = PDDocument.load(pdf);
		int numPages = doc.getNumberOfPages();
		
		PDFRenderer renderer = new PDFRenderer(doc);
		BufferedImage image = renderer.renderImageWithDPI(
				numPages - 1, 200, ImageType.ARGB);
		doc.close();

		int w = image.getWidth();
		int h = image.getHeight();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(100000);
		boolean written = ImageIO.write(image, "png", baos);
		
		return written ? baos.toByteArray() : null;
	}
}
