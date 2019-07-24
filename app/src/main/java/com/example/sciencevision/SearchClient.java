package com.example.sciencevision;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SearchClient {
    private AsyncHttpClient client;

    public SearchClient() {
        client = new AsyncHttpClient();
    }

    public Callable<String> getWiki(final String searchLabel) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                HttpURLConnection urlConnection = null;
                URL url = new URL("https://simple.wikipedia.org/w/api.php?&format=json" +
                        "&action=query" +
                        "&prop=extracts" +
                        "&exintro=" +
                        "&explaintext=" +
                        "&redirects=1" +
                        "&titles=" + searchLabel);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                String jsonString = sb.toString();
                JSONObject response = new JSONObject(jsonString);
                String firstSentence = clipSentenceFromJSON(response);
                Log.d("Search Client", searchLabel + ": " + firstSentence);
                return firstSentence;
            }
        };
    }

    private String clipSentenceFromJSON(JSONObject response) {
        try {
            //Gets the page number for the search
            String page = response.getJSONObject("query").getJSONObject("pages").names().get(0).toString();
            //Description of what the searchLabel
            String finalresponse = response.getJSONObject("query").getJSONObject("pages").getJSONObject(page).getString("extract");
            //Easy Solution for grabbing the first sentence
            String firstsentence = finalresponse.substring(0, finalresponse.indexOf(".") + 1);
            return firstsentence;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Callable<String> getDataFromGoogle(final String query) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                List<String> result = new ArrayList<>();
                String request = "https://www.google.com/search?q=" + query + "&num=5";
                System.out.println("Sending request..." + request);

                try {
                    // need http protocol, set this as a Google bot agent :)
                    Document doc = Jsoup
                            .connect(request)
                            .ignoreHttpErrors(true)
                            .userAgent(
                                    "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                            .timeout(5000).get();

                    Elements links = doc.select("a[href]");
                    int counter = 0;
                    for (Element link : links) {
                        String temp = link.attr("href");
                        if (temp.startsWith("/url?q=") && counter < 5) {
                            //use regex to get domain name
                            result.add(temp);
                            counter++;
                        }
                    }

                    for (String s : result) {
                        Log.d("SearchClient", s);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result.toString();
            }
        };
    }


}
