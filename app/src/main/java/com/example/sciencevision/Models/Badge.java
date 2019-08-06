package com.example.sciencevision.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sciencevision.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;


import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;


public class Badge implements Parcelable {

    private String name;
    private int thumbnail;
    private String description;
    //Number of findings required to unlock the badge
    private int numberRequired;
    private static Dictionary<Integer, Badge> dictionaryBadges = new Hashtable<>();


    public Badge(String badgeName, String badgeDescription, int badgeThumbnail, Integer numberNeeded) {
        name = badgeName;
        description = badgeDescription;
        thumbnail = badgeThumbnail;
        numberRequired = numberNeeded;
    }

    protected Badge(Parcel in) {
        name = in.readString();
        thumbnail = in.readInt();
        description = in.readString();
        numberRequired = in.readInt();
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public int getNumberRequired() {
        return numberRequired;
    }

    // Returns a list of Badges
    private static ArrayList<Badge> createBadges() {
        ArrayList<Badge> badges = new ArrayList<>();
        badges.add(new Badge("New Account!", "Congratulations on creating an account!", R.drawable.badge, 0));
        badges.add(new Badge("First Finding", "Congratulations on your first finding!", R.drawable.badge, 1));
        badges.add(new Badge("Tenth Finding", "Congratulations on your first finding!", R.drawable.badge, 10));
        badges.add(new Badge("Twenty-Fifth Finding", "Congratulations on your first finding!", R.drawable.badge, 25));
        badges.add(new Badge("Seventy-Fifth Finding", "Congratulations on your first finding!", R.drawable.badge, 75));
        badges.add(new Badge("Hundreadth Finding", "Congratulations on your first finding!", R.drawable.badge, 100));

        return badges;
    }

    private static Dictionary<Integer, Badge> getDictionary() {
        ArrayList<Badge> badges = createBadges();

        for (int i = 0; i < badges.size(); i++) {
            dictionaryBadges.put(badges.get(i).getNumberRequired(), badges.get(i));
        }
        return dictionaryBadges;
    }

    public static ArrayList<Badge> getBadge(ArrayList<Integer> numberOfBadges) {
        Dictionary<Integer, Badge> badges = getDictionary();
        ArrayList<Badge> badgesAcquired = new ArrayList<>();

        for (int j = 0; j < numberOfBadges.size(); j++) {
            badgesAcquired.add(badges.get(numberOfBadges.get(j)));
        }
        return badgesAcquired;


    }


    //Returns a n Integer that is associated with a certain Badge
    public static Integer getBadge(int numberForBadge) {
        if (numberForBadge == 0) {
            return 0;
        } else if (numberForBadge == 1) {
            return 1;
        } else if (numberForBadge == 2) {
            return 2;
        }else if (numberForBadge == 3) {
            return 3;
        }else if (numberForBadge == 4) {
            return 4;
        }else {
            return null;
        }
    }

    public static final Creator<Badge> CREATOR = new Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel in) {
            return new Badge(in);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(thumbnail);
        dest.writeString(description);
        dest.writeInt(numberRequired);
    }
}


