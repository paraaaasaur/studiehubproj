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

public class SystemUtilsNickUse {
//建立此檔案，使用其他預設圖片
	
	// foler設常數
	public static final String PLACE_IMAGE_FOLDER = "C:\\images\\place";
	// pathToBlob 預設圖片 > 更改為題庫用預設圖片
	public static final String NO_IMAGE_PATH = "\\static\\images\\NoQuestionImage.png";
	
	// 題庫:上傳檔案資料夾
	public static final String QUESTION_FILE_FOLDER = "C:\\images\\question";

	// 給一個檔名，這邊給一個副檔名
	public static String getExtFilename(String filename) {
		// System.out.println("filename= " + filename);
		int n = filename.lastIndexOf(".");
		if (n >= 0) {
			return filename.substring(n);
		} else {
			return null;
		}
		// return filename.substring(filename.lastIndexOf("."));
	}
	public static String getFilename(String filename) {
		// System.out.println("filename= " + filename);
		int n = filename.lastIndexOf(".");
		if (n >= 0) {
			return filename.substring(0, n);
		} else {
			return null;
		}
		// return filename.substring(filename.lastIndexOf("."));
	}

	// db抓圖片轉base64
	public static String blobToDataProtocol(String mimeType, Blob image) {
		if (image == null || mimeType == null) {
			image = pathToBlob(NO_IMAGE_PATH);
			mimeType = "image/png"; // mimeType直接用常數(因為預設圖是固定的)
		}

		// data:[mimeType];base64,xxxxxxxxxxx
		StringBuffer result = new StringBuffer("data:" + mimeType + ";base64,");
		try (InputStream is = image.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			int len = 0;
			byte[] b = new byte[81920];
//				byte[] b = new byte[is.available()];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len); // 此敘述的口訣: Array.Offset.Length.
			}
			byte[] bytes = baos.toByteArray();
			Base64.Encoder be = Base64.getEncoder(); // 透過getEncoder()回傳物件 (Encoder是Base64的inner class)

			byte[] ba = be.encode(bytes);
			String tmp = new String(ba, "UTF-8");
			// System.out.println(tmp);
			result.append(tmp);

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
//					byte[] b = new byte[is.available()];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len); // 此敘述的口訣: Array.Offset.Length.
			}
			byte[] data = baos.toByteArray();
			blob = new SerialBlob(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blob;
	}

	// BLOB
	public static Blob pathToBlob(String path) { // 因為用ClassPath的resource，所以用String
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
//				byte[] b = new byte[is.available()];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len); // 此敘述的口訣: Array.Offset.Length.
			}
			byte[] data = baos.toByteArray();
			blob = new SerialBlob(data);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return blob;
	}

	// CLOB
	public static Clob pathToClob(String path) { // 因為用ClassPath的resource，所以用String
		Clob clob = null;

		try {
			ClassPathResource cpr = new ClassPathResource(path);
			File file = cpr.getFile();
			if (!file.exists()) {
				return null;
			}
			InputStream is = cpr.getInputStream();
			InputStreamReader isr = new InputStreamReader(is); // 變文字的話要用read，inputStream是讀位元，現在改讀字元

//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CharArrayWriter caw = new CharArrayWriter();

			int len = 0;
			char[] b = new char[81920];
//				char[] b = new char[is.available()];
			while ((len = isr.read(b)) != -1) {
				caw.write(b, 0, len); // 此敘述的口訣: Array.Offset.Length.
			}
			char[] data = caw.toCharArray();
			clob = new SerialClob(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clob;
	}

	// String轉成Clob
	public static Clob stringToClob(String str) { // 因為用ClassPath的resource，所以用String
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
