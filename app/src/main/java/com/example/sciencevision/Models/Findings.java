package com.example.sciencevision.Models;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.sciencevision.R;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;



@ParseClassName("Findings")
public class Findings extends ParseObject {
    //Values that are in the Parse Database
    private static final String KEY_USER = "User";
    private static final String KEY_NAME = "ItemName";
    private static final String KEY_DESCRIPTION = "ItemDescription";
    private static final String KEY_FACT = "FunFact";
    private static final String KEY_IMAGE = "ItemImage";
    private static final String KEY_EXPERIMENT = "Experiment";
    private static final String KEY_CREATED = "createdAt";


    //Methods to get and set attributes for Findings
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String itemName) {
        put(KEY_NAME, itemName);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String itemDescription) {
        put(KEY_DESCRIPTION, itemDescription);
    }

    public String getFunFact() {
        return getString(KEY_FACT);
    }

    public void setFunFact(String funFact) {
        put(KEY_FACT, funFact);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseFile getProPic() {
        return getUser().getParseFile("ProfilePicture");
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public String getExperiment() {
        return getString(KEY_EXPERIMENT);
    }

    public void setExperiment(String experiment) {
        put(KEY_EXPERIMENT, experiment);
    }


    public String getNiceTime() {
        Date date = getCreatedAt();
        PrettyTime time = new PrettyTime();
        return time.format(date).toString();
    }

    //Query
    public static class Query extends ParseQuery<Findings> {
        public Query() {
            super(Findings.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include(KEY_USER);
            return this;
        }

        public Query getRecent() {
            getQuery(KEY_CREATED).orderByAscending(KEY_CREATED);
            return this;
        }

        public Query getOlder(int size) {
            getQuery(KEY_CREATED).orderByAscending(KEY_CREATED).setSkip(size);
            return this;
        }

        public Query getUser(ParseUser user) {
            whereEqualTo("User", user);
            return this;
        }
    }


    public static Callable<Findings> createFinding(final ParseUser User, final String ItemName, final String ItemDescription, final String FunFact, final ParseFile ItemImage, final String Experiment) {


        return new Callable<Findings>() {
            @Override
            public Findings call() throws Exception {
                final Findings newFinding = new Findings();
                newFinding.setUser(User);
                newFinding.setName(ItemName);
                newFinding.setDescription(ItemDescription);
                newFinding.setFunFact(FunFact);
                newFinding.setImage(ItemImage);
                newFinding.setExperiment(Experiment);

                User.increment("NumberOfFindings");
                ArrayList<Integer> badges = (ArrayList<Integer>) User.get("Badges");
                Integer numberOfBadge = Badge.getBadge((Integer) User.get("NumberOfFindings"));
                if (!badges.contains(numberOfBadge) && numberOfBadge != null) {
                    badges.add(numberOfBadge);
                    User.put("Badges", badges);
                    User.put("EarnBadge",Boolean.parseBoolean("True"));
                }

                User.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("increasingFindings", "Incrememnted findings");
                        } else {
                            e.printStackTrace();
                        }
                    }
                });

                newFinding.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("createFinding", "New Finding Success");
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                return newFinding;
            }
        };
    }

    @Override
    public String toString() {
        return getName() + "|" + getUser() + "|" + getDescription();
    }




}

