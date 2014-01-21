
package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

	public static String DATE_FORMAT = "yyyy-MM-dd";

	public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static ObjectMapper objectMapper = new ObjectMapper();

	public static String objectToJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T jsonToObject(String json, Class<T> valueType, String... dateFormat) {
		try {
			if (!ObjectUtil.isNull(dateFormat) && dateFormat.length > 0) {
				objectMapper.setDateFormat(new java.text.SimpleDateFormat(dateFormat[0]));
			} else {
				objectMapper.setDateFormat(new java.text.SimpleDateFormat(JsonUtil.DATE_TIME_FORMAT));
			}
			return objectMapper.readValue(json, valueType);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	public static <T> List<T> jsonToList(String json, Class<T> valueType) {
		try {
			return objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametricType(ArrayList.class, valueType));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	public static <K, T> Map<K, T> jsonToMap(String json, Class<K> keyType, Class<T> valueType) {
		try {
			return objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametricType(HashMap.class, keyType, valueType));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	public static String getIdJson(String id, Integer[] ids) {
		StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append("{\"" + id + "\":\"" + ids[i] + "\"}");
		}
		sb.append("]");
		String str = sb.toString();
		return str;
	}

	public static String getIdJson(String id, String[] ids) {
		StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append("{\"" + id + "\":\"" + ids[i] + "\"}");
		}
		sb.append("]");
		String str = sb.toString();
		return str;
	}
}
