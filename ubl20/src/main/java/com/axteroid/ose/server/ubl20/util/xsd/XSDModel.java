package com.axteroid.ose.server.ubl20.util.xsd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XSDModel {

	private static final Logger LOG = LoggerFactory.getLogger(XSDModel.class);

	private String nameXsd;

	private String name;
	private String type;
	private String delimiter;
	private String lineSeparator;

	private XSDComplexType complexType;

	public XSDModel(String nameXsd) {
		this.nameXsd = nameXsd;
	}

	public void init() throws Exception {
		LOG.info("init xsd document: " + nameXsd);

		this.complexType.init();
	}

	public String getNameXsd() {
		return nameXsd;
	}

	public void setNameXsd(String nameXsd) {
		this.nameXsd = nameXsd;
	}

	public String getType() {
		return type;
	}

	public XSDComplexType getComplexType() {
		return this.complexType;
	}

	public void setComplexType(XSDComplexType complexType) {
		this.complexType = complexType;
	}

	public Object populate(String[] fields) throws Exception {
		return this.complexType.populate(fields);
	}

	public void populateObject(Object temp, String[] values, String objName) throws Exception {
		if (temp == null) {
			return;
		}

		this.complexType.populateObject(temp, values, objName);
	}

	public String objectToString(Object obj, boolean raw) throws Exception {
		return complexType.objectToString(obj, raw);
	}
	
	public boolean compareObjects(Object obj1, Object obj2) throws Exception {	
		return complexType.compareObjects(obj1, obj2);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("XSDModel [nameXsd=");
		builder.append(nameXsd);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", delimiter=");
		builder.append(delimiter);
		builder.append(", lineSeparator=");
		builder.append(lineSeparator.charAt(0));
		builder.append(", \ncomplexType=");
		builder.append(complexType);
		builder.append("]");
		return builder.toString();
	}

}