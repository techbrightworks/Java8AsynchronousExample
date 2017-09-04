package org.srinivas.siteworks.netty.works;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.nettyclient.utilities.NettyClientInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

public class NettyClient extends NewsChannelMessagesImpl {

	private static final int ARRAY_FIRST_ELEMENT_INDEX = 0;
	private static final Logger log = LoggerFactory.getLogger(NettyClient.class);
	private static ChannelFuture clientChannelFuture;

	
	/**
	 * Execute Netty Client.
	 * @param newsSources the news sources.
	 * @throws Exception.
	 */
	public void executeNettyClient(String[] newsSources) throws Exception {
		URI uri = new URI(newsSources[ARRAY_FIRST_ELEMENT_INDEX]);
		String host = uri.getHost();
		int port = uri.getPort();

		// Configure the client.
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootStrap = new Bootstrap();

		try {
			makeCall(newsSources, uri, host, port, group, bootStrap);
			if (!getClientChannelFuture().channel().closeFuture().await(5000)) {
				log.info("The request timed out.");
			}
			log.info("Done");
		} finally {
			group.shutdownGracefully();
		}
	}

	/**
	 * Make call.
	 * @param newsSources the news sources.
	 * @param uri the uri.
	 * @param host the host.
	 * @param port the port.
	 * @param group the group.
	 * @param bootStrap the boot strap.
	 * @throws URISyntaxException.
	 */
	private void makeCall(String[] newsSources, URI uri, String host, int port, EventLoopGroup group, Bootstrap bootStrap) throws URISyntaxException {
		bootStrap.group(group).channel(NioSocketChannel.class).handler(new NettyClientInitializer(null, getNettyClientHandler()));
		// Make the connection attempt.
		clientChannelFuture = bootStrap.connect(host, port);

		IntStream.range(0, newsSources.length).forEachOrdered(newsSource -> {
			boolean isClosingSource = (newsSource == newsSources.length - 1) ? true : false;
			try {
				makeSourceCall(clientChannelFuture, newsSources[newsSource], isClosingSource);
			} catch (Exception e) {
				log.info("Error at Make source call", e);
			}
		});

	}
	
	/**
	 * Gets the client channel future.
	 * @return the client channel future.
	 */
	public static ChannelFuture getClientChannelFuture() {
		return clientChannelFuture;
	}

	/**
	 * Sets the client channel future.
	 * @param clientChannelFuture the new client channel future.
	 */
	public static void setClientChannelFuture(ChannelFuture clientChannelFuture) {
		NettyClient.clientChannelFuture = clientChannelFuture;
	}	

	/**
	 * Make source call.
	 * @param callChannelfuture the call channelfuture.
	 * @param url the url.
	 * @param isClosingSource the boolean for closing source.
	 * @throws URISyntaxException. the URI syntax exception.
	 */
	private void makeSourceCall(ChannelFuture callChannelfuture, String url, boolean isClosingSource) throws URISyntaxException {
		URI uri = new URI(url);
		String host = uri.getHost();

		callChannelfuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (!future.isSuccess()) {
					future.cause().printStackTrace();
					return;
				}

				log.info("Prepare the HTTP request");
				HttpRequest callRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
				callRequest.headers().set(HttpHeaders.Names.HOST, host);
				if (isClosingSource) {
					callRequest.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);

				} else {
					callRequest.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CONTINUE);
				}
				callRequest.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
				future.channel().writeAndFlush(callRequest);

			}

		});
	}

}
