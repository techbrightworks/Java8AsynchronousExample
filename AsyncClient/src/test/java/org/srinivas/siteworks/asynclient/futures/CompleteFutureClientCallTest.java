package org.srinivas.siteworks.asynclient.futures;

import java.util.List;
import java.util.stream.IntStream;
import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.asynclient.newsobjects.Item;
import org.srinivas.siteworks.asynclient.newsobjects.NewsChannel;

public class CompleteFutureClientCallTest extends TestCase {

	private static final String NEWSSOURCE_PROGRESSIVENEWS_URL = "http://localhost:8080/RestServer/feeds/newssource/progressivenews/";
	private static final String NEWSSOURCE_POSITIVENEWS_URL = "http://localhost:8080/RestServer/feeds/newssource/positivenews/";

	private ThreadLocal<CompleteFutureClientCall> completeFutureClient;
	private static final Logger log = LoggerFactory.getLogger(CompleteFutureClientCallTest.class);

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		completeFutureClient = ThreadLocal.withInitial(CompleteFutureClientCall::new);
	}

	/**
	 * TestExecute complete future client call.
	 * @throws Exception.
	 */
	@Test
	public void testexecuteCompleteFutureClientCall() throws Exception {
		List<NewsChannel> newsChannelList = completeFutureClient.get().executeCompleteFutureClientCall(new String[] { NEWSSOURCE_PROGRESSIVENEWS_URL, NEWSSOURCE_POSITIVENEWS_URL });
		assertEquals(2, newsChannelList.size());

	}

	/**
	 * Test news channel filter test.
	 * @throws Exception. 
	 */
	@Test
	public void testNewsChannelFilterTest() throws Exception {
		List<NewsChannel> newsChannelList = completeFutureClient.get().executeCompleteFutureClientCall(new String[] { NEWSSOURCE_PROGRESSIVENEWS_URL, NEWSSOURCE_POSITIVENEWS_URL });
		assertEquals(2, newsChannelList.size());
		List<Item> filteredResults = completeFutureClient.get().newsChannelItemsFilter(newsChannelList, completeFutureClient.get().conditionJava);
		assertEquals(filteredResults.size(), 2);
	}

	/**
	 * Test news item test.
	 * @throws Exception.
	 */
	@Test
	public void testNewsItemTest() throws Exception {
		List<NewsChannel> newsChannelList = completeFutureClient.get().executeCompleteFutureClientCall(new String[] { NEWSSOURCE_PROGRESSIVENEWS_URL, NEWSSOURCE_POSITIVENEWS_URL });
		assertEquals(2, newsChannelList.size());
		List<Item> filteredResults = completeFutureClient.get().newsItemFilter(newsChannelList.get(0).getNewsItems().getItem(), completeFutureClient.get().conditionJava);
		assertEquals(1, filteredResults.size());
	}

	/**
	 * TestExecute 50 complete future client calls(Reliability).
	 * @throws Exception.
	 */
	@Test
	public void testexecuteCompleteFuturClientCall50() throws Exception {
		IntStream.range(0, 50).forEach(testNumber -> {
			log.info("CompleteFuturClientCall:" + testNumber);
			setUp();
			try {
				testexecuteCompleteFutureClientCall();
			} catch (Exception e) {
				log.info("CompleteFuturClientCall50", e);
				fail("CompleteFuturClientCall50: " + e.getMessage());
			}
		});
	}

}
