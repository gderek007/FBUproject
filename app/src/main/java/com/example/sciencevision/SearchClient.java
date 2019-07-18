package com.example.sciencevision;


import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class SearchClient {
    private AsyncHttpClient client;
    private String API_BASE_URL = "https://simple.wikipedia.org/w/api.php";
    public SearchClient() {
        client = new AsyncHttpClient();
    }
    public void getWiki(String searchLabel) {
        // create the url
        String url = API_BASE_URL;

        //set the request parameters
        RequestParams params = new RequestParams();
        params.put("format","json");
        params.put("action","query");
        params.put("prop","extracts");
        params.put("exintro","");
        params.put("explaintext","");
        params.put("redirects","1");
        params.put("titles",searchLabel);

        //execute a GET request, expect JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //Gets the page number for the search
                    String page=response.getJSONObject("query").getJSONObject("pages").names().get(0).toString();
                    //Description of what the searchLabel
                    String finalresponse=response.getJSONObject("query").getJSONObject("pages").getJSONObject(page).getString("extract");
                    //Easy Solution for grabbing the first sentence
                    String firstsentence=finalresponse.substring(0,finalresponse.indexOf(".")+1);
                    Log.d("Search Client",firstsentence);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private Set<String> getDataFromGoogle(String query) {

        Set<String> result = new HashSet<String>();
        String request = "https://www.google.com/search?q=" + query + "&num=20";
        System.out.println("Sending request..." + request);

        try {

            // need http protocol, set this as a Google bot agent :)
            Document doc = Jsoup
                    .connect(request)
                    .userAgent(
                            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(5000).get();

            // get all links
            Elements links = doc.select("a[href]");
            for (Element link : links) {

                String temp = link.attr("href");
                if(temp.startsWith("/url?q=")){
                    //use regex to get domain name
                    result.add(temp);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}
