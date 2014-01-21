/*
 * Copyright (c) 2010-2020 Founder International Co., Ltd. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Founder. You
 * shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the agreements you entered into with Founder.
 */

package utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Miscellaneous object utility methods. Mainly for internal use within the
 * framework;
 */
public abstract class ObjectUtil {

//	private static Map<Class<?>, Integer> javaTypeToSqlTypeMap = new HashMap<Class<?>, Integer>(21);
//	static {
//		/* JDBC 3.0 only - not compatible with e.g. MySQL at present */
//		javaTypeToSqlTypeMap.put(boolean.class, Types.BOOLEAN);
//		javaTypeToSqlTypeMap.put(Boolean.class, Types.BOOLEAN);
//		javaTypeToSqlTypeMap.put(byte.class, Types.TINYINT);
//		javaTypeToSqlTypeMap.put(Byte.class, Types.TINYINT);
//		javaTypeToSqlTypeMap.put(short.class, Types.SMALLINT);
//		javaTypeToSqlTypeMap.put(Short.class, Types.SMALLINT);
//		javaTypeToSqlTypeMap.put(int.class, Types.INTEGER);
//		javaTypeToSqlTypeMap.put(Integer.class, Types.INTEGER);
//		javaTypeToSqlTypeMap.put(long.class, Types.BIGINT);
//		javaTypeToSqlTypeMap.put(Long.class, Types.BIGINT);
//		javaTypeToSqlTypeMap.put(BigInteger.class, Types.BIGINT);
//		javaTypeToSqlTypeMap.put(float.class, Types.FLOAT);
//		javaTypeToSqlTypeMap.put(Float.class, Types.FLOAT);
//		javaTypeToSqlTypeMap.put(double.class, Types.DOUBLE);
//		javaTypeToSqlTypeMap.put(Double.class, Types.DOUBLE);
//		javaTypeToSqlTypeMap.put(BigDecimal.class, Types.DECIMAL);
//		javaTypeToSqlTypeMap.put(java.sql.Date.class, Types.DATE);
//		javaTypeToSqlTypeMap.put(java.sql.Time.class, Types.TIME);
//		javaTypeToSqlTypeMap.put(java.sql.Timestamp.class, Types.TIMESTAMP);
//		javaTypeToSqlTypeMap.put(Blob.class, Types.BLOB);
//		javaTypeToSqlTypeMap.put(Clob.class, Types.CLOB);
//	}
//
//	/**
//	 * Derive a default SQL type from the given Java type.
//	 * 
//	 * @param javaType
//	 *            the Java type to translate
//	 * @return the corresponding SQL type, or <code>null</code> if none found
//	 */
//
//	public static int javaTypeToSqlParameterType(Class<?> javaType) {
//		Integer sqlType = javaTypeToSqlTypeMap.get(javaType);
//		if (sqlType != null) {
//			return sqlType;
//		}
//		if (Number.class.isAssignableFrom(javaType)) {
//			return Types.NUMERIC;
//		}
//		if (isString(javaType)) {
//			return Types.VARCHAR;
//		}
//		if (isDate(javaType) || Calendar.class.isAssignableFrom(javaType)) {
//			return Types.TIMESTAMP;
//		}
//		return Integer.MIN_VALUE;
//	}
//
//	private static Map<String, Class<?>> classMap = new HashMap<String, Class<?>>(22);
//	static {
//		classMap.put("boolean", boolean.class);
//		classMap.put("Boolean", Boolean.class);
//		classMap.put("byte", byte.class);
//		classMap.put("Byte", Byte.class);
//		classMap.put("short", short.class);
//		classMap.put("Short", Short.class);
//		classMap.put("int", int.class);
//		classMap.put("Integer", Integer.class);
//		classMap.put("long", long.class);
//		classMap.put("Long", Long.class);
//		classMap.put("BigInteger", BigInteger.class);
//		classMap.put("float", float.class);
//		classMap.put("Float", Float.class);
//		classMap.put("double", double.class);
//		classMap.put("Double", Double.class);
//		classMap.put("BigDecimal", BigDecimal.class);
//		classMap.put("Date", java.sql.Date.class);
//		classMap.put("Time", java.sql.Time.class);
//		classMap.put("Timestamp", java.sql.Timestamp.class);
//		classMap.put("Blob", Blob.class);
//		classMap.put("Clob", Clob.class);
//		classMap.put("String", String.class);
//	}
//
//	public static Class<?> getClass(String typeName) {
//		if (StringUtil.isNotEmpty(typeName)) {
//			Class<?> clazz = classMap.get(typeName);
//			if (clazz != null) {
//				return clazz;
//			}
//		}
//		return String.class;
//	}
//
//	private static final Map<Class<?>, IConvertor> TYPE_ConvertorS = new HashMap<Class<?>, IConvertor>();
//	static {
//		try {
//			TYPE_ConvertorS.put(int.class, IntegerConvertor.class.newInstance());
//			TYPE_ConvertorS.put(BigDecimal.class, BigDecimalConvertor.class.newInstance());			
//			TYPE_ConvertorS.put(Integer.class, IntegerConvertor.class.newInstance());
//			TYPE_ConvertorS.put(boolean.class, BooleanConvertor.class.newInstance());
//			TYPE_ConvertorS.put(Boolean.class, BooleanConvertor.class.newInstance());
//			TYPE_ConvertorS.put(long.class, LongConvertor.class.newInstance());
//			TYPE_ConvertorS.put(Long.class, LongConvertor.class.newInstance());
//			TYPE_ConvertorS.put(float.class, FloatConvertor.class.newInstance());
//			TYPE_ConvertorS.put(Float.class, FloatConvertor.class.newInstance());
//			TYPE_ConvertorS.put(double.class, DoubleConvertor.class.newInstance());
//			TYPE_ConvertorS.put(Double.class, DoubleConvertor.class.newInstance());
//			TYPE_ConvertorS.put(char.class, CharacterConvertor.class.newInstance());
//			TYPE_ConvertorS.put(Character.class, CharacterConvertor.class.newInstance());
//			TYPE_ConvertorS.put(short.class, ShortConvertor.class.newInstance());
//			TYPE_ConvertorS.put(Short.class, ShortConvertor.class.newInstance());
//			TYPE_ConvertorS.put(byte.class, ByteConvertor.class.newInstance());
//			TYPE_ConvertorS.put(Byte.class, ByteConvertor.class.newInstance());
//			TYPE_ConvertorS.put(String.class, StringConvertor.class.newInstance());
//			TYPE_ConvertorS.put(java.util.Date.class, DateConvertor.class.newInstance());
//			TYPE_ConvertorS.put(java.sql.Date.class, DateConvertor.class.newInstance());
//			TYPE_ConvertorS.put(java.sql.Time.class, DateConvertor.class.newInstance());
//			TYPE_ConvertorS.put(java.sql.Timestamp.class, DateConvertor.class.newInstance());
//			TYPE_ConvertorS.put(Color.class, ColorConvertor.class.newInstance());
//
//		} catch (InstantiationException e) {
//			throw new RuntimeException("init TypeConvertor failed.", e);
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException("init TypeConvertor failed.", e);
//		}
//	}
//
//	public static Object convertValue(Class<?> clazz, Object value) {
//		if (value == null) {
//			return null;
//		}
//		if (clazz.equals(value.getClass())) {
//			return value;
//		}
//		try {
//			IConvertor typeConvertor = TYPE_ConvertorS.get(clazz);
//			if (typeConvertor != null) {
//				return typeConvertor.getValue(value);
//			} else if (typeConvertor == null) {// interface or superclass
//				Class<?>[] clazzs = clazz.getInterfaces();
//				for (Class<?> clazzi : clazzs) {
//					typeConvertor = TYPE_ConvertorS.get(clazzi);
//					if (typeConvertor != null) {
//						return typeConvertor.getValue(value);
//					}
//				}
//				Class<?> superclazz = clazz.getSuperclass();
//				typeConvertor = TYPE_ConvertorS.get(superclazz);
//				if (typeConvertor != null) {
//					return typeConvertor.getValue(value);
//				}
//			}
//		} catch (Exception e) {
//		}
//		return null;
//	}
//
//	public static Object[] convertValue(Class<?> clazz, Object[] value) {
//		if (value == null) {
//			return null;
//		}
//		if (clazz.equals(value.getClass())) {
//			return value;
//		}
//		try {
//			IConvertor typeConvertor = TYPE_ConvertorS.get(clazz);
//			if (typeConvertor != null) {
//				return typeConvertor.getArray(value);
//			} else if (typeConvertor == null) {// interface or superclass
//				Class<?>[] clazzs = clazz.getInterfaces();
//				for (Class<?> clazzi : clazzs) {
//					typeConvertor = TYPE_ConvertorS.get(clazzi);
//					if (typeConvertor != null) {
//						return typeConvertor.getArray(value);
//					}
//				}
//				Class<?> superclazz = clazz.getSuperclass();
//				typeConvertor = TYPE_ConvertorS.get(superclazz);
//				if (typeConvertor != null) {
//					return typeConvertor.getArray(value);
//				}
//			}
//		} catch (Exception e) {
//		}
//		return null;
//	}
//
//	public static class IntegerConvertor extends AbstarctConvertor {
//
//		public Integer getValue(Object value) {
//			return Integer.valueOf(value.toString());
//		}
//	}
//
//	public static class BigDecimalConvertor extends AbstarctConvertor {
//
//		public BigDecimal getValue(Object value) {
//			return BigDecimal.valueOf(Long.parseLong(value.toString()));
//		}
//	}
//	
//	public static class StringConvertor extends AbstarctConvertor {
//
//		public String getValue(Object value) {
//			return value.toString();
//		}
//	}
//
//	public static class LongConvertor extends AbstarctConvertor {
//
//		public Long getValue(Object value) {
//			return Long.valueOf(value.toString());
//		}
//	}
//
//	public static class FloatConvertor extends AbstarctConvertor {
//
//		public Float getValue(Object value) {
//			return Float.valueOf(value.toString());
//		}
//	}
//
//	public static class DoubleConvertor extends AbstarctConvertor {
//
//		public Double getValue(Object value) {
//			return Double.valueOf(value.toString());
//		}
//	}
//
//	public static class BooleanConvertor extends AbstarctConvertor {
//
//		public Boolean getValue(Object value) {
//			if (value.toString().length() > 1) {
//				return Boolean.valueOf(value.toString());
//			} else if (value.toString().length() <= 1) {
//				char c = value.toString().charAt(0);
//				if (c == '1' || c == 'y' || c == 'Y' || c == 't' || c == 'T')
//					return Boolean.TRUE;
//				else
//					return Boolean.FALSE;
//			} else {
//				return Boolean.FALSE;
//			}
//		}
//	}
//
//	public static class CharacterConvertor extends AbstarctConvertor {
//
//		public Character getValue(Object value) {
//			return Character.valueOf(value.toString().charAt(0));
//		}
//	}
//
//	public static class ShortConvertor extends AbstarctConvertor {
//
//		public Short getValue(Object value) {
//			return Short.valueOf(value.toString());
//		}
//	}
//
//	public static class ByteConvertor extends AbstarctConvertor {
//
//		public Byte getValue(Object value) {
//			return Byte.valueOf(value.toString());
//		}
//	}
//
//	public static class DateConvertor extends AbstarctConvertor {
//		public Date getValue(Object value) {
//			return DateUtil.convert(value.toString());
//		}
//	}
//
//	public static class ColorConvertor extends AbstarctConvertor {
//
//		public Color getValue(Object value) {
//			String[] tokens = value.toString().split(",");
//			try {
//				int red = Integer.parseInt(tokens[0]);
//				int green = Integer.parseInt(tokens[1]);
//				int blue = Integer.parseInt(tokens[2]);
//				return new Color(red, green, blue);
//			} catch (Exception e) {
//			}
//			return null;
//		}
//	}
//
//	public static class UnsupportedDataConvertor extends AbstarctConvertor {
//
//		public Object getValue(Object fieldValue) {
//			return null;
//		}
//
//		@Override
//		public Object[] getArray(Object[] value) {
//			return null;
//		}
//
//		@Override
//		public Collection<?> getCollection(Object[] value) {
//			return null;
//		}
//	}
//
//	/**
//	 * Check whether the given value can be treated as a String value.
//	 */
//	public static boolean isString(Class<?> inValueType) {
//		// Consider any CharSequence (including StringBuffer and StringBuilder)
//		// as a String.
//		return (CharSequence.class.isAssignableFrom(inValueType) || StringWriter.class.isAssignableFrom(inValueType));
//	}
//
//	/**
//	 * Check whether the given value is a <code>java.util.Date</code> (but not
//	 * one of the JDBC-specific subclasses).
//	 */
//	public static boolean isDate(Class<?> inValueType) {
//		return (java.util.Date.class.isAssignableFrom(inValueType) && !(java.sql.Date.class.isAssignableFrom(inValueType) || java.sql.Time.class.isAssignableFrom(inValueType) || java.sql.Timestamp.class
//				.isAssignableFrom(inValueType)));
//	}
//
//	public static final String EMPTY_STRING = "";
//
//	public static final String NULL_STRING = "null";
//
//	@SuppressWarnings("rawtypes")
//	public static Boolean isInstance(Class targetClazz, Object obj) {
//		return targetClazz.isInstance(obj);
//	}
//
//
//	public static Boolean isPrimitive(Object obj) {
//		return obj.getClass().isPrimitive();
//	}
//
//	public static Boolean isNumber(Object obj) {
//		return Number.class.isInstance(obj);
//	}
//
//
//	public static Boolean isDate(Object obj) {
//		return Date.class.isInstance(obj) || Calendar.class.isInstance(obj);
//	}
//
//
//	public static Boolean isString(Object obj) {
//		return isInstance(String.class, obj);
//	}
//
//	/**
//	 * Determine whether the given object is an array: either an Object array or
//	 * a primitive array.
//	 * 
//	 * @param obj
//	 *            the object to check
//	 */
//	public static boolean isArray(Object obj) {
//		return ((obj != null) && obj.getClass().isArray());
//	}
//
//	public static boolean isCollection(Object obj) {
//		return ((obj != null) && obj instanceof Collection);
//	}
//
//	public static boolean isMap(Object obj) {
//		return ((obj != null) && obj instanceof Map);
//	}
//
//	/**
//	 * Return whether the given throwable is a checked exception: that is,
//	 * neither a RuntimeException nor an Error.
//	 * 
//	 * @param ex
//	 *            the throwable to check
//	 * @return whether the throwable is a checked exception
//	 * @see java.lang.Exception
//	 * @see java.lang.RuntimeException
//	 * @see java.lang.Error
//	 */
//	public static boolean isCheckedException(Throwable ex) {
//		return !((ex instanceof RuntimeException) || (ex instanceof Error));
//	}
//
//	/**
//	 * Check whether the given exception is compatible with the exceptions
//	 * declared in a throws clause.
//	 * 
//	 * @param ex
//	 *            the exception to checked
//	 * @param declaredExceptions
//	 *            the exceptions declared in the throws clause
//	 * @return whether the given exception is compatible
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static boolean isCompatibleWithThrowsClause(Throwable ex, Class[] declaredExceptions) {
//		if (!isCheckedException(ex)) {
//			return true;
//		}
//		if (declaredExceptions != null) {
//			int i = 0;
//			while (i < declaredExceptions.length) {
//				if (declaredExceptions[i].isAssignableFrom(ex.getClass())) {
//					return true;
//				}
//				i++;
//			}
//		}
//		return false;
//	}
//
//	@SuppressWarnings("rawtypes")
	public static boolean isNull(Object obj) {
		if (obj == null)
			return true;
		if (obj instanceof String) {
			return ((String) obj).isEmpty();
		} else if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		} else if (obj.getClass().isArray()) {
			return Array.getLength( obj) < 1;
		} else if (obj instanceof Map) {
			return ((Map) obj).isEmpty();
		}
		return false;
	}
//
//	public static boolean isNotEmpty(Object obj) {
//		return !isNullOrEmpty(obj);
//	}
//
//	/**
//	 * Determine whether the given charter string is empty: i.e. null
//	 * 
//	 * @return Boolean
//	 */
//	public static Boolean isNullOrEmpty(String target) {
//		return target == null || target.trim().length() < 1;
//	}
//
//	public static Boolean NotEmpty(String target) {
//		return !isNullOrEmpty(target);
//	}
//
//	/**
//	 * Determine whether the given array is empty: i.e. <code>null</code> or of
//	 * zero length.
//	 * 
//	 * @param array
//	 *            the array to check
//	 */
//	public static boolean isNullOrEmpty(Object[] array) {
//		return ((array == null) || (array.length == 0));
//	}
//
//	public static boolean isNotEmpty(Object[] array) {
//		return !isNullOrEmpty(array);
//	}
//
//	/**
//	 * Return <code>true</code> if the supplied Collection is <code>null</code>
//	 * or empty. Otherwise, return <code>false</code>.
//	 * 
//	 * @param collection
//	 *            the Collection to check
//	 * @return whether the given Collection is empty
//	 */
//	@SuppressWarnings("rawtypes")
//	public static boolean isNullOrEmpty(Collection collection) {
//		return ((collection == null) || collection.isEmpty());
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static boolean isNotEmpty(Collection collection) {
//		return !isNullOrEmpty(collection);
//	}
//
//	/**
//	 * Return <code>true</code> if the supplied Map is <code>null</code> or
//	 * empty. Otherwise, return <code>false</code>.
//	 * 
//	 * @param map
//	 *            the Map to check
//	 * @return whether the given Map is empty
//	 */
//	@SuppressWarnings("rawtypes")
//	public static boolean isNullOrEmpty(Map map) {
//		return ((map == null) || map.isEmpty());
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static boolean isNotEmpty(Map map) {
//		return !isNullOrEmpty(map);
//	}
//
//	/*
//	 * public static boolean equals(Object x, Object y) { return (x == y) || ((x
//	 * != null) && (y != null) && x.equals(y)); }
//	 */
//	/**
//	 * Determine if the given objects are equal, returning <code>true</code> if
//	 * both are <code>null</code> or <code>false</code> if only one is
//	 * <code>null</code>.
//	 * <p>
//	 * Compares arrays with <code>Arrays.equals</code>, performing an equality
//	 * check based on the array elements rather than the array reference.
//	 * 
//	 * @param o1
//	 *            first Object to compare
//	 * @param o2
//	 *            second Object to compare
//	 * @return whether the given objects are equal
//	 * @see java.util.Arrays#equals
//	 */
//	public static boolean equals(Object o1, Object o2) {
//		if (o1 == o2) {
//			return true;
//		}
//		if ((o1 == null) || (o2 == null)) {
//			return false;
//		}
//		if (o1.equals(o2)) {
//			return true;
//		}
//		if (isArray(o1) && isArray(o2)) {
//			if ((o1 instanceof Object[]) && (o2 instanceof Object[])) {
//				return Arrays.equals((Object[]) o1, (Object[]) o2);
//			}
//			if ((o1 instanceof boolean[]) && (o2 instanceof boolean[])) {
//				return Arrays.equals((boolean[]) o1, (boolean[]) o2);
//			}
//			if ((o1 instanceof byte[]) && (o2 instanceof byte[])) {
//				return Arrays.equals((byte[]) o1, (byte[]) o2);
//			}
//			if ((o1 instanceof char[]) && (o2 instanceof char[])) {
//				return Arrays.equals((char[]) o1, (char[]) o2);
//			}
//			if ((o1 instanceof double[]) && (o2 instanceof double[])) {
//				return Arrays.equals((double[]) o1, (double[]) o2);
//			}
//			if ((o1 instanceof float[]) && (o2 instanceof float[])) {
//				return Arrays.equals((float[]) o1, (float[]) o2);
//			}
//			if ((o1 instanceof int[]) && (o2 instanceof int[])) {
//				return Arrays.equals((int[]) o1, (int[]) o2);
//			}
//			if ((o1 instanceof long[]) && (o2 instanceof long[])) {
//				return Arrays.equals((long[]) o1, (long[]) o2);
//			}
//			if ((o1 instanceof short[]) && (o2 instanceof short[])) {
//				return Arrays.equals((short[]) o1, (short[]) o2);
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * Determine the class name for the given object.
//	 * <p>
//	 * Returns <code>"null"</code> if <code>obj</code> is <code>null</code>.
//	 * 
//	 * @param obj
//	 *            the object to introspect (may be <code>null</code>)
//	 * @return the corresponding class name
//	 */
//	public static String getClassName(Object obj) {
//		return (obj != null ? obj.getClass().getName() : NULL_STRING);
//	}
//
//	/**
//	 * Return a content-based String representation if <code>obj</code> is not
//	 * <code>null</code>; otherwise returns an empty String.
//	 * <p>
//	 * Differs from {@link ArrayUtil#toString(Object)} in that it returns an
//	 * empty String rather than "null" for a <code>null</code> value.
//	 * 
//	 * @param obj
//	 *            the object to build a display String for
//	 * @return a display String representation of <code>obj</code>
//	 * @see ArrayUtil#toString(Object)
//	 */
//	public static String getDisplayName(Object obj) {
//		if (obj == null) {
//			return EMPTY_STRING;
//		}
//		return ArrayUtil.toString(obj);
//	}
//
//	/**
//	 * Return a String representation of an object's overall identity.
//	 * 
//	 * @param obj
//	 *            the object (may be <code>null</code>)
//	 * @return the object's identity as String representation, or an empty
//	 *         String if the object was <code>null</code>
//	 */
//	public static String getIdentity(Object obj) {
//		return obj == null ? EMPTY_STRING : obj.getClass().getName() + "@" + getHexIdentity(obj);
//	}
//
//	/**
//	 * Return a hex String form of an object's identity hash code.
//	 * 
//	 * @param obj
//	 *            the object
//	 * @return the object's identity code in hex notation
//	 */
//	public static String getHexIdentity(Object obj) {
//		return Integer.toHexString(System.identityHashCode(obj));
//	}
//
//	/**
//	 * Return the same value as <code>{@link Boolean#hashCode()}</code>.
//	 * 
//	 * @see Boolean#hashCode()
//	 */
//	public static int hashCode(boolean bool) {
//		return bool ? 1231 : 1237;
//	}
//
//	/**
//	 * Return the same value as <code>{@link Long#hashCode()}</code>.
//	 * 
//	 * @see Byte#hashCode()
//	 */
//	public static int hashCode(byte value) {
//		return (int) value;
//	}
//
//	/**
//	 * Return the same value as <code>{@link Long#hashCode()}</code>.
//	 * 
//	 * @see Character#hashCode()
//	 */
//	public static int hashCode(char value) {
//		return (int) value;
//	}
//
//	// ---------------------------------------------------------------------
//	// Convenience methods for content-based equality/hash-code handling
//	// ---------------------------------------------------------------------
//	/**
//	 * Return the same value as <code>{@link Double#hashCode()}</code>.
//	 * 
//	 * @see Double#hashCode()
//	 */
//	public static int hashCode(double dbl) {
//		long bits = Double.doubleToLongBits(dbl);
//		return hashCode(bits);
//	}
//
//	/**
//	 * Return the same value as <code>{@link Float#hashCode()}</code>.
//	 * 
//	 * @see Float#hashCode()
//	 */
//	public static int hashCode(float flt) {
//		return Float.floatToIntBits(flt);
//	}
//
//	/**
//	 * Return the same value as <code>{@link Long#hashCode()}</code>.
//	 * 
//	 * @see Long#hashCode()
//	 */
//	public static int hashCode(long lng) {
//		return (int) (lng ^ (lng >>> 32));
//	}
//
//	/**
//	 * Return the same value as <code>{@link Long#hashCode()}</code>.
//	 * 
//	 * @see Short#hashCode()
//	 */
//	public static int hashCode(short value) {
//		return (int) value;
//	}
//
//	/**
//	 * Return the same value as <code>{@link Long#hashCode()}</code>.
//	 * 
//	 * @see Integer#hashCode()
//	 */
//	public static int hashCode(int value) {
//		return value;
//	}
//
//	/**
//	 * Return the same value as <code>{@link Long#hashCode()}</code>.
//	 * @param <T>
//	 * 
//	 * @see Integer#hashCode()
//	 */
//	public  static <T> T ifNullValue(T value, T convertValue) {
//		if (value == null) {
//			return convertValue;
//		}
//		return value;
//	}
//
//	private static Map<String, OP> OPMap = new HashMap<String, OP>(12);
//	static {
//		OPMap.put("EQ", OP.EQ);
//		OPMap.put("NE", OP.NE);
//		OPMap.put("NOTIN", OP.NOTIN);		
//		OPMap.put("IN", OP.IN);
//		OPMap.put("IN", OP.IN);
//		OPMap.put("GE", OP.GE);
//		OPMap.put("LE", OP.LE);
//		OPMap.put("GT", OP.GT);
//		OPMap.put("LT", OP.LT);
//		OPMap.put("LIKE", OP.LIKE);
//		OPMap.put("FLIKE", OP.FLIKE);
//		OPMap.put("ELIKE", OP.ELIKE);
//		OPMap.put("UEMPTY", OP.UEMPTY);		
//		OPMap.put("IS", OP.IS);
//	}
//
//	public static OP getOP(String operation) {
//		if (operation != null && operation.trim().length() > 0) {
//			String key = operation.trim().toUpperCase();
//			if (OPMap.containsKey(key)) {
//				return OPMap.get(key);
//			}
//		}
//		return OP.EQ;
//	}
//	
//	private static Map<Class<?>, BeanMap> cache = new HashMap<>();
//	public static BeanMap getBeanMap(Object object) {
//		if(isNullOrEmpty(object)){
//			return null;
//		}
//		BeanMap beanMap;
//		Class<?> clazz = object.getClass();
//		if (cache.containsKey(clazz)) {
//			beanMap = cache.get(clazz).newInstance(object);
//		} else {
//			beanMap = BeanMap.create(object);
//			cache.put(clazz, beanMap);
//		}
//		Map<String, Object> map = new HashMap<>();
//		for (Object key : beanMap.keySet()) {
//			Object val = beanMap.get(key);
//			if (ObjectUtil.isNotEmpty(val)) {
//				map.put(key.toString(), val);
//			}
//		}
//		return beanMap;
//	}
}
