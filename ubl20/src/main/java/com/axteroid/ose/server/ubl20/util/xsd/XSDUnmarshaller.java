package com.axteroid.ose.server.ubl20.util.xsd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XSDUnmarshaller<T> {

	private static final Logger LOG = LoggerFactory.getLogger(XSDUnmarshaller.class);

	private static final Map<String, XSDModel> cache = new ConcurrentHashMap<String, XSDModel>();

	private XSDModel xsdModel;

	public XSDUnmarshaller(XSDModel xsdModel) {
		this.xsdModel = xsdModel;
	}

	public XSDModel getXSDModel() {
		return xsdModel;
	}

	public static <U> XSDUnmarshaller<U> newInstance(String xsd) throws Exception {
		if (!cache.containsKey(xsd)) {
			cache.put(xsd, parseXSD(xsd));
		}

		return new XSDUnmarshaller<U>(cache.get(xsd));
	}

	public static XSDModel parseXSD(String nameXsd) throws Exception {
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document dom;

		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();

		dom = db.parse(XSDUnmarshaller.class.getResourceAsStream("/xsd/" + nameXsd));

		return parseDocument(nameXsd, dom.getDocumentElement());
	}

	private static XSDModel parseDocument(String nameXsd, Element el) throws Exception {
		Map<String, XSDComplexType> mapOfComplexType = new Hashtable<String, XSDComplexType>();
		XSDModel model = new XSDModel(nameXsd);
		NodeList elements = el.getChildNodes();
		Node node;

		for (int i = 0; i < elements.getLength(); i++) {
			node = elements.item(i);

			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			if ("xs:element".equals(node.getNodeName())) {
				setAttributes(model, node.getAttributes());
			}

			if ("xs:complexType".equals(node.getNodeName())) {
				setComplexType(mapOfComplexType, node);
			}
		}

		model.setComplexType(mapOfComplexType.get(model.getType()));

		for (XSDElement element : model.getComplexType().getElements()) {
			if (mapOfComplexType.containsKey(element.getType())) {
				element.setComplexType(mapOfComplexType.get(element.getType()));
			}
		}

		model.init();

		return model;
	}

	private static void setComplexType(Map<String, XSDComplexType> mapOfComplexType, Node node) throws Exception {

		XSDComplexType xsdComplexType = setAttributes(new XSDComplexType(), node.getAttributes());

		NodeList children = node.getChildNodes();
		List<XSDElement> elements = new ArrayList<XSDElement>();

		for (int j = 0; j < children.getLength(); j++) {
			Node child = children.item(j);

			if (child.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (!child.getNodeName().equals("xs:sequence")) {
				continue;
			}

			NodeList nodeElements = child.getChildNodes();

			for (int k = 0; k < nodeElements.getLength(); k++) {
				Node ele = nodeElements.item(k);

				if (ele.getNodeType() == Node.ELEMENT_NODE) {
					elements.add(setAttributes(new XSDElement(), ele.getAttributes()));
				}
			}
		}

		xsdComplexType.setElements(elements);

		mapOfComplexType.put(xsdComplexType.getName(), xsdComplexType);
	}

	private static <S> S setAttributes(S s, NamedNodeMap map) throws Exception {
		Field field;
		Node item;
		String name;

		for (int i = 0; i < map.getLength(); i++) {
			item = map.item(i);
			name = item.getNodeName();

			if (name.startsWith("jrb:")) {
				name = name.substring(4);
			}

			field = s.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(s, item.getNodeValue());
		}

		return s;
	}

	public static XSDModel getModelFromCache(String model) {
		return cache.get(model);
	}

	public List<T> unmarshallAll(File file, Charset charset) throws Exception {
		List<T> result = new ArrayList<T>();
		for (Iterator<T> iter = unmarshall(file, charset); iter.hasNext();) {
			result.add(iter.next());
		}
		return result;
	}

	public Iterator<T> unmarshall(File file, Charset charset) throws Exception {
		return new UnmarshallerIterator<T>(file, charset, xsdModel);
	}

	private final class UnmarshallerIterator<V> implements Iterator<V> {

		private File file;
		private Charset charset;
		private XSDModel model;

		private InputStream is;
		private InputStreamReader isr;
		private BufferedReader buf;

		private V currentRecord;
		private boolean buffEmpty;

		private int numLine;
		private String cacheLine;

		public UnmarshallerIterator(File file, Charset charset, XSDModel model) {
			super();
			this.file = file;
			this.charset = charset;
			this.model = model;
		}

		@SuppressWarnings("unchecked")
		private void readNext() {
			if (currentRecord != null) {
				return;
			}

			if (buffEmpty) {
				return;
			}

			try {
				if (buf == null) {
					initialize();
				}

				IND ind;
				String line;
				String[] fields;

				if (cacheLine != null) {
					try {
						currentRecord = (V) model.populate(cacheLine.split("\\|", -1));
					} catch (Exception e) {
						LOG.error(file.getName() + " ==> error en linea " + numLine + ": " + e);
						currentRecord = null;
					}
					cacheLine = null;
				}

				while ((line = buf.readLine()) != null) {
					if (line.trim().isEmpty()) {
						continue;
					}
					try {
						fields = line.split("\\|", -1);
						ind = IND.getIndicator(fields[0]);

						if (ind == IND.CABECERA) {
							if (currentRecord == null) {
								currentRecord = (V) model.populate(fields);
							} else {
								cacheLine = line;
								numLine++;								
								break;
							}
						} else {
							model.populateObject(currentRecord, fields, ind.name().toLowerCase());
						}
					} catch (Exception e) {
						String error = file.getName() + " ==> error en linea " + numLine + ": " + e;

						error += currentRecord == null ? ""
								: "\n doc. invalido [" + model.objectToString(currentRecord, true) + "]: "
										+ "error al procesar item";

						LOG.error(error);
						currentRecord = null;
						cacheLine = null;
					}
					numLine++;
				}

				if (line == null) {
					close();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private void initialize() throws Exception {
			this.is = new FileInputStream(file);
			this.isr = new InputStreamReader(is, charset);
			this.buf = new BufferedReader(isr);

			this.buf.mark(4);

			if ('\ufeff' != this.buf.read()) {
				this.buf.reset();
			}
		}

		private void close() throws IOException {
			this.buffEmpty = true;

			if (buf != null) {
				buf.close();
				buf = null;
			}
			if (isr != null) {
				isr.close();
				isr = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
		}

		@Override
		public boolean hasNext() {
			readNext();
			return currentRecord != null;
		}

		@Override
		public V next() {
			readNext();
			if (currentRecord == null) {
				throw new NoSuchElementException();
			}
			V current = (V) currentRecord;
			currentRecord = null;
			return current;
		}

		@Override
		public void remove() {
		}

	}

	private static enum IND {
		CABECERA("C"),
		ITEMS("D"),
		ANTICIPOS("A"),
		REFERENCIAS("R");

		private String val;

		private IND(String val) {
			this.val = val;
		}

		static IND getIndicator(String val) throws Exception {
			for (IND ind : values()) {
				if (val.equals(ind.val)) {
					return ind;
				}
			}
			throw new Exception("**indicador invalido [" + val + "]");
		}
	}

}
