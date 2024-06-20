package com.axteroid.ose.server.sunat.core;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMSerializer {
    /**
     * The declaration omitting defaults to <code>false</code>.
     */
    public static final boolean DEFAULT_DECLARATION_OMITTING = false;

	/**
	 * The version defaults to <code>1.0</code>.
	 */
	public static final String DEFAULT_VERSION = "1.0";

    /**
     * The standalone flag defaults to <code>false</code>.
     */
    public static final boolean DEFAULT_STANDALONE = false;

	/**
	 * The encoding defaults to <code>UTF-8</code>.
	 */
	public static final String DEFAULT_ENCODING = "ISO-8859-1";

	/**
	 * The indentation defaults to <code>\t</code>.
	 */
	public static final String DEFAULT_INDENTATION = "\t";

	/**
	 * The line breaking defaults to <code>true</code>.
	 */
	public static final boolean DEFAULT_LINE_BREAKING = true;

	/**
	 * The output stream defaults to <code>System.out</code>.
	 */
	public static final OutputStream DEFAULT_OUTPUT_STREAM = System.out;

	/**
	 * The whitespace ignoring defaults to <code>true</code>.
	 */
	public static final boolean DEFAULT_WHITESPACE_IGNORING = true;

	private OutputStream outputStream = DEFAULT_OUTPUT_STREAM;
	private PrintWriter writer = null;
    private boolean declarationOmitting = DEFAULT_DECLARATION_OMITTING;
    private String version = DEFAULT_VERSION;
    private String encoding = DEFAULT_ENCODING;
    private boolean standalone = DEFAULT_STANDALONE;
	private String indentation = DEFAULT_INDENTATION;
	private boolean lineBreaking = DEFAULT_LINE_BREAKING;
	private boolean whitespaceIgnoring = DEFAULT_WHITESPACE_IGNORING;
	private int depth = 0;

	/**
	 * Creates a new DOM serializer.
	 */
	public DOMSerializer() {
	}

    /**
     * Sets the declaration omitting mode.
     *
     * @param declarationOmitting the declaration omitting mode.
     */
    public void setDeclarationOmitting (boolean declarationOmitting) {
        this.declarationOmitting = declarationOmitting;
    }

    /**
     * Returns the declaration omitting mode.
     *
     * @return the declaration omitting mode.
     */
    public boolean isDeclarationOmitting () {
        return this.declarationOmitting;
    }

	/**
	 * Sets the encoding type.
	 *
	 * @param encoding the encoding type.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Returns the encoding type.
	 *
	 * @return the encoding type.
	 */
	public String getEncoding() {
		return this.encoding;
	}

	/**
	 * Sets the indentation string.
	 *
	 * @param indentation the indentation string.
	 */
	public void setIndentation(String indentation) {
		this.indentation = indentation;
	}

	/**
	 * Returns the indentation string.
	 *
	 * @return the indentation string.
	 */
	public String getIndentation() {
		return this.indentation;
	}

	/**
	 * Sets the line breaking mode.
	 *
	 * @param lineBreaking the line breaking mode.
	 */
	public void setLineBreaking(boolean lineBreaking) {
		this.lineBreaking = lineBreaking;
	}

	/**
	 * Returns the line breaking mode.
	 *
	 * @return the line breaking mode.
	 */
	public boolean isLineBreaking() {
		return this.lineBreaking;
	}

	/**
	 * Sets the output stream for serialization.
	 *
	 * @param outputStream the output stream for serialization.
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * Returns the output stream for serialization.
	 *
	 * @return the output stream for serialization.
	 */
	public OutputStream getOutputStream() {
		return this.outputStream;
	}

    /**
     * Sets the standalone flag.
     *
     * @param standalone the standalone flag.
     */
    public void setStandalone (boolean standalone) {
        this.standalone = standalone;
    }

    /**
     * Returns the standalone flag.
     *
     * @return the standalone flag.
     */
    public boolean isStandalone () {
        return this.standalone;
    }

    /**
     * Sets the version string.
     *
     * @param version the version string.
     */
    public void setVersion (String version) {
        this.version = version;
    }

    /**
     * Returns the version string.
     *
     * @return the version string.
     */
    public String getVersion () {
        return this.version;
    }

	/**
	 * Sets the whitespace ignoring mode.
	 *
	 * @param whitespaceIgnoring the whitespace ignoring mode.
	 */
	public void setWhitespaceIgnoring(boolean whitespaceIgnoring) {
		this.whitespaceIgnoring = whitespaceIgnoring;
	}

	/**
	 * Returns the whitespace ignoring mode.
	 *
	 * @return the whitespace ignoring mode.
	 */
	public boolean isWhitespaceIgnoring() {
		return this.whitespaceIgnoring;
	}

	/**
	 * Serializes the specified node.
	 *
	 * @param node the node to be serialized.
	 */
	public void serialize(Node node) throws UnsupportedEncodingException {
		// create a new writer
		this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.outputStream,
		                                                                        this.encoding)), false);

		// serialize node
		this.writeSerialization(node);

		// flush and release writer
		this.writer.flush();
		this.writer = null;
	}

	private void writeIndentation() {
		if (this.indentation != null) {
			for (int index = 0; index < this.depth; index++) {
				this.writer.write(this.indentation);
			}
		}
	}

	private void writeLineBreaking() {
		if (this.lineBreaking) {
			this.writer.write("\r\n"); //.println();
		}
	}

	private void writeNormalization(String text) {
		if ((text == null) || (text.length() == 0)) {
			return;
		}
		char c;
		for (int index = 0; index < text.length(); index++) {
		    c = text.charAt(index);

			switch (c) {
				case '<': {
					this.writer.write("&lt;");

					break;
				}

				case '>': {
					this.writer.write("&gt;");

					break;
				}

				case '&': {
					this.writer.write("&amp;");

					break;
				}

				case '\'': {
					this.writer.write("&apos;");

					break;
				}

				case '"': {
					this.writer.write("&quot;");

					break;
				}
//				case 241: { // enhe minuscula
//					this.writer.write("&#xf1;");
//					break;
//				}
//				case 209: { // enhe mayuscula
//					this.writer.write("&#xD1;");
//					break;
//				}

				default:this.writer.write(c);
			}
		}
	}

	private void writeSerialization(Node node) {
        writeSerialization(node, false);
	}

	private void writeSerialization(Node node, boolean mixed) {
		if (node == null) {
			return;
		}

		// handle node types
		int type = node.getNodeType();

		switch (type) {
			case Node.ATTRIBUTE_NODE:// should not happen
				break;

			case Node.CDATA_SECTION_NODE: {
				// write indentation
				writeIndentation();

				// write cdata section
				this.writer.print("<![CDATA[");
				this.writer.print(node.getNodeValue());
				this.writer.print("]]>");

				// write line breaking
				writeLineBreaking();

				break;
			}

			case Node.COMMENT_NODE: {
				// write indentation
				writeIndentation();

				// write comment
				this.writer.print("<!--");
				this.writer.print(node.getNodeValue());
				this.writer.print("-->");

				// write line breaking
				writeLineBreaking();

				break;
			}

			case Node.DOCUMENT_FRAGMENT_NODE: {
				NodeList children = node.getChildNodes();
				int count = children.getLength();

				for (int index = 0; index < count; index++) {
					// serialize children
					writeSerialization(children.item(index), mixed);
				}

				break;
			}

			case Node.DOCUMENT_NODE: {
                if (! this.declarationOmitting) {
                    // write xml header
                    this.writer.print("<?xml version=\"");
                    this.writer.print(this.version);
                    this.writer.print("\" encoding=\"");
                    this.writer.print(this.encoding);
                    this.writer.print("\"");

                    if (this.standalone) {
                        this.writer.print(" standalone=\"yes\"");
                    }

                    this.writer.print("?>");

                    // write line breaking
                    //writeLineBreaking();
                    this.writer.println();
                }

				// serialize document element
				writeSerialization(((Document)node).getDocumentElement());

				break;
			}

			case Node.DOCUMENT_TYPE_NODE:// todo ?
				break;

			case Node.ELEMENT_NODE: {
				// write indentation
				writeIndentation();

				// start opening tag
				this.writer.print("<");
				this.writer.print(node.getNodeName());

				// write attributes
				NamedNodeMap attributes = node.getAttributes();
				Node attribute = null;
				for (int index = 0; index < attributes.getLength(); index++) {
					attribute = attributes.item(index);
					this.writer.print(" ");
					this.writer.print(attribute.getNodeName());
					this.writer.print("=\"");
					writeNormalization(attribute.getNodeValue());
					this.writer.print("\"");
				}

				NodeList children = node.getChildNodes();
				int count = children.getLength();
				boolean textOnly = (count == 1) && (children.item(0).getNodeType() == Node.TEXT_NODE);

				if (count > 0) {
					// finish opening tag
					this.writer.print(">");

					if (!textOnly) {
						// write line breaking
						writeLineBreaking();

						// increase indentation depth
						this.depth++;
					}

					for (int index = 0; index < count; index++) {
						// serialize children
						writeSerialization(children.item(index), !textOnly);
					}

					if (!textOnly) {
						// decrease indentation depth
						this.depth--;

						// write indentation
						writeIndentation();
					}

					// write closing tag
					this.writer.print("</");
					this.writer.print(node.getNodeName());
					this.writer.print(">");
				} else {
					// finish empty tag
					this.writer.print(" />");
				}

				// write line breaking
				writeLineBreaking();

				break;
			}

			case Node.ENTITY_NODE:// todo ?
				break;

			case Node.ENTITY_REFERENCE_NODE: {
				// write indentation
				writeIndentation();

				// write entity reference
				this.writer.print("&");
				this.writer.print(node.getNodeName());
				this.writer.print(";");

				// write line breaking
				writeLineBreaking();

				break;
			}

			case Node.NOTATION_NODE:// todo ?
				break;

			case Node.PROCESSING_INSTRUCTION_NODE: {
				// write indentation
				writeIndentation();

				// write processing instruction
				this.writer.print("<?");
				this.writer.print(node.getNodeName());

				String data = node.getNodeValue();

				if ((data != null) && (data.length() > 0)) {
					this.writer.print(" ");
					this.writer.print(data);
				}

				this.writer.print("?>");

				// write line breaking
				writeLineBreaking();

				break;
			}

			case Node.TEXT_NODE: {
				String value = node.getNodeValue();

				if (value != null) {
					if (this.whitespaceIgnoring && DOMWhitespace.isWhitespace(value)) {
						// ignore whitespace
						break;
					}

                    if (mixed) {
                        // write indentation
                        writeIndentation();
                    }

					// write text
					writeNormalization(value);

                    if (mixed) {
                        // write line breaking
                        writeLineBreaking();
                    }
				}

				break;
			}
		}
	}
}
