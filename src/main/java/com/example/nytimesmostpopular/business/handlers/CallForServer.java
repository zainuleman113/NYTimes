package com.example.nytimesmostpopular.business.handlers;

import android.util.Log;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.nytimesmostpopular.utils.CommonMethods;
import com.example.nytimesmostpopular.utils.CommonObjects;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class CallForServer {

    private String url;
    private OnServerResultNotifier onServerResultNotifier;
    private String NO_INTERNET = "No internet connection";
    private String API_BASE_URL = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/7.json?";

    public CallForServer(String url, OnServerResultNotifier onServerResultNotifier) {
        this.url = url;
        this.onServerResultNotifier = onServerResultNotifier;
    }

    public CallForServer(String url, OnServerResultNotifier onServerResultNotifier, String input) {
        this.url = url;
        this.onServerResultNotifier = onServerResultNotifier;
    }

    public interface OnServerResultNotifier {
        public void onServerResultNotifier(boolean isError, String response);
    }

    //Load data from server
    public void callForServerGet() {
        if (CommonMethods.isNetworkAvailable(CommonObjects.getContext())) {
            AndroidNetworking.initialize(CommonObjects.getContext());
            ANRequest.GetRequestBuilder getRequestBuilder = AndroidNetworking.get(API_BASE_URL+url);
            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .build();
            getRequestBuilder.setOkHttpClient(client);
            Log.d("Service_Response", url);
            getRequestBuilder.build().getAsString(new StringRequestListener() {
                @Override
                public void onResponse(String jsonStr) {
                    onServerResultNotifier.onServerResultNotifier(false,jsonStr);
                }

                @Override
                public void onError(ANError error) {
                    onServerResultNotifier.onServerResultNotifier(true,error.getErrorBody());
                }
            });
        } else {
            onServerResultNotifier.onServerResultNotifier(true, NO_INTERNET);
        }
    }

}
