package com.example.sciencevision;

import android.util.Log;

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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;


public class SearchClient {

    HashMap<String, String> funFacts;

    public SearchClient() {
        funFacts = new HashMap<String, String>();
        funFacts.put("Plastic bottle", "It takes about 450 years just for one plastic bottle to break down in the ground.");
        funFacts.put("Bottle", "It takes about 450 years just for one plastic bottle to break down in the ground.");
        funFacts.put("Skin", "The average person’s skin covers an area of 2 square meters.");
        funFacts.put("Hand", "The hand has 27 bones.");
        funFacts.put("Handbag", "The Average Handbag Weighs 6.7 Pounds");
        funFacts.put("Banana", "Bananas can help lower blood pressure and protect heart health due to high potassium and low salt content");
        funFacts.put("Food", "Honey is the only edible food that never goes bad.");
        funFacts.put("Desk", "The average workplace desk can be 100x less hygienic than your kitchen table, and 400x filthier than the average toilet seat!");
        funFacts.put("Water", "There is the same amount of water on Earth as there was when the Earth was formed.");
        funFacts.put("Apple", "Apples are made of 25% air, which is why they float.");
        funFacts.put("Tableware", "According to recent food science, tableware (plates, cups, and cutlery) affect the taste of food as do other factors like color, smell, sound, and weight.");
        funFacts.put("Computer", "RAM stands for Random Access Memory, and this is the place on a computer where everything that’s used all the time is stored and can be found quickly.");



    }

    public Callable<String> getWiki(final String searchLabel) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                URL url = new URL("https://simple.wikipedia.org/w/api.php?&format=json" +
                        "&action=query" +
                        "&prop=extracts" +
                        "&exintro=" +
                        "&explaintext=" +
                        "&redirects=1" +
                        "&titles=" + searchLabel);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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

                if (firstSentence == null) {
                    firstSentence = "No description found.";
                    Log.d("Search Client", searchLabel + ": " + firstSentence);
                } else {
                    Log.d("Search Client", searchLabel + ": " + firstSentence);
                }
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
            return "";
        }
    }

    public Callable<String> getFactsFromGoogle(final String query) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (funFacts.get(query) != null) {
                    return funFacts.get(query);
                }
                List<String> result = new ArrayList<>();
                String request = "https://www.google.com/search?q=" + query + "+fun+facts" + "&num=5";
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
                            int starting = 7;

                            if (temp.contains("google")) {
                                starting = 48;
                            }
                            for (int i = starting; i < temp.length(); i++) {
                                if ((temp.charAt(i) == '&' || temp.charAt(i) == '%') && !(temp.contains("youtube"))) {
                                    result.add(temp.substring(starting, i));
                                    counter++;
                                    break;
                                }
                            }

                        }
                    }
                    //if (request.contains("facts")) {
                    try {
                        //String request2 = "http://boilerpipe-web.appspot.com/extract?url=" + result.get(0) + "output=htmlFragment";
                        Document doc2 = Jsoup
                                .connect(result.get(0))
                                .timeout(5000).get();
                        Log.d(SearchClient.class.getSimpleName(), "It worked!");
                        String allText = doc2.body().text();
                        if ((doc2.body().getElementsByTag("p").text().trim() != null) && (doc2.body().getElementsByTag("p").text().trim() != "")) {
                            allText = doc2.body().getElementsByTag("p").text();
                        }
                        if (allText.substring(0, allText.indexOf(".") + 1).length() > 5) {
                            return allText.substring(0, allText.indexOf(".") + 1);
                        } else {
                            return "No fun facts found.";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(SearchClient.class.getSimpleName(), "Failure extracting fun fact");
                    }

                    //}

                    for (String s : result) {
                        Log.d("SearchClient", s);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "No fun facts found.";
                }

                return result.get(0);
            }
        };
    }

    public Callable<String> getExperimentUrl(final String query) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                List<String> result = new ArrayList<>();
                String request = "https://www.google.com/search?q=" + query + "+kids+science+experiments" + "&num=5";
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
                            int starting = 7;
                            if (temp.contains("google")) {
                                starting = 48;
                            }
                            for (int i = starting; i < temp.length(); i++) {
                                if ((temp.charAt(i) == '&' || temp.charAt(i) == '%') && !(temp.contains("youtube"))) {
                                    result.add(temp.substring(starting, i));
                                    counter++;
                                    break;
                                }
                            }

                        }
                    }

                    for (String s : result) {
                        Log.d("SearchClient", s);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "Experiment not found";
                }

                return result.get(0);
            }
        };
    }


}
