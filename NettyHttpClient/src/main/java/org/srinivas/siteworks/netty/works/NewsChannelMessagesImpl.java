package org.srinivas.siteworks.netty.works;

import java.util.ArrayList;
import java.util.List;

import org.srinivas.siteworks.netty.newsobjects.NewsChannel;
import org.srinivas.siteworks.nettyclient.utilities.NettyClientHandler;
import org.srinivas.siteworks.nettyclient.utilities.NewsChannelMessages;

public class NewsChannelMessagesImpl implements NewsChannelMessages {

	private List<NewsChannel> newsChannels = new ArrayList<NewsChannel>();
	private NettyClientHandler nettyClientHandler;

	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.netty.works.NewsChannelMessages#getNewsChannels()
	 */
	@Override
	public List<NewsChannel> getNewsChannels() {
		return newsChannels;
	}

	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.netty.works.NewsChannelMessages#setNewsChannels(java.util.List)
	 */
	@Override
	public void setNewsChannels(List<NewsChannel> newsChannels) {
		this.newsChannels = newsChannels;
	}

	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.netty.works.NewsChannelMessages#addNewsChannel(org.srinivas.siteworks.netty.newsobjects.NewsChannel)
	 */
	@Override
	public void addNewsChannel(NewsChannel newsItem) {
		getNewsChannels().add(newsItem);
	}

	/**
	 * Instantiates a new news channel messages impl.
	 */
	public NewsChannelMessagesImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.netty.works.NewsChannelMessages#getNettyClientHandler()
	 */
	@Override
	public NettyClientHandler getNettyClientHandler() {
		if(nettyClientHandler == null){
			 nettyClientHandler =	new NettyClientHandler();			 
			}
			nettyClientHandler.setNettyClient(this);
			return nettyClientHandler;
	}

	/* (non-Javadoc)
	 * @see org.srinivas.siteworks.netty.works.NewsChannelMessages#setNettyClientHandler(org.srinivas.siteworks.nettyclient.utilities.NettyClientHandler)
	 */
	@Override
	public void setNettyClientHandler(NettyClientHandler nettyClientHandler) {
		this.nettyClientHandler = nettyClientHandler;
	}

}