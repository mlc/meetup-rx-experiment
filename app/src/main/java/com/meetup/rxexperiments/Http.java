package com.meetup.rxexperiments;

import android.content.Context;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import okio.BufferedSource;
import rx.Observable;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Http {
    private static final long CACHE_SIZE = 10 * 1024 * 1024; // 10 MByte
    private static volatile Http instance;

    private final OkHttpClient httpClient;

    private Http(Context context) {
        OkHttpClient cli = new OkHttpClient();
        cli.setConnectTimeout(20, TimeUnit.SECONDS);
        cli.setReadTimeout(15, TimeUnit.SECONDS);
        cli.setFollowSslRedirects(false);
        cli.setFollowRedirects(false);
        cli.setCache(new Cache(new File(context.getCacheDir(), "http"), CACHE_SIZE));
        httpClient = cli;
    }

    public static Http getInstance(Context context) {
        if (instance == null) {
            synchronized (Http.class) {
                if (instance == null) {
                    instance = new Http(context);
                }
            }
        }
        return instance;
    }

    public <T> Observable<T> get(HttpUrl url, final Class<? extends T> resultKlass) {
        Request req = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call httpCall = httpClient.newCall(req);

        return Observable.create(subscriber -> httpCall.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                subscriber.onError(e);
            }

            @Override
            public void onResponse(Response response) {
                try (BufferedSource data = response.body().source()) {
                    if (response.isSuccessful()) {
                        JsonParser jp = Json.getJsonFactory().createParser(data.inputStream());
                        JsonToken tok = jp.nextToken();
                        if (tok == JsonToken.START_ARRAY) {
                            while (jp.nextToken() != JsonToken.END_ARRAY) {
                                subscriber.onNext(jp.readValueAs(resultKlass));
                            }
                        } else if (tok == JsonToken.START_OBJECT) {
                            subscriber.onNext(jp.readValueAs(resultKlass));
                        }
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new HttpException(response));
                    }
                } catch (IOException ex) {
                    subscriber.onError(ex);
                }
            }
        }));
    }
}
