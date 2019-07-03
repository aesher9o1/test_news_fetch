package com.aesher.test.news.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Constants {
    static  String SharedPreferenceName = "NewsFetch";

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
