package org.srinivas.siteworks.restserver.utilities;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.xml.sax.InputSource;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

public class JAXCodeGen {

	/**
	 * Generate JAXB Based Java Objects. 
	 * @param outputDirectory the output directory.
	 * @param packageName the package name.
	 * @param xsdName the xsd name.
	 * @throws URISyntaxException 
	 * @throws IOException.
	 */
	public void generate(String outputDirectory, String packageName, String xsdName) throws IOException, URISyntaxException {

		// Setup schema compiler
		SchemaCompiler sc = XJC.createSchemaCompiler();
		sc.forcePackageName(packageName);	

		// Setup SAX InputSource		
		File schemaFile = new File(getClass().getClassLoader().getResource(xsdName).getFile());
		InputSource is = new InputSource(schemaFile.toURI().toString());

		// Parse & build
		sc.parseSchema(is);
		S2JJAXBModel model = sc.bind();
		JCodeModel jCodeModel = model.generateCode(null, null);
		jCodeModel.build(new File(outputDirectory));

	}

}
