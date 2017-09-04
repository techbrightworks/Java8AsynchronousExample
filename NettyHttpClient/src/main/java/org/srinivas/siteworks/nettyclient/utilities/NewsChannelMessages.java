package org.srinivas.siteworks.nettyclient.utilities;

import java.util.List;

import org.srinivas.siteworks.netty.newsobjects.NewsChannel;

public interface NewsChannelMessages {

	/**
	 * Gets the news channels.
	 * @return the news channels.
	 */
	List<NewsChannel> getNewsChannels();

	/**
	 * Sets the news channels.
	 * @param newsChannels the new newsChannels.
	 */
	void setNewsChannels(List<NewsChannel> newsChannels);

	/**
	 * Adds the news channel.
	 * @param newsItem the news item.
	 */
	void addNewsChannel(NewsChannel newsItem);

	/**
	 * Gets the netty client handler.
	 * @return the netty client handler.
	 */
	NettyClientHandler getNettyClientHandler();

	/**
	 * Sets the netty client handler.
	 * @param nettyClientHandler the new netty client handler.
	 */
	void setNettyClientHandler(NettyClientHandler nettyClientHandler);

}