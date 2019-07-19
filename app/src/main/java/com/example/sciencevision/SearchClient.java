package com.example.sciencevision;

import android.content.ClipData;

import android.util.Log;
import android.widget.TextView;


import com.example.sciencevision.fragments.FindingFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;

public class SearchClient {
    private FindingFragment findings= new FindingFragment();
    private AsyncHttpClient client;
    private String API_BASE_URL = "https://simple.wikipedia.org/w/api.php";

    public SearchClient() {
        client = new AsyncHttpClient();
    }

    public void getWiki(final ParseUser user, final String searchLabel, final String FunFact, final ParseFile ItemImage, final String Experiment, final TextView textView) {
        // create the url
        String url = API_BASE_URL;

        //set the request parameters
        RequestParams params = new RequestParams();
        params.put("format", "json");
        params.put("action", "query");
        params.put("prop", "extracts");
        params.put("exintro", "");
        params.put("explaintext", "");
        params.put("redirects", "1");
        params.put("titles", searchLabel);

        //execute a GET request, expect JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //Gets the page number for the search
                    String page = response.getJSONObject("query").getJSONObject("pages").names().get(0).toString();
                    //Description of what the searchLabel
                    String finalresponse = response.getJSONObject("query").getJSONObject("pages").getJSONObject(page).getString("extract");
                    //Easy Solution for grabbing the first sentence
                    String firstsentence = finalresponse.substring(0, finalresponse.indexOf(".") + 1);

                    Log.d("Search Client", searchLabel + ": " + firstsentence);
                    textView.setText(String.format(searchLabel + ": " + firstsentence));
                    findings.createFinding(user,searchLabel,firstsentence,FunFact, ItemImage,Experiment);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }


}
