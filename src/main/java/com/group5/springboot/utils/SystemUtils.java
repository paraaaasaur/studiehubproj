package com.group5.springboot.utils;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Base64;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;

import org.springframework.core.io.ClassPathResource;

public class SystemUtils {
	
	//foler設常數
	public final static String PLACE_IMAGE_FOLDER = "C:\\images\\place";
	//pathToBlob預設圖片
	public final static String NO_IMAGE_PATH = "\\static\\images\\NoImage.png";
	
	
	
	
	//給一個檔名，這邊給一個副檔名
	public static String getExtFilename(String filename) {
		return filename.substring(filename.lastIndexOf("."));
	}
	

	// db抓圖片轉base64
	public static String blobToDataProtocol(String mimeType, Blob image) {
		if(image == null || mimeType == null) {
			image = pathToBlob(NO_IMAGE_PATH);
			mimeType = "image/png";	//mimeType直接用常數(為預設圖片)
		}
		
		
		// data:[mimeType];base64,xxxxxxxxxxx
		StringBuffer result = new StringBuffer("data:" + mimeType + ";base64,");
		try (InputStream is = image.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			int len = 0;
			byte[] b = new byte[81920];
//			byte[] b = new byte[is.available()];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			byte[] bytes = baos.toByteArray();
			Base64.Encoder be = Base64.getEncoder(); // 透過getEncoder()回傳物件

			result.append(new String(be.encode(bytes)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	
	// BLOB
		public static Blob inputStreamToBlob(InputStream is) { // MultipartFile轉Blob
			Blob blob = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len = 0;
				byte[] b = new byte[81920];
//				byte[] b = new byte[is.available()];
				while ((len = is.read(b)) != -1) {
					baos.write(b, 0, len);
				}
				byte[] data = baos.toByteArray();
				blob = new SerialBlob(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return blob;
		}


	// BLOB
	public static Blob pathToBlob(String path) {
		Blob blob = null;

		try {
			ClassPathResource cpr = new ClassPathResource(path);
			File file = cpr.getFile();
			if (!file.exists()) {
				return null;
			}
			InputStream is = cpr.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int len = 0;
			byte[] b = new byte[81920];
//			byte[] b = new byte[is.available()];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			byte[] data = baos.toByteArray();
			blob = new SerialBlob(data);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return blob;
	}

	// CLOB
	public static Clob pathToClob(String path) {
		Clob clob = null;

		try {
			ClassPathResource cpr = new ClassPathResource(path);
			File file = cpr.getFile();
			if (!file.exists()) {
				return null;
			}
			InputStream is = cpr.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			CharArrayWriter caw = new CharArrayWriter();

			int len = 0;
			char[] b = new char[81920];
			while ((len = isr.read(b)) != -1) {
				caw.write(b, 0, len);
			}
			char[] data = caw.toCharArray();
			clob = new SerialClob(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clob;
	}
	
	//String轉成Clob
	public static Clob stringToClob(String str) {
		Clob clob = null;

		try {
			char[] data = str.toCharArray();
			clob = new SerialClob(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clob;
	}
	
	
}
