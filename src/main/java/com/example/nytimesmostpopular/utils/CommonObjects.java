package com.example.nytimesmostpopular.utils;

import android.content.Context;

import com.example.nytimesmostpopular.business.objects.NYMostPopularResoponse;

public class CommonObjects {
    private static Context context;
    private static NYMostPopularResoponse response;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        CommonObjects.context = context;
    }

    public static NYMostPopularResoponse getResponse() {
        return response;
    }

    public static void setResponse(NYMostPopularResoponse response) {
        CommonObjects.response = response;
    }
}