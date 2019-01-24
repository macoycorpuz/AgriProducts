package thesis.agriproducts.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import thesis.agriproducts.R;
import thesis.agriproducts.util.Tags;
import thesis.agriproducts.util.Utils;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        BottomNavigationView navigation = findViewById(R.id.adminNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //region Navigation Listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_users:
                    Utils.switchContentAdmin(AdminActivity.this, R.id.adminContainer, Tags.USERS_FRAGMENT);
                    return true;
                case R.id.navigation_products:
                    Utils.switchContentAdmin(AdminActivity.this, R.id.adminContainer, Tags.PRODUCTS_FRAGMENT);
                    return true;
            }
            return false;
        }
    };
    //endregion
}