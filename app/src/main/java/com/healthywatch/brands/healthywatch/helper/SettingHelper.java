package com.healthywatch.brands.healthywatch.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by brandon on 04/05/16.
 */
public class SettingHelper {
    private static String TAG = SettingHelper.class.getSimpleName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "HealthyWatch";
    private static final String ONBOARD = "onBoard";

    public SettingHelper(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setOnboard(boolean option){
        editor.putBoolean(ONBOARD,option);
        editor.commit();
    }
    public boolean getOnboard(){
        return  pref.getBoolean(ONBOARD,true);
    }

}
