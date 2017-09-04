package org.srinivas.siteworks.asynclient.utilities;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

public class LoggingFilter implements ClientRequestFilter, ClientResponseFilter {

	private static final String STRING_THREAD_NAME = ":Thread Name ";
	private static final String STRING_CLIENT_RESPONSE = "Client Response";
	private static final String STRING_CLIENT_REQUEST = "Client request";
	private static final int ENTITY_SIZE_0 = 0;
	private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());
	private static final int MAXIMUM_ENTITY_SIZE = 8 * 1024;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ws.rs.client.ClientRequestFilter#filter(javax.ws.rs.client.
	 * ClientRequestContext)
	 */
	@Override
	public void filter(final ClientRequestContext context) throws IOException {
		final StringBuilder requestBuilderLog = new StringBuilder();
		requestLInePrint(requestBuilderLog, STRING_CLIENT_REQUEST, context.getMethod(), context.getUri());
		log(requestBuilderLog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ws.rs.client.ClientResponseFilter#filter(javax.ws.rs.client.
	 * ClientRequestContext, javax.ws.rs.client.ClientResponseContext)
	 */
	@Override
	public void filter(final ClientRequestContext requestContext, final ClientResponseContext responseContext) throws IOException {
		final StringBuilder responseBuilderLog = new StringBuilder();

		responseLinePrint(responseBuilderLog, STRING_CLIENT_RESPONSE, responseContext.getStatus());

		if (responseContext.hasEntity()) {
			responseContext.setEntityStream(logInputStream(responseBuilderLog, responseContext.getEntityStream(), Charset.forName("UTF-8")));
		}
		log(responseBuilderLog);
	}

	/**
	 * Log from the provided StringBuilder. 
	 * @param builder the StringBuilder.
	 */
	private void log(final StringBuilder builder) {
		if (logger != null) {
			logger.info(builder.toString());
		}
	}

	/**
	 * Request Line Print. 
	 * @param builder the StringBuilder.
	 * @param note  the Text.
	 * @param method the ContextMethod.
	 * @param uri the URI.
	 */
	private void requestLInePrint(final StringBuilder builder, final String note, final String method, final URI uri) {
		builder.append(note).append(STRING_THREAD_NAME).append(Thread.currentThread().getName()).append("\n");
		builder.append(method).append(" ").append(uri.toASCIIString()).append("\n");
	}

	/**
	 * Response Line Print. 
	 * @param builder the StringBuilder.
	 * @param note the Text.
	 * @param status the ResponseContextStatus.
	 */
	private void responseLinePrint(final StringBuilder builder, final String note, final int status) {
		builder.append(note).append(STRING_THREAD_NAME).append(Thread.currentThread().getName()).append("\n");
		builder.append(Integer.toString(status)).append("\n");
	}

	/**
	 * Log Input Stream. 
	 * @param builder the StringBuilder.
	 * @param stream  the Stream.
	 * @param charset the Charset.
	 * @return the InputStream.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private InputStream logInputStream(final StringBuilder builder, InputStream stream, final Charset charset) throws IOException {
		if (!stream.markSupported()) {
			stream = new BufferedInputStream(stream);
		}
		stream.mark(MAXIMUM_ENTITY_SIZE + 1);
		final byte[] entity = new byte[MAXIMUM_ENTITY_SIZE + 1];
		final int entitySize = stream.read(entity);
		builder.append(new String(entity, ENTITY_SIZE_0, Math.min(entitySize, MAXIMUM_ENTITY_SIZE), charset));
		if (entitySize > MAXIMUM_ENTITY_SIZE) {
			builder.append(" Entity Size Greater");
		}
		builder.append('\n');
		stream.reset();
		return stream;
	}

}