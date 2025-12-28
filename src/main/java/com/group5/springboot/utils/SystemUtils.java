package com.group5.springboot.utils;

import java.io.*;
import java.net.URI;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Base64;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class SystemUtils {

	// db抓圖片轉base64
	public static String blobToDataProtocol(String mimeType, Blob image) {
		if (image == null || mimeType == null) {
			image = pathToBlob(ResourceLocationResolver.USER_NO_IMAGE_URL);
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

	// fixme: same as above except "method name" and "line 2 in the method body"
	// db抓圖片轉base64
	public static String blobToDataProtocolForQuestion(String mimeType, Blob image) {
		if (image == null || mimeType == null) {
			image = pathToBlob(ResourceLocationResolver.QUESTION_NO_IMAGE_URL);
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

//	public static Blob pathToBlob(Path srcPath) { // 因為用ClassPath的resource，所以用String
//		if (Files.notExists(srcPath)) {
//			return null;
//		}
//
//		Blob blob = null;
//		byte[] data;
//		try {
//			data = Files.readAllBytes(srcPath);
//			blob = new SerialBlob(data);
//		} catch (IOException | SQLException e) {
//			throw new RuntimeException("Error converting file to Blob", e);
//		}
//		return blob;
//	}

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

	public static String getBaseUrl() {
		URI baseUrl= ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
		return baseUrl.toString();
	}
}
