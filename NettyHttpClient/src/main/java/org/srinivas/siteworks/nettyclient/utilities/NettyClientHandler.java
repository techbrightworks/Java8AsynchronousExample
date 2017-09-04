package org.srinivas.siteworks.nettyclient.utilities;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.netty.newsobjects.NewsChannel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<HttpObject> {

	private static final String EMPTY_STRING = "";
	private boolean readComplete;
	private StringBuffer xmlValue = new StringBuffer(NettyClientHandler.EMPTY_STRING);
	private static final Logger log = LoggerFactory.getLogger(NettyClientHandler.class);
	private NewsChannelMessages nettyClient;	

	/**
	 * Checks if is read complete.
	 * @return true, if is read complete.
	 */
	public boolean isReadComplete() {
		return readComplete;
	}

	/**
	 * Sets the read complete.
	 * @param readComplete the new read complete.
	 */
	public void setReadComplete(boolean readComplete) {
		this.readComplete = readComplete;
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
		if (msg instanceof HttpResponse) {
			HttpResponse response = (HttpResponse) msg;

			log.info("STATUS: " + response.getStatus());

			if ((response.headers().get(HttpHeaders.Names.CONNECTION) != null) && response.headers().get(HttpHeaders.Names.CONNECTION).equals(HttpHeaders.Values.CLOSE)) {
				setReadComplete(true);
			}

		}
		if (msg instanceof HttpContent) {
			HttpContent content = (HttpContent) msg;

			String xmlString = content.content().toString(CharsetUtil.UTF_8);

			xmlValue.append(xmlString);

			addNewsChannels(xmlString, xmlValue.toString());

			System.err.flush();

			if (content instanceof LastHttpContent) {
				log.info("} END OF CONTENT");
				if (isReadComplete()) {
					ctx.close();
				}
			}
		}

	}

	/**
	 * Adds the news channels.
	 * @param xmlString the xml string.
	 * @param xmlValue the xml value.
	 */
	private void addNewsChannels(String xmlString, String xmlValue) {
		if (xmlString.trim().contains("<newsChannel>") && xmlString.trim().endsWith("</newsChannel>")) {
			NewsChannel result = xmlToNewsChannel(xmlString);
			if (result.getNewsItems().getItem().size() > 0) {
				getNettyClient().addNewsChannel(result);
				log.info("XMLSTRING" + xmlString);
				log.info("newschannelsize" + getNettyClient().getNewsChannels().size());				
			}
			setXmlValue(new StringBuffer(EMPTY_STRING));

		} else if (xmlValue.trim().contains("<newsChannel>") && xmlValue.trim().endsWith("</newsChannel>")) {
			NewsChannel result = xmlToNewsChannel(xmlValue);
			if (result.getNewsItems().getItem().size() > 0) {
				getNettyClient().addNewsChannel(result);
				log.info("XMLVALUE" + xmlValue);
				log.info("newschannelsize" + getNettyClient().getNewsChannels().size());
			}
			setXmlValue(new StringBuffer(EMPTY_STRING));
		}
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#exceptionCaught(io.netty.channel.ChannelHandlerContext, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	/**
	 * Gets the Netty client.
	 * @return the Netty Client.
	 */
	public NewsChannelMessages getNettyClient() {
		return nettyClient;
	}

	/**
	 * Sets the Netty client.
	 * @param nettyClient the new Netty client.
	 */
	public void setNettyClient(NewsChannelMessages nettyClient) {
		this.nettyClient = nettyClient;
	}

	/**
	 * Gets the xml value.
	 * @return the xml value.
	 */
	public StringBuffer getXmlValue() {
		return xmlValue;
	}

	/**
	 * Sets the xml value.
	 * @param xmlValue the new xml value.
	 */
	public void setXmlValue(StringBuffer xmlValue) {
		this.xmlValue = xmlValue;
	}

	/**
	 * Xml to NewsChannel.
	 * @param xmlString the xml string.
	 * @return the news channel.
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
}