package com.meetup.rxexperiments;

import android.content.Context;
import com.squareup.okhttp.HttpUrl;

public class API {
    private API() {
        throw new UnsupportedOperationException();
    }

    private static HttpUrl.Builder base() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.meetup.com");
    }

    public static HttpUrl recommendedGroups(Context context) {
        return base()
                .addPathSegment("recommended")
                .addPathSegment("groups")
                .addQueryParameter("fields", "self,self_status")
                .addQueryParameter("key", context.getString(R.string.meetup_api_key))
                .build();
    }
}
