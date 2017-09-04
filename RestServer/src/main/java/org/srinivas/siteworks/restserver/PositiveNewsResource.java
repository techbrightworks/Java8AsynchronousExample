package org.srinivas.siteworks.restserver;

import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.srinivas.siteworks.generated.Item;
import org.srinivas.siteworks.generated.NewsChannel;
import org.srinivas.siteworks.generated.NewsItems;
import org.srinivas.siteworks.restserver.utilities.RestServerUtility;

@Path("/newssource/positivenews")
public class PositiveNewsResource {

	/**
	 * Positive news. 
	 * @return the string.
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String PositiveNews() {

		StringWriter stringWriter = new StringWriter();

		Item javaItem = new Item();
		javaItem.setCategory("World News");
		javaItem.setTitle("Java");
		javaItem.setDescription("Java Technologies");
		javaItem.setLink("http://www.techbrightworks.com/");
		javaItem.setPubDate(RestServerUtility.getCurrentDate());
		javaItem.setGuid("http://www.techbrightworks.com/");

		Item sportsItem = new Item();
		sportsItem.setCategory("Sports News");
		sportsItem.setTitle("Sports");
		sportsItem.setDescription("Cricket World Champion");
		sportsItem.setLink("http://www.techbrightworks.com/");
		sportsItem.setPubDate(RestServerUtility.getCurrentDate());
		sportsItem.setGuid("http://www.techbrightworks.com/");

		Item politicsItem = new Item();
		politicsItem.setCategory("Politics News");
		politicsItem.setTitle("Politics");
		politicsItem.setDescription("Party wins Elections");
		politicsItem.setLink("http://www.techbrightworks.com/");
		politicsItem.setPubDate(RestServerUtility.getCurrentDate());
		politicsItem.setGuid("http://www.techbrightworks.com/");

		NewsItems newsItems = new NewsItems();
		newsItems.getItem().add(javaItem);
		newsItems.getItem().add(sportsItem);
		newsItems.getItem().add(politicsItem);
		NewsChannel newsChannel = new NewsChannel();
		newsChannel.setTitle("Positive News");
		newsChannel.setNewsItems(newsItems);
		RestServerUtility.newsChannelToXML(newsChannel, stringWriter);
		return stringWriter.toString();
	}

}
