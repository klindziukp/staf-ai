package com.klindziuk.petstore.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.klindziuk.petstore.config.ConfigurationManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Factory class for creating Retrofit client instances
 */
@Slf4j
public final class RetrofitClientFactory {
    
    private static Retrofit retrofit;
    private static final ConfigurationManager config = ConfigurationManager.getInstance();
    
    private RetrofitClientFactory() {
        // Private constructor to prevent instantiation
    }
    
    public static synchronized Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = createRetrofit();
        }
        return retrofit;
    }
    
    private static Retrofit createRetrofit() {
        String baseUrl = config.getBaseUrl();
        log.info("Creating Retrofit instance with base URL: {}", baseUrl);
        
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build();
    }
    
    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getDefaultTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getDefaultTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getDefaultTimeout(), TimeUnit.SECONDS);
        
        if (config.isLoggingEnabled()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                    message -> log.debug("HTTP: {}", message)
            );
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        
        return builder.build();
    }
    
    private static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .create();
    }
    
    public static <T> T createService(Class<T> serviceClass) {
        return getRetrofitInstance().create(serviceClass);
    }
}
