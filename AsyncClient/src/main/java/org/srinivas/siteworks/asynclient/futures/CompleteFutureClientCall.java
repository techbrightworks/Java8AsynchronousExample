package org.srinivas.siteworks.asynclient.futures;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.asynclient.newsobjects.Item;
import org.srinivas.siteworks.asynclient.newsobjects.NewsChannel;
import org.srinivas.siteworks.asynclient.utilities.LoggingFilter;

public class CompleteFutureClientCall {

	public static final int CALLTRIALS_COUNT_START_VALUE_0 = 0;
	private static final int MAX_CALL_TRIALS_ALLOWED_10 = 10;
	private static final Logger log = LoggerFactory.getLogger(CompleteFutureClientCall.class);
	private Client client;

	final Predicate<Item> conditionJava = new Predicate<Item>() {
		@Override
		public boolean test(Item item) {
			return item.getTitle().contains("Java");
		}
	};

	/**
	 * Instantiates a new complete future client call.
	 */
	public CompleteFutureClientCall() {
		client = ClientBuilder.newClient();
		client.register(LoggingFilter.class);
	}

	/**
	 * Execute complete future client call.
	 * @param newsSources the news sources.
	 * @return the NewsChannelList.
	 * @throws InterruptedException. 
	 * @throws ExecutionException.
	 */
	public List<NewsChannel> executeCompleteFutureClientCall(String[] newsSources) throws InterruptedException, ExecutionException {
		List<NewsChannel> newsChannelList = executeNewsSources(newsSources);
		return handleUnansweredCall(newsSources, newsChannelList);
	}

	/**
	 * News Item filter.
	 * @param items the List of Items.
	 * @param predicate the Predicate.
	 * @return the list of Item.
	 */
	public List<Item> newsItemFilter(List<Item> items, Predicate<Item> predicate) {
		List<Item> result = items.stream().filter(element -> predicate.test(element)).collect(Collectors.toList());
		return result;
	}

	/**
	 * News channel items filter.
	 * @param newsChannelsList the news channels list.
	 * @param predicate the Predicate.
	 * @return the list of Item.
	 */
	public List<Item> newsChannelItemsFilter(List<NewsChannel> newsChannelsList, Predicate<Item> predicate) {
		List<Item> result = new ArrayList<Item>();
		newsChannelsList.stream().forEachOrdered(newsSource -> {
			Collection<Item> filterResults = newsSource.getNewsItems().getItem().stream().filter(predicate).collect(Collectors.<Item> toList());
			result.addAll(filterResults);
		});
		return result;
	}

	/**
	 * Gets the client.
	 * @return the client.
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Sets the client.
	 * @param client the new client.
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Execute news sources.
	 * @param newsSources the news sources.
	 * @return the list of NewsChannel.
	 */
	private List<NewsChannel> executeNewsSources(String[] newsSources) {
		List<NewsChannel> newsChannelsList = new ArrayList<NewsChannel>();
		CompletableFuture<NewsChannel> newsChannels = callNewsSources(newsSources, client, newsChannelsList);
		while (!newsChannels.isDone()) {
			// include any of your processing
		}
		newsChannels.join();
		return newsChannelsList;
	}

	/**
	 * Call news sources.
	 * @param newsSources the news sources.
	 * @param client the client.
	 * @param newsChannelsList the news channels list.
	 * @return the completable future.
	 */
	private CompletableFuture<NewsChannel> callNewsSources(String[] newsSources, Client client, List<NewsChannel> newsChannelsList) {
		// Lambda not suitable
		CompletableFuture<NewsChannel> newsChannels = null;
		for (String source : newsSources) {
			newsChannels = executeNewsSourceCall(client, newsChannelsList, source);
		}
		return newsChannels;
	}

	/**
	 * Execute news source call.
	 * @param client the client.
	 * @param newsChannelsList the news channels list.
	 * @param source the News Source.
	 * @return the completable future.
	 */
	private CompletableFuture<NewsChannel> executeNewsSourceCall(Client client, List<NewsChannel> newsChannelsList, String source) {
		CompletableFuture<NewsChannel> newsChannels;
		newsChannels = CompletableFuture.supplyAsync((Supplier<NewsChannel>) () -> {
			log.info("Making a call");
			NewsChannel result = null;
			String xmlString = client.target(source).request().get(String.class);
			result = (null == xmlString || xmlString.isEmpty()) ? null : xmlToNewsChannel(xmlString);
			if (result != null) {
				newsChannelsList.add(result);
			}
			return result;
		}).exceptionally(e -> {
			log.info("Error", e);
			return null;
		});
		return newsChannels;
	}

	/**
	 * Handle unanswered call.
	 * Execute trials If there is No Response.
	 * @param newsSources the news sources.
	 * @param newsChannelList the news channel list.
	 * @return the list of NewsChannel.
	 */
	private List<NewsChannel> handleUnansweredCall(String[] newsSources, List<NewsChannel> newsChannelList) {
		if (IsAllSourcesNotResponded(newsSources, newsChannelList)) {
			int callTrials = CALLTRIALS_COUNT_START_VALUE_0;
			newsChannelList = executeTrials(newsSources, newsChannelList, callTrials, MAX_CALL_TRIALS_ALLOWED_10);
			return newsChannelList;
		} else {
			return newsChannelList;
		}
	}

	/**
	 * Checks if is all sources have not responded.
	 * @param newsSources the news sources.
	 * @param newsChannelsList the news channels list.
	 * @return true, if successful.
	 */
	private boolean IsAllSourcesNotResponded(String[] newsSources, List<NewsChannel> newsChannelsList) {
		return newsChannelsList.size() < newsSources.length;
	}

	/**
	 * Execute trials.
	 * @param newsSources the news sources.
	 * @param newsChannelList the news channel list.
	 * @param callTrials the call trials.
	 * @param maximumCallTrialsAllowed the maximum call trials allowed.
	 * @return the list of NewsChannel.
	 */
	private List<NewsChannel> executeTrials(String[] newsSources, List<NewsChannel> newsChannelList, int callTrials, int maximumCallTrialsAllowed) {
		// Lambda not suitable
		if (callTrials <= MAX_CALL_TRIALS_ALLOWED_10) {
			for (; callTrials < MAX_CALL_TRIALS_ALLOWED_10; callTrials++) {
				log.info("callTrials " + callTrials);
				newsChannelList.clear();
				newsChannelList = executeNewsSources(newsSources);
				if (!IsAllSourcesNotResponded(newsSources, newsChannelList)) {
					callTrials = MAX_CALL_TRIALS_ALLOWED_10;
				}
			}
		}
		return newsChannelList;
	}

	/**
	 * Xml to news channel.
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
			log.info("Exception", e);
		} catch (JAXBException je) {
			log.info("Exception", je);
		}
		return newsChannel;
	}

}
