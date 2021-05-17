package com.melonbar.playground.client;

import com.melonbar.exchange.coinbase.client.CoinbaseProClient;
import com.melonbar.exchange.coinbase.client.CoinbaseProClientFactory;
import com.melonbar.playground.config.AppConfigLoader;

public class ClientProvider {

    public static CoinbaseProClient createHttpsClient(final String apiKey,
                                                      final String apiPassword,
                                                      final String apiSecretKey) {
        return CoinbaseProClientFactory.createClient(apiKey, apiPassword, apiSecretKey);
    }

    public static CoinbaseProClient createHttpsClientUsingConfigCredentials() {
        return createHttpsClient(AppConfigLoader.getApiKey(),
                AppConfigLoader.getApiPassword(), AppConfigLoader.getApiSecretKey());
    }
}
