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
			mimeType = "image/png";
		}

		// data:[mimeType];base64,xxxxxxxxxxx
		StringBuffer result = new StringBuffer("data:" + mimeType + ";base64,");
		try (InputStream is = image.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			int len = 0;
			byte[] b = new byte[81920];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			byte[] bytes = baos.toByteArray();
			Base64.Encoder be = Base64.getEncoder();

			byte[] ba = be.encode(bytes);
			String tmp = new String(ba, "UTF-8");
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
			mimeType = "image/png";
		}

		// data:[mimeType];base64,xxxxxxxxxxx
		StringBuffer result = new StringBuffer("data:" + mimeType + ";base64,");
		try (InputStream is = image.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			int len = 0;
			byte[] b = new byte[81920];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			byte[] bytes = baos.toByteArray();
			Base64.Encoder be = Base64.getEncoder();

			byte[] ba = be.encode(bytes);
			String tmp = new String(ba, "UTF-8");
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

	public static String getBaseUrl() {
		URI baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
		return baseUrl.toString();
	}
}