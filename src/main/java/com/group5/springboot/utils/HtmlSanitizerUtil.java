package com.group5.springboot.utils;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class HtmlSanitizerUtil {
	private static final PolicyFactory POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);


	public static String sanitize(String htmlContent) {
		return POLICY.sanitize(htmlContent);
	}
}