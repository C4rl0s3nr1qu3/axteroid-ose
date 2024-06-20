package com.axteroid.ose.server.tools.exception;

import java.io.File;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

public class MyURIResolver implements URIResolver {

	public Source resolve(String href, String base) throws TransformerException {
		File resourceFile = new File(MyURIResolver.class.getClassLoader().getResource("META-INF/" + href).getFile());
		return new StreamSource(resourceFile);
	}
}
