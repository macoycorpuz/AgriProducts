package thesis.agriproducts.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import thesis.agriproducts.R;
import thesis.agriproducts.util.SharedPrefManager;
import thesis.agriproducts.util.Tags;
import thesis.agriproducts.util.Utils;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserLoggedIn();

        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void checkUserLoggedIn() {
        if(SharedPrefManager.getInstance().isLoggedIn(getApplicationContext()))
            Utils.switchContent(HomeActivity.this, R.id.fragContainer, Tags.HOME_FRAGMENT);
        else {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    //region Navigation Listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Utils.switchContent(HomeActivity.this, R.id.fragContainer, Tags.HOME_FRAGMENT);
                    return true;
                case R.id.navigation_sell:
                    Utils.switchContent(HomeActivity.this, R.id.fragContainer, Tags.SELL_FRAGMENT);
                    return true;
                case R.id.navigation_inbox:
                    Utils.switchContent(HomeActivity.this, R.id.fragContainer, Tags.INBOX_FRAGMENT);
                    return true;
                case R.id.navigation_my_products:
                    Utils.switchContent(HomeActivity.this, R.id.fragContainer, Tags.MY_PRODUCTS_FRAGMENT);
                    return true;
                case R.id.navigation_account:
                    Utils.switchContent(HomeActivity.this, R.id.fragContainer, Tags.ACCOUNT_FRAGMENT);
                    return true;
            }
            return false;
        }
    };
    //endregion
}
