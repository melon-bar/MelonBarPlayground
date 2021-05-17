package com.melonbar.playground.config;

import com.melonbar.exchange.coinbase.util.AppConfig;
import com.melonbar.exchange.coinbase.util.Guard;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public final class AppConfigLoader {
    private static final String APP_CONFIG_FILE_NAME = "credentials.properties";
    private static final String API_KEY_CONFIG = "api_key";
    private static final String API_PASSWORD_CONFIG = "api_password";
    private static final String API_SECRET_KEY_CONFIG = "api_secret_key";

    private static final Properties PROPERTIES = new Properties();

    @Getter private static String apiKey;
    @Getter private static String apiPassword;
    @Getter private static String apiSecretKey;

    static {
        try (final InputStream propertiesStream
                     = AppConfig.class.getClassLoader().getResourceAsStream(APP_CONFIG_FILE_NAME)){
            PROPERTIES.load(propertiesStream);
            apiKey = PROPERTIES.getProperty(API_KEY_CONFIG);
            apiPassword = PROPERTIES.getProperty(API_PASSWORD_CONFIG);
            apiSecretKey = PROPERTIES.getProperty(API_SECRET_KEY_CONFIG);

            // panic if any missing credentials (indicating incomplete credentials.properties)
            Guard.nonNull(apiKey, apiPassword, apiSecretKey);
        } catch (Exception exception) {
            log.error("Could not load {} due to exception.", APP_CONFIG_FILE_NAME, exception);
            log.warn("If you haven't already, please create a {} file under the resources directory "
                    + "with fields: {}, {}, {}", APP_CONFIG_FILE_NAME, API_KEY_CONFIG, API_PASSWORD_CONFIG,
                    API_SECRET_KEY_CONFIG);
            System.exit(-1);
        }
    }

    public static String getValue(final String key) {
        Guard.nonNull(key);
        synchronized (PROPERTIES) {
            return PROPERTIES.getProperty(key);
        }
    }
}
