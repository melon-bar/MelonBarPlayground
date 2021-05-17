package com.melonbar.playground.module;

import com.melonbar.exchange.coinbase.client.CoinbaseProClient;
import com.melonbar.exchange.coinbase.model.core.ProductId;
import com.melonbar.exchange.coinbase.model.order.MarketOrderRequest;
import com.melonbar.exchange.coinbase.model.order.flag.OrderSide;
import com.melonbar.exchange.coinbase.model.response.Response;
import com.melonbar.exchange.coinbase.util.JsonUtils;
import com.melonbar.exchange.coinbase.websocket.CoinbaseProWebsocketFeedClient;
import com.melonbar.exchange.coinbase.websocket.MessageTypes;
import com.melonbar.exchange.coinbase.websocket.message.model.Channel;
import com.melonbar.exchange.coinbase.websocket.message.model.ChannelType;
import com.melonbar.exchange.coinbase.websocket.processing.MessageHandlers;
import com.melonbar.exchange.coinbase.websocket.processing.tracking.PriceTracker;
import com.melonbar.playground.client.ClientProvider;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Simple example with basic definitions.
 */
@Slf4j
public class DemoModule0 extends AbstractModule {

    private static final int DURATION_SECONDS = 600;
    private static final int KB = 1024;

    private CoinbaseProClient httpsClient;
    private CoinbaseProWebsocketFeedClient websocketClient;
    private PriceTracker priceTracker;

    @Override
    public void init() {
        httpsClient = ClientProvider.createHttpsClientUsingConfigCredentials();;
        priceTracker = new PriceTracker();
        websocketClient = CoinbaseProWebsocketFeedClient.builder()
                .withTrackers(priceTracker) // same as: withMessageHandlers(priceTracker::update)
                .withChannels(
                        Channel.of(ChannelType.TICKER),
                        Channel.of(ChannelType.MATCHES))
                .withProducts(
                        ProductId.ETH_USD,
                        ProductId.BTC_USD)
                .withTextBufferSize(64 * KB)
                .build();
    }

    @Override
    public void run() {
        // add some message handlers here just for example (better to init with builder)
        websocketClient.addMessageHandlers(
                // on ticker message: echo best bid and best ask
                MessageHandlers.byType(MessageTypes.TICKER,
                        message -> log.info("[RECEIVED TICKER] best bid: {}, best ask: {}",
                                JsonUtils.extractField("best_bid", message),
                                JsonUtils.extractField("best_ask", message))),
                // on match message: echo trade IDs of matches
                MessageHandlers.byType(MessageTypes.MATCHED_ORDER,
                        message -> log.info("[RECEIVED MATCH] new match, trade ID: {}",
                                JsonUtils.extractField("trade_id", message)))
        );

        // make 2 threads for tracking + echoing ETH and BTC price each
        final Thread btcThread = new Thread(
                () -> {
                    while (true) {
                        log.info("[BTC Thread] price: {} USD", getPrice(ProductId.BTC_USD));
                        delay(100);
                    }
                });
        final Thread ethThread = new Thread(
                () -> {
                    while (true) {
                        log.info("[ETH Thread] price: {} USD", getPrice(ProductId.ETH_USD));
                        delay(100);
                    }
                });

        // start threads
        btcThread.start();
        ethThread.start();

        // pause main thread for some time
        delay(DURATION_SECONDS * 1000);

        // done
        log.info("DONE!");
    }

    private static void delay(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException interruptedException) {
            log.warn("Interrupted!", interruptedException);
        }
    }

    private double getPrice(final ProductId productId) {
        return priceTracker.getPrice(productId).doubleValue();
    }

    // danger below!

    private MarketOrderRequest marketOrderRequest(final OrderSide side, final double amount) {
        return MarketOrderRequest.builder()
                .side(side)
                .productId(ProductId.ETH_USD)
                .funds(BigDecimal.valueOf(amount))
                .build();
    }

    private Response sell(final double amount) {
        return httpsClient.placeMarketOrder(marketOrderRequest(OrderSide.BUY, amount));
    }

    private Response buy(final double amount) {
        return httpsClient.placeMarketOrder(marketOrderRequest(OrderSide.SELL, amount));
    }
}
