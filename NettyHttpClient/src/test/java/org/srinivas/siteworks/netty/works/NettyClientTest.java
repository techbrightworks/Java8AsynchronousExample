package org.srinivas.siteworks.netty.works;

import java.util.List;
import java.util.stream.IntStream;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.netty.newsobjects.NewsChannel;

public class NettyClientTest extends TestCase {
	private static final String NEWSSOURCE_PROGRESSIVENEWS_URL = "http://localhost:8080/RestServer/feeds/newssource/progressivenews/";
	private static final String NEWSSOURCE_POSITIVENEWS_URL = "http://localhost:8080/RestServer/feeds/newssource/positivenews/";
	private ThreadLocal<NettyClient> nettyClient;
	private static final Logger log = LoggerFactory.getLogger(NettyClientTest.class);

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		nettyClient = ThreadLocal.withInitial(NettyClient::new);		
		nettyClient.get().getNewsChannels().clear();
	}

	/**
	 * Test execute netty client.
	 * @throws Exception.
	 */
	@Test
	public void testExecuteNettyClient() throws Exception {
		nettyClient.get().executeNettyClient(new String[] { NEWSSOURCE_PROGRESSIVENEWS_URL, NEWSSOURCE_POSITIVENEWS_URL });
		assertEquals(2, nettyClient.get().getNewsChannels().size());
		assertTitle(nettyClient.get().getNewsChannels(), "Progressive News");
		assertTitle(nettyClient.get().getNewsChannels(), "Positive News");
	}

	/**
	 * Test execute 50 Netty Client Calls(Reliability).
	 * @throws Exception.
	 */
	@Test
	public void testExecuteNettyClient50() throws Exception {		
		IntStream.range(0, 50).forEach(i ->{
			log.info("NettyClientCall: "+i);
			setUp();
			try {
				testExecuteNettyClient();
			} catch (Exception e) {
				log.info("NettyClientCall50", e);
				fail("NettyClientCall50: " + e.getMessage());
			}			
		});

	}

	/**
	 * Assert title.
	 * @param newsChannelList the List of NewsChannel.
	 * @param title the text.
	 */
	private void assertTitle(List<NewsChannel> newsChannelList, String title) {
		boolean isFound = false;
		if(newsChannelList.parallelStream().filter(newsChannel -> newsChannel.getTitle().equals(title)).count() == 1L){
			isFound = true;
		}
		assertTrue(title, isFound);
	}	

}
