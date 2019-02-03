package thesis.agriproducts.util;

import android.content.Context;
import android.content.SharedPreferences;
import thesis.agriproducts.AppController;
import thesis.agriproducts.model.entities.User;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "agriprodsharedpref";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_NUMBER = "number";
    private static final String KEY_USER_ADDRESS = "address";
    private static final String KEY_ADMIN_ID = "adminId";
    private static final String KEY_ADMIN_NAME = "adminName";
    private static final String KEY_ADMIN_EMAIL = "adminEmail";

    private static SharedPrefManager sharedPrefManager = new SharedPrefManager();

    public static SharedPrefManager getInstance() {
        return sharedPrefManager;
    }

    public void userLogin(Context mCtx, User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NUMBER, user.getNumber());
        editor.putString(KEY_USER_ADDRESS, user.getAddress());
        editor.apply();
    }

    public void adminLogin(Context mCtx, User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ADMIN_ID, user.getUserId());
        editor.putString(KEY_ADMIN_NAME, user.getName());
        editor.putString(KEY_ADMIN_EMAIL, user.getEmail());
        editor.apply();
    }

    public boolean isLoggedIn(Context mCtx) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return (sharedPreferences.getInt(KEY_USER_ID, 0) != 0);
    }

    public User getUser(Context mCtx) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_USER_ID, 0),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null),
                sharedPreferences.getString(KEY_USER_NUMBER, null),
                sharedPreferences.getString(KEY_USER_ADDRESS, null)
        );
    }

    public User getAdmin(Context mCtx) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ADMIN_ID, 0),
                sharedPreferences.getString(KEY_ADMIN_NAME, null),
                sharedPreferences.getString(KEY_ADMIN_EMAIL, null)
        );
    }

    public void logout(Context mCtx) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}