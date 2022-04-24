package de.ngloader.synced.util;

public class StringUtil {

	public static String justify(String str) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String sub : str.split("_")) {
			if (stringBuilder.length() != 0) {
				stringBuilder.append(" ");
			}
			stringBuilder.append(sub.substring(0, 1).toUpperCase() + sub.substring(1).toLowerCase());
		}
		return stringBuilder.toString();
	}
}