package org.srinivas.siteworks.nettyclient.utilities;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.ssl.SslContext;


public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx; 
    private final NettyClientHandler nettyClientHandler;
    

   
    /**
     * Instantiates a new netty client initializer.
     * @param sslCtx the SslContext.
     * @param nettyClientHandler the netty client handler.
     */
    public NettyClientInitializer(SslContext sslCtx,NettyClientHandler nettyClientHandler) {
        this.sslCtx = sslCtx;    
        this.nettyClientHandler = nettyClientHandler;
    }
 
    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
     */
    @Override
    public void initChannel(SocketChannel socketChannel) {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        // Enable HTTPS if necessary.
        if (sslCtx != null) {
            channelPipeline.addLast(sslCtx.newHandler(socketChannel.alloc()));
        }
        channelPipeline.addLast(new HttpClientCodec());
        channelPipeline.addLast(nettyClientHandler);
    }
}