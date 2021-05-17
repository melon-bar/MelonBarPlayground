package com.melonbar.playground.dummy;

import com.melonbar.exchange.coinbase.http.HttpClient;
import com.melonbar.exchange.coinbase.model.response.Response;
import com.melonbar.exchange.coinbase.model.request.BaseRequest;
import com.melonbar.exchange.coinbase.util.request.Requests;
import org.slf4j.Logger;

import javax.net.ssl.SSLSession;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.Future;

public class DummyHttpClient implements HttpClient {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DummyHttpClient.class);

    @Override
    public HttpResponse<Response> send(BaseRequest request) {
        log.info("request: {}", Requests.toString(request));
        return new DummyHttpResponse();
    }

    @Override
    public Future<HttpResponse<Response>> sendAsync(BaseRequest request) {
        return null;
    }

    public static class DummyHttpResponse implements HttpResponse<Response> {

        @Override
        public int statusCode() {
            return 999;
        }

        @Override
        public HttpRequest request() {
            return null;
        }

        @Override
        public Optional<HttpResponse<Response>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return null;
        }

        @Override
        public Response body() {
            return new Response("DUMMY", null, 999);
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return UriBuilder.fromPath("DUMMY").build();
        }

        @Override
        public java.net.http.HttpClient.Version version() {
            return null;
        }
    }
}
