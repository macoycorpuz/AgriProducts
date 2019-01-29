package thesis.agriproducts.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import thesis.agriproducts.AppController;
import thesis.agriproducts.view.fragment.AccountFragment;
import thesis.agriproducts.view.fragment.AdminFragment;
import thesis.agriproducts.view.fragment.HomeFragment;
import thesis.agriproducts.view.fragment.InboxFragment;
import thesis.agriproducts.view.fragment.MessagesFragment;
import thesis.agriproducts.view.fragment.MyProductsFragment;
import thesis.agriproducts.view.fragment.ProductDetailsFragment;
import thesis.agriproducts.view.fragment.SellFragment;

//Class for usable functions
public class Utils {

    private static int productId = 0;
    private static int dealId = 0;
    private static int userId = 0;
    private static boolean isMyProduct = false;
    private static String CURRENT_TAG = null;
    private static Utils utils;

    public static Utils getUtils() {

        if (null == utils) {
            utils = new Utils();
        }
        return utils;
    }

    //region UI Interaction
    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    public static void dismissProgressDialog(ProgressDialog pDialog) {
        if (pDialog != null) pDialog.dismiss();
    }

    public void showProgress(final boolean show, final View progressView, final View goneForm) {
        int shortAnimTime = AppController.getInstance().resources.getInteger(android.R.integer.config_shortAnimTime);

        goneForm.setVisibility(show ? View.GONE : View.VISIBLE);
        goneForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                goneForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //endregion

    //region Authentication
    public boolean isEmailValid(String email) {
        if (email == null) return false;
        return (!TextUtils.isEmpty(email)) && (email.contains("@"));
    }

    public boolean isPasswordValid(String password) {
        return (!TextUtils.isEmpty(password)) && (password.length() > 8);
    }

    public boolean isEmptyFields(String... fields) {
        for(String f : fields) {
            if(f.isEmpty()) return false;
        }
        return true;
    }
    //endregion

    //region Real Path
    public String getRealPathFromURI(Context context, Uri contentUri) {
        String result;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        } catch (Exception ex) {
            result = ex.getMessage();
        }
        return result;
    }
    //endregion

    //region Fragment Util
    public static void switchContent(FragmentActivity baseActivity, int id, String TAG) {

        Fragment fragmentToReplace = new HomeFragment();
        FragmentManager fragmentManager = baseActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!TAG.equals(CURRENT_TAG)) {
            Fragment fragment = fragmentManager.findFragmentByTag(TAG);
            if (fragment == null) {

                if (TAG.equals(Tags.HOME_FRAGMENT)) {
                    fragmentToReplace = new HomeFragment();
                } else if (TAG.equals(Tags.SELL_FRAGMENT)) {
                    fragmentToReplace = new SellFragment();
                } else if (TAG.equals(Tags.INBOX_FRAGMENT)) {
                    fragmentToReplace = new InboxFragment();
                } else if (TAG.equals(Tags.MY_PRODUCTS_FRAGMENT)) {
                    fragmentToReplace = new MyProductsFragment();
                } else if (TAG.equals(Tags.ACCOUNT_FRAGMENT)) {
                    fragmentToReplace = new AccountFragment();
                } else if (TAG.equals(Tags.PRODUCT_DETAILS_FRAGMENT)) {
                    fragmentToReplace = new ProductDetailsFragment();
                    ((ProductDetailsFragment) fragmentToReplace).setProductId(productId);
                    ((ProductDetailsFragment) fragmentToReplace).setIsMyProduct(isMyProduct);
                } else if (TAG.equals(Tags.MESSAGES_FRAGMENT)) {
                    fragmentToReplace = new MessagesFragment();
                    ((MessagesFragment) fragmentToReplace).setDealId(dealId);
                    ((MessagesFragment) fragmentToReplace).setProductId(productId);
                }
            } else {
                if (TAG.equals(Tags.HOME_FRAGMENT)) {
                    fragmentToReplace = (HomeFragment) fragment;
                } else if (TAG.equals(Tags.SELL_FRAGMENT)) {
                    fragmentToReplace = (SellFragment) fragment;
                } else if (TAG.equals(Tags.INBOX_FRAGMENT)) {
                    fragmentToReplace = (InboxFragment) fragment;
                } else if (TAG.equals(Tags.MY_PRODUCTS_FRAGMENT)) {
                    fragmentToReplace = (MyProductsFragment) fragment;
                } else if (TAG.equals(Tags.ACCOUNT_FRAGMENT)) {
                    fragmentToReplace = (AccountFragment) fragment;
                } else if (TAG.equals(Tags.PRODUCT_DETAILS_FRAGMENT)) {
                    fragmentToReplace = (ProductDetailsFragment) fragment;
                    ((ProductDetailsFragment) fragmentToReplace).setProductId(productId);
                    ((ProductDetailsFragment) fragmentToReplace).setIsMyProduct(isMyProduct);
                } else if (TAG.equals(Tags.MESSAGES_FRAGMENT)) {
                    fragmentToReplace = (MessagesFragment) fragment;
                    ((MessagesFragment) fragmentToReplace).setDealId(dealId);
                    ((MessagesFragment) fragmentToReplace).setProductId(productId);
                }
            }

            CURRENT_TAG = TAG;
            transaction.replace(id, fragmentToReplace, TAG);
            transaction.commit();

        }
    }

    public static void switchContentAdmin(FragmentActivity baseActivity, int id, String TAG) {
        Fragment adminFragment = new AdminFragment();
        FragmentManager fragmentManager = baseActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ((AdminFragment) adminFragment).setTag(TAG);
        transaction.replace(id, adminFragment, TAG);
        transaction.commit();
    }

    public static void setProductId(int productId) {
        Utils.productId = productId;
    }

    public static void setDealId(int dealId) {
        Utils.dealId = dealId;
    }

    public static void setUserId(int userId) {
        Utils.userId = userId;
    }

    public static void setIsMyProduct(boolean isMyProduct) {Utils.isMyProduct = isMyProduct; }
    //endregion

}