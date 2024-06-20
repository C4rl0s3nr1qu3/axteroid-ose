package com.axteroid.ose.server.ubl20.util.xsd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XSDElement {

	private static final Logger LOG = LoggerFactory.getLogger(XSDElement.class);

	private String name;
	private String type;
	private String minOccurs;
	private String length;
	private String converter;
	private String nillable;
	private String maxOccurs;

	private boolean isArray;
	private Field objField;
	private Converter objConverter;
	private XSDComplexType complexType;
	private Method add;

	public void init(Class<?> clazz) throws Exception {
		this.objField = clazz.getDeclaredField(name);
		this.objField.setAccessible(true);

		if (objField.getType().isAssignableFrom(List.class)) {
			this.isArray = true;
			this.add = List.class.getDeclaredMethod("add", Object.class);
		}

		if (converter != null && !converter.equals("")) {
			this.objConverter = converters.get(converter);
		}

		if (this.complexType != null) {
			this.complexType.init();
		}
	}

	public boolean isArray() {
		return isArray;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getLength() {
		return length;
	}

	public XSDComplexType getComplexType() {
		return complexType;
	}

	public void setComplexType(XSDComplexType complexType) {
		this.complexType = complexType;
	}

	public Object convert(String value) throws Exception {
		if (objConverter != null) {
			return objConverter.convert(value);
		}

		return value;
	}

	public void populate(Object obj, String val) throws Exception {
		objField.set(obj, convert(val));
	}

	public void populateObject(Object obj, Object val) throws Exception {
		if (isArray) {
			add.invoke(objField.get(obj), val);
		} else {
			objField.set(obj, val);
		}
	}

	public void populateObject(Object obj, String[] values) throws Exception {
		if (isArray) {
			add.invoke(objField.get(obj), complexType.populate(values));
		} else {
			objField.set(obj, complexType.populate(values));
		}
	}
	
	public Object getValue(Object obj) throws Exception {
		return objField.get(obj);
	}	

	public String objectToString(Object obj, boolean raw) throws Exception {
		if (isArray) {
			StringBuilder stb = new StringBuilder();
			List<?> list = (List<?>) objField.get(obj);

			stb.append("{");
			for (Object object : list) {
				stb.append("[").append(complexType.objectToString(object, raw)).append("]");
			}
			stb.append("}");

			return stb.toString();
		}

		return name + "=" + objField.get(obj);
	}

	public boolean compareObjects(Object obj1, Object obj2) throws Exception {
		if (isArray) {
			List<?> list1 = (List<?>) objField.get(obj1);
			List<?> list2 = (List<?>) objField.get(obj2);

			for (int i = 0; i < list1.size(); i++) {
				if (!complexType.compareObjects(list1.get(i), list2.get(i))) {
					return false;
				}
			}
			return true;
		}

		Object o1 = objField.get(obj1);
		Object o2 = objField.get(obj2);

		if (o1 == null && o2 == null) {
			return true;
		} else if (o1 == null && o2 != null) {
			return false;
		} else if (o1 != null && o2 == null) {
			return false;
		} else if (type.equals("xs:string") && converter == null) {
			String val1 = (String) o1;
			String val2 = (String) o2;

			return val1.trim().equals(val2.trim());
		} else {
			return objField.get(obj1).equals(objField.get(obj2));
		}
	}

	static abstract class Converter {
		abstract Object convert(String value) throws Exception;
	}

	private final static Map<String, Converter> converters = new HashMap<String, Converter>();

	static {
		converters.put("com.ebiz.ce.gateway.sunat.StringConverter", new Converter() {
			@Override
			public Object convert(String value) throws Exception {
				if (StringUtils.isBlank(value)) {
					return null;
				}
				return value.trim();
			}
		});
		converters.put("com.ebiz.ce.gateway.sunat.IntegerConverter", new Converter() {
			@Override
			public Object convert(String value) throws Exception {
				if (StringUtils.isBlank(value)) {
					return null;
				}
				try {
					return new Long(value.trim());
				} catch (Exception e) {
					LOG.error("Error al convertir a numero  el valor <" + value.trim() + ">");
				}
				return null;
			}
		});
		converters.put("com.ebiz.ce.gateway.sunat.DateConverter", new Converter() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			@Override
			public Object convert(String value) throws Exception {
				try {
					if (StringUtils.isBlank(value))
						return null;
					return ((SimpleDateFormat) sdf.clone()).parse(value.trim());
				} catch (ParseException e) {
					LOG.error("Error al convertir a fecha <" + value.trim() + ">");
				}
				return null;
			}
		});
		converters.put("com.ebiz.ce.gateway.sunat.DecimalConverter", new Converter() {
			@Override
			public Object convert(String value) throws Exception {
				if (StringUtils.isBlank(value)) {
					return null;
				}
				try {
					return new BigDecimal(value.trim());
				} catch (Exception e) {
					LOG.error("Error al convertir a numero  el valor <" + value.trim() + ">");
				}
				return null;
			}
		});
		converters.put("com.ebiz.ce.gateway.sunat.DecimalCantidadConverter",
				converters.get("com.ebiz.ce.gateway.sunat.DecimalConverter"));
		converters.put("com.ebiz.ce.gateway.sunat.Decimal10Converter",
				converters.get("com.ebiz.ce.gateway.sunat.DecimalConverter"));
	}

	@Override
	public String toString() {
		return "\n\t\tXSDElement [" + (name != null ? "name=" + name + ", " : "")
				+ (type != null ? "type=" + type + ", " : "")
				+ (minOccurs != null ? "minOccurs=" + minOccurs + ", " : "")
				+ (length != null ? "length=" + length + ", " : "")
				+ (converter != null ? "converter=" + converter + ", " : "")
				+ (nillable != null ? "nillable=" + nillable + ", " : "")
				+ (maxOccurs != null ? "maxOccurs=" + maxOccurs + ", " : "")
				+ (complexType != null ? "\ncomplexType=" + complexType : "") + "]";
	}

}
