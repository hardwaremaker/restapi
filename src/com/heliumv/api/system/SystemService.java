package com.heliumv.api.system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heliumv.tools.StringHelper;

public class SystemService {
	private static Logger log = LoggerFactory.getLogger(SystemService.class) ;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");
	private String barcodeLogfile;
	private boolean initialized;
	
	private String getBarcodeLogfile() {
		if(!initialized) {
			try {
				Context env = (Context) new InitialContext().lookup("java:comp/env") ;
				barcodeLogfile = (String) env.lookup("heliumv.barcode.store");
			} catch(NamingException e) {
			}
			initialized = true;				
		}
		
		return barcodeLogfile;
	}
	
	public void logBarcode(String identity, String barcode, long time) {
		if(StringHelper.isEmpty(barcode)) return;
		
		if(time == 0L) {
			time = System.currentTimeMillis();
		}
		if(identity == null) {
			identity = "";
		}
		String msg = "barcodestore [" + sdf.format(new Date(time)) + "|" + identity + "|" + barcode + "]";

		String store = getBarcodeLogfile();
		if(store != null) {
			try(BufferedWriter w = new BufferedWriter(new FileWriter(new File(store), true))) {
				w.write(sdf.format(new Date()) + " " + msg);
				w.newLine();
			} catch(IOException e) {
				log.error("Couldn't write barcode logfile", e);
			}
		} else {
			log.warn(msg);			
		}		
	}
	
	public void logBarcode(String identity, String barcode) {
		logBarcode(identity, barcode, System.currentTimeMillis());
	}

	public void logBarcode(String barcode, long time) {
		logBarcode("", barcode, time);
	}
	
	public void logBarcode(String barcode) {
		logBarcode("", barcode, System.currentTimeMillis());
	}
}
