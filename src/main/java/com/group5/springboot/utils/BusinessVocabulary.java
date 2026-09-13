package com.group5.springboot.utils;

import java.util.HashMap;
import java.util.Map;

public final class BusinessVocabulary {
	public static Map<String, String> eventtype() {
		Map<String, String> map = new HashMap<>();

		map.put("研討會", "研討會");
		map.put("線下課程", "線下課程");
		map.put("講座", "講座");
		map.put("分享會", "分享會");

		return map;
	}
}
