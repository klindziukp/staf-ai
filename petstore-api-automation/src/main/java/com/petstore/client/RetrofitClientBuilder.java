package com.petstore.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petstore.config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Retrofit Client Builder for creating and configuring Retrofit instances
 * Singleton pattern implementation
 */
@Slf4j
public class RetrofitClientBuilder {
    
    private static Retrofit retrofit;
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private RetrofitClientBuilder() {
    }
    
    public static synchronized Retrofit getClient() {
        if (retrofit == null) {
            retrofit = buildRetrofitClient();
        }
        return retrofit;
    }
    
    private static Retrofit buildRetrofitClient() {
        log.info("Building Retrofit client with base URL: {}", config.getBaseUrl());
        
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .addInterceptor(createLoggingInterceptor())
                .build();
        
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        
        return new Retrofit.Builder()
                .baseUrl(config.getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    
    private static HttpLoggingInterceptor createLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> log.debug(message));
        
        if (config.isLoggingEnabled()) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        
        return interceptor;
    }
    
    public static <T> T createService(Class<T> serviceClass) {
        return getClient().create(serviceClass);
    }
}
