package com.example.sciencevision.Models;

import com.example.sciencevision.R;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

//@ParseClassName("Badges")
public class Badge{

    private String name;
    private int thumbnail;
    private String description;
    //Number of findings required to unlock the badge
    private int numberRequired;

    public Badge(String badgeName, String badgeDescription, int badgeThumbnail, Integer numberNeeded) {
//        ParseObject.create("Badges");
//        super();
        name = name;
        description = description;
        thumbnail = badgeThumbnail;
        numberRequired = numberNeeded;
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
    private static ArrayList<Badge> getBadges() {
        ArrayList<Badge> badges = new ArrayList<>();
        badges.add(new Badge("New Account!", "Congratulations on creating an account!", R.drawable.badge, 0));
        badges.add(new Badge("First Finding", "Congratulations on your first finding!", R.drawable.badge, 1));
        return badges;
    }

    //Returns a n Integer that is associated with a certain
    public static Integer getBadge(int numberForBadge) {
        if (numberForBadge == 0) {
            return 0;
        } else if (numberForBadge == 1) {
            return 1;
        } else {
            return null;
        }
    }

}
