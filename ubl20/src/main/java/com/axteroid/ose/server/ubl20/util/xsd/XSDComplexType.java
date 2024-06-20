package com.axteroid.ose.server.ubl20.util.xsd;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axteroid.ose.server.ubl20.gateway.batch.EDocumentoCliente;
import com.axteroid.ose.server.ubl20.gateway.batch.EDocumentoItemCliente;

public class XSDComplexType {

	private static final Logger LOG = LoggerFactory
			.getLogger(XSDComplexType.class);

	private static final int[][] auxiliarsMap = { { 100, 40, 500, 250 },
			{ 10, 20, 5, 25 } };
	private static final int[][] auxiliarsItemMap = { { 100, 40, 500, 250 },
			{ 10, 10, 5, 10 } };

	private String name;
	private String subclass;
	private String length;
	private List<XSDElement> elements;

	private Class<?> objClass;
	private int numFields;

	public void init() throws Exception {
		LOG.info("init complex type: " + name);

		objClass = Class.forName(subclass);

		for (XSDElement ele : elements) {
			ele.init(objClass);
			numFields += ele.isArray() ? 0 : 1;
		}
	}

	public Class<?> getObjClass() {
		return objClass;
	}

	public String getName() {
		return name;
	}

	public void setElements(List<XSDElement> elements) {
		this.elements = elements;
	}

	public List<XSDElement> getElements() {
		return this.elements;
	}

	public int getNumFieldsInRaw() {
		return numFields;
	}

	public String getLength() {
		return length;
	}

	public Object populate(String[] values) throws Exception {
		Object obj = null;
		int pos = 0;

		if (numFields != values.length) {
			throw new Exception("Cantidad de campos del archivo ["
					+ values.length + "]no coincide "
					+ "con cantidad de campos del xsd [" + numFields + "]. "
					+ "Tal vez haya un error en la version.");
		}

		try {
			obj = objClass.newInstance();

			for (XSDElement el : elements) {
				if (!el.isArray()) {
					el.populate(obj, values[pos++]);
				}
			}

			if (obj instanceof EDocumentoCliente) {
				eraseAuxiliars(objClass, obj, auxiliarsMap);
			} else if (obj instanceof EDocumentoItemCliente) {
				eraseAuxiliars(objClass, obj, auxiliarsItemMap);
			}
		} catch (IndexOutOfBoundsException iobe) {
			throw new Exception(
					"Archivo tiene menos campos que XSD: archivo[" + (pos - 1)
							+ "], xsd[" + elements.size() + "]");
		}

		return obj;
	}

	/**
	 * Limpia campos auxiliares si el codigo o texto no es enviado
	 * 
	 * @param clazz
	 *            Class representation
	 * @param obj
	 *            instance of class representation
	 * @param map
	 *            auxiliar's fields quantity
	 * @throws Exception
	 */
	public void eraseAuxiliars(Class<?> clazz, Object obj, int[][] map)
			throws Exception {
		for (int j = 0; j < map[0].length; j++) {
			for (int i = 1; i <= map[1][j]; i++) {
				Field key = clazz.getDeclaredField("codigoAuxiliar"
						+ map[0][j] + "_" + i);
				Field val = clazz.getDeclaredField("textoAuxiliar"
						+ map[0][j] + "_" + i);

				key.setAccessible(true);
				val.setAccessible(true);

				String strKey = (String) key.get(obj);
				String strVal = (String) val.get(obj);

				if ((strKey == null || strKey.trim().isEmpty())
						&& (strVal != null && !strVal.trim().isEmpty())) {
					val.set(obj, "");
				}

				if ((strKey != null && !strKey.trim().isEmpty())
						&& (strVal == null || strVal.trim().isEmpty())) {
					key.set(obj, "");
				}
			}
		}
	}

	public void populateObject(Object obj, String[] values, String field)
			throws Exception {
		XSDElement element = getElementObject(field);

		if (element == null) {
			throw new Exception("Field " + name + " not found in class "
					+ subclass);
		}

		element.populateObject(obj, values);
	}

	public XSDElement getElement(String name) {
		for (XSDElement ele : elements) {
			if (ele.getName().equals(name)) {
				return ele;
			}
		}
		return null;
	}

	private XSDElement getElementObject(String name) {
		for (XSDElement ele : elements) {
			if (ele.isArray() && ele.getName().equals(name)) {
				return ele;
			}
		}
		return null;
	}

	public String objectToString(Object obj, boolean raw) throws Exception {
		StringBuilder stb = new StringBuilder();

		for (XSDElement ele : elements) {
			stb.append(raw ? "" : "\n").append(ele.objectToString(obj, raw))
					.append(",");
		}

		stb.delete(stb.length() - 1, stb.length());

		return stb.toString();
	}

	public boolean compareObjects(Object obj1, Object obj2) throws Exception {
		for (XSDElement ele : elements) {
			if (!ele.compareObjects(obj1, obj2)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "XSDComplexType [name=" + name + ", subclass=" + subclass
				+ (length == null ? "" : ", length=" + length)
				+ ", \n\telements=" + elements + "]";
	}

}