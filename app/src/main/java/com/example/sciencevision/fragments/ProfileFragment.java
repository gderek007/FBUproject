package com.example.sciencevision.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sciencevision.R;
import com.example.sciencevision.SearchClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        JsoupTask j = new JsoupTask();
        j.execute("hi");

        /*Set<String> result = client.getDataFromGoogle("mario");
        for(String temp : result){
            Log.d(ProfileFragment.class.getSimpleName(), temp);
        }*/
    }

    private class JsoupTask extends AsyncTask<String, Void, Set<String>> {


        @Override
        protected Set<String> doInBackground(String... params) {
            return getDataFromGoogle(params[0]);
        }

        protected void onPostExecute(Set<String> results) {
            for (String s : results) {
                Log.d(ProfileFragment.class.getSimpleName(), s);
                TextView tvText = (TextView) getView().findViewById(R.id.tvText);
                tvText.setText(tvText.getText() + s);
            }
        }


        public Set<String> getDataFromGoogle(String query) {

            Set<String> result = new HashSet<String>();
            String request = "https://www.google.com/search?q=" + query + "&num=20";
            System.out.println("Sending request..." + request);

            try {
                // need http protocol, set this as a Google bot agent :)
                Document doc = Jsoup
                        .connect(request)
                        .ignoreHttpErrors(true)
                        .userAgent(
                                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                        .timeout(5000).get();

                // get all links
                Elements links = doc.select("a[href]");
                for (Element link : links) {

                    String temp = link.attr("href");
                    if (temp.startsWith("/url?q=")) {
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
}
