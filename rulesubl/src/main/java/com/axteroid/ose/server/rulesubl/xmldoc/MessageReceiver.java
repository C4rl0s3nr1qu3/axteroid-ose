package com.axteroid.ose.server.rulesubl.xmldoc;

import java.util.ArrayList;
import java.util.List;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.om.NamespaceBinding;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.SchemaType;
import net.sf.saxon.type.SimpleType;

public class MessageReceiver implements Receiver {

	public List<String> warnings = new ArrayList<String>();

	@Override
	public String getSystemId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPipelineConfiguration(PipelineConfiguration pipe) {
		// TODO Auto-generated method stub

	}

	@Override
	public PipelineConfiguration getPipelineConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSystemId(String systemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open() throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument(int properties) throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endDocument() throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUnparsedEntity(String name, String systemID, String publicID) throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startElement(NodeName elemName, SchemaType typeCode, Location location, int properties) throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void namespace(NamespaceBinding namespaceBinding, int properties) throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void attribute(NodeName attName, SimpleType typeCode, CharSequence value, Location location, int properties) throws XPathException {
		// TODO Auto-generated method stub
	}

	@Override
	public void startContent() throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement() throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void characters(CharSequence chars, Location location, int properties) throws XPathException {
		warnings.add(chars.toString());
	}

	@Override
	public void processingInstruction(String name, CharSequence data, Location location, int properties) throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void comment(CharSequence content, Location location, int properties) throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws XPathException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean usesTypeAnnotations() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<String> getWarnings() {
		return warnings;
	}

}
