package com.logicals.compratodo.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicals.compratodo.customer.mainhomeactivity.UserHomeActivity;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.login_signup.VendorLoginActivity;
import com.logicals.compratodo.model.UserDTO;

import java.lang.reflect.Type;

/**
 * Created by VARUN on 01/01/19.
 */
public class SharedPrefrence {
    public static SharedPreferences myPrefs;
    public static SharedPreferences.Editor prefsEditor;
    public static SharedPrefrence myObj;
    private Context _context;
    public SharedPrefrence(Context context) {
        this._context=context;
    }

    public void clearAllPreferences() {
        prefsEditor = myPrefs.edit();
        prefsEditor.clear();
        prefsEditor.commit();
    }


    public static SharedPrefrence getInstance(Context ctx) {
        if (myObj == null) {
            myObj = new SharedPrefrence(ctx);
            myPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            prefsEditor = myPrefs.edit();
        }
        return myObj;
    }

    public void clearPreferences(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }


    public void setchooseRole(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.apply();
    }

    public int getchooseRole(String Tag) {
        return myPrefs.getInt(Tag, 0);
    }




    public void setIntValue(String Tag, int value) {
        prefsEditor.putInt(Tag, value);
        prefsEditor.apply();
    }

    public int getIntValue(String Tag) {
        return myPrefs.getInt(Tag, 0);
    }

    public void setLongValue(String Tag, long value) {
        prefsEditor.putLong(Tag, value);
        prefsEditor.apply();
    }

    public long getLongValue(String Tag) {
        return myPrefs.getLong(Tag, 0);
    }


    public void setValue(String Tag, String token) {
        prefsEditor.putString(Tag, token);
        prefsEditor.commit();
    }



    public String getValue(String Tag) {
        if (Tag.equalsIgnoreCase(Consts.LATITUDE))
            return myPrefs.getString(Tag, "");
        else if (Tag.equalsIgnoreCase(Consts.LONGITUDE))
            return myPrefs.getString(Tag, "");
        return myPrefs.getString(Tag, "");
    }


    public boolean getBooleanValue(String Tag) {
        return myPrefs.getBoolean(Tag, false);

    }

    public void setBooleanValue(String Tag, boolean token) {
        prefsEditor.putBoolean(Tag, token);
        prefsEditor.commit();
    }

    public void setParentUser(UserDTO userDTO, String tag) {
        Gson gson = new Gson();
        String hashMapString = gson.toJson(userDTO);

        prefsEditor.putString(tag, hashMapString);
        prefsEditor.apply();
    }

    public UserDTO getParentUser(String tag) {
        String obj = myPrefs.getString(tag, "defValue");
        if (obj.equals("defValue")) {
            return new UserDTO();
        } else {
            Gson gson = new Gson();
            String storedHashMapString = myPrefs.getString(tag, "");
            Type type = new TypeToken<UserDTO>() {
            }.getType();
            UserDTO testHashMap = gson.fromJson(storedHashMapString, type);
            return testHashMap;
        }
    }

    public boolean isLoggedIn() {
        return myPrefs.getBoolean(Consts.IS_LOGIN, false);
    }

    public boolean setLogin(boolean isLoggedIn) {
        prefsEditor.putBoolean(Consts.IS_LOGIN, isLoggedIn);
        prefsEditor.commit();
        return isLoggedIn;
    }

    public void logout() {
        prefsEditor.clear();
        prefsEditor.apply();
        setLogin(false);
        Intent showLogin = new Intent(_context, UserHomeActivity.class);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(showLogin);
    }

    public void logout2() {
        prefsEditor.clear();
        prefsEditor.apply();
        setLogin(false);
        Intent showLogin = new Intent(_context, VendorLoginActivity.class);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        showLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(showLogin);
    }


}
