package com.healthywatch.brands.healthywatch.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by brandon on 06/05/16.
 */
public class SessionHelper {
    private static String TAG = SessionHelper.class.getSimpleName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "HealthyWatch";
    private static final String USERID = "userId";
    private static final String LOGGEDIN = "logedIn";

    public SessionHelper(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserId(String value){
        editor.putString(USERID,value);
        editor.commit();
    }
    public String getUserId(){
        return  pref.getString(USERID,"");
    }
    public void setLogedIn(boolean option){
        editor.putBoolean(LOGGEDIN,option);
        editor.commit();
    }
    public boolean getLogedIn(){
        return  pref.getBoolean(LOGGEDIN,false);
    }
}
