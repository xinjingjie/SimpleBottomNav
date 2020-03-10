package com.example.simplebottomnav.repository;

import java.util.Random;

public class GetPicKey {
    static private String leastKey;

    static public String getFreshKey() {
        String[] strings = {"popular", "tea", "dog", "flower", "earth",
                "sky", "animals", "people", "nature", "ocean",
                "young", "spark", "sunset", "amazing", "tree",
                "happy", "rock", "happiness", "technology", "car",
                "bear", "girl", "beauty", "rain", "cloud",
                "sunrise", "romance", "children", "world", "plane"
        };
        leastKey = strings[new Random().nextInt(30)];
        return leastKey;
    }

    static public String getLeastKey() {
        return leastKey;
    }
}
