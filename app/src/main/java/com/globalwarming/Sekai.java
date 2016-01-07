package com.globalwarming;

/**
 * Created by Tabuzaki IA on 12/24/2015.
 */
public class Sekai {
    private static Sekai instance = null;
    public String userName = "";
    private  Sekai() {

    }

    public static Sekai getInstance() {
        if (instance == null) {
            instance = new Sekai();
        }
        return instance;
    }

}
