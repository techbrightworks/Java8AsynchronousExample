package org.srinivas.siteworks.restserver;

import java.io.File;
import java.io.FilenameFilter;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.generated.NewsChannel;
import org.srinivas.siteworks.restserver.utilities.JAXCodeGen;

public class RestServerTest extends TestCase {

	private static final String FILE_NAME_STARTS_WITH_NEWS_CHANNEL = "NewsChannel";
	private static final String DELETE_FOLDER_TEMP = File.separator +"temp";
	private static final String JAXB_CODE_GEN_DIR_PACKAGE_PATH_TEMP_GENERATED = File.separator + "temp" + File.separator + "generated";
	private static final String JAXB_CODE_GEN_XSD_NAME_NEWS_SOURCE_XSD = "NewsSource.xsd";
	private static final String JAXB_CODE_GEN_PACKAGE_NAME_TEMP_GENERATED = "temp.generated";
	private static final String JAXB_CODE_GEN_OUTPUT_DIRECTORY_SRC_TEST_JAVA = "src" + File.separator + "test" + File.separator + "java";
	private PositiveNewsResource positiveNewsResource;
	private ProgressiveNewsResource progressiveNewsResource;
	private static final Logger log = LoggerFactory.getLogger(RestServerTest.class);

	/*
	 * (non-Javadoc) 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		positiveNewsResource = new PositiveNewsResource();
		progressiveNewsResource = new ProgressiveNewsResource();
	}

	/**
	 * Test positive news resource. 
	 * @throws Exception.
	 */
	@Test
	public void testPositiveNewsResource() throws Exception {
		String xmlstring = positiveNewsResource.PositiveNews();
		NewsChannel nc = xmlToNewsChannel(xmlstring);
		assertEquals(nc.getTitle(), "Positive News");
	}

	/**
	 * Test progressive news resource. 
	 * @throws Exception.
	 */
	@Test
	public void testProgressiveNewsResource() throws Exception {
		String xmlstring = progressiveNewsResource.ProgressiveNews();
		NewsChannel nc = xmlToNewsChannel(xmlstring);
		assertEquals(nc.getTitle(), "Progressive News");

	}

	/**
	 * Test JAXB code generation. 
	 * @throws Exception.
	 */
	@Test
	public void testJAXCodeGen() throws Exception {
		JAXCodeGen jaxCodeGen = new JAXCodeGen();
		jaxCodeGen.generate(JAXB_CODE_GEN_OUTPUT_DIRECTORY_SRC_TEST_JAVA, JAXB_CODE_GEN_PACKAGE_NAME_TEMP_GENERATED, JAXB_CODE_GEN_XSD_NAME_NEWS_SOURCE_XSD);
		File dir = new File(new File("").getAbsolutePath() + File.separator + JAXB_CODE_GEN_OUTPUT_DIRECTORY_SRC_TEST_JAVA + JAXB_CODE_GEN_DIR_PACKAGE_PATH_TEMP_GENERATED);
		dir.listFiles();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(FILE_NAME_STARTS_WITH_NEWS_CHANNEL);
			}
		};
		String[] children = dir.list(filter);
		assertTrue(children.length == 1);
		deleteTempFile(new File(new File("").getAbsolutePath() + File.separator + JAXB_CODE_GEN_OUTPUT_DIRECTORY_SRC_TEST_JAVA + DELETE_FOLDER_TEMP));
	}

	/**
	 * Xml to NewsChannel. 
	 * @param xmlString the xml string.
	 * @return the NewsChannel.
	 */
	private NewsChannel xmlToNewsChannel(String xmlString) {
		NewsChannel newsChannel = new NewsChannel();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(NewsChannel.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			newsChannel = (NewsChannel) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));

		} catch (PropertyException e) {
			log.info(e.getMessage());
		} catch (JAXBException e) {
			log.info(e.getMessage());
		}
		return newsChannel;
	}

	/**
	 * Delete temporary file. 
	 * @param tempFile the temporary file.
	 */
	private void deleteTempFile(File tempFile) {
		try {
			if (tempFile.isDirectory()) {
				File[] entries = tempFile.listFiles();
				for (File currentFile : entries) {
					deleteTempFile(currentFile);
				}
				tempFile.delete();
			} else {
				tempFile.delete();
			}
			log.info("DELETED Temporal File: " + tempFile.getPath());
		} catch (Throwable t) {
			log.error("Could not DELETE file: " + tempFile.getPath(), t);
		}
	}

}
