package org.srinivas.siteworks.restserver.utilities;

import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.generated.Item;
import org.srinivas.siteworks.generated.NewsChannel;

public class RestServerUtility {

	private static final Logger log = LoggerFactory.getLogger(RestServerUtility.class);

	/**
	 * Item to xml. 
	 * @param item the item.
	 * @param stringWriter the string writer.
	 */
	public static void itemToXML(Item item, StringWriter stringWriter) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(item, stringWriter);
		} catch (PropertyException e) {
			log.info("Error", e);
		} catch (JAXBException e) {
			log.info("Error", e);
		}
	}

	/**
	 * News channel to xml. 
	 * @param newsChannel the news channel.
	 * @param stringWriter the string writer.
	 */
	public static void newsChannelToXML(NewsChannel newsChannel, StringWriter stringWriter) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(NewsChannel.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(newsChannel, stringWriter);
		} catch (PropertyException e) {
			log.info("Error", e);
		} catch (JAXBException e) {
			log.info("Error", e);
		}
	}

	/**
	 * Gets the current date. 
	 * @return the current date.
	 */
	public static XMLGregorianCalendar getCurrentDate() {
		GregorianCalendar gcal = (GregorianCalendar) GregorianCalendar.getInstance();
		gcal.setTime(new Date());
		XMLGregorianCalendar xgcal = null;
		try {
			xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		} catch (DatatypeConfigurationException e) {
			log.info("Error", e);
		}
		return xgcal;
	}

}
