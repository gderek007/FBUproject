package com.example.sciencevision.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("ProfilePictures")
public class ProfilePictures extends ParseObject{
    private static final String KEY_AVATAR= "Avatar";
    public ParseFile getAvatar(){return getParseFile(KEY_AVATAR);}
    public static class Query extends ParseQuery<ProfilePictures> {
        public Query() {
            super(ProfilePictures.class);
        }
        public ProfilePictures.Query getTop(){
            setLimit(20);
            return this; }

    }

}
