package com.example.nytimesmostpopular.business.handlers;

import android.util.Log;

import com.example.nytimesmostpopular.business.objects.NYMostPopularResoponse;
import com.example.nytimesmostpopular.utils.CommonMethods;
import com.example.nytimesmostpopular.utils.CommonObjects;
import com.google.gson.Gson;


public class AppHandler {

    public interface MostPopularListener {
        public void onGetList(NYMostPopularResoponse nyMostPopularResoponse);
        public void onError(String error);
    }


    //Call to fetch data
    public static void getMostPopular(final MostPopularListener mostPopularListener) {
        new CallForServer("api-key=34aa0d10068348748a77126d513d15e9" , new CallForServer.OnServerResultNotifier() {
            @Override
            public void onServerResultNotifier(boolean isError, String response) {
                 if(!isError) {
                    try {
                        NYMostPopularResoponse nyMostPopularResoponse = new Gson().fromJson(response, NYMostPopularResoponse.class);
                        if (nyMostPopularResoponse.getStatus().equals("OK")) {
                            mostPopularListener.onGetList (nyMostPopularResoponse);
                        } else {
                            mostPopularListener.onError(nyMostPopularResoponse.getStatus());
                        }
                    } catch (Exception e) {
                        mostPopularListener.onError(e.getMessage());
                    }
                }
                else
                {
                    mostPopularListener.onError(response);
                }
            }
        }).callForServerGet();
    }
}

