package knowyc.com.knowyc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

import knowyc.com.knowyc.R;
import knowyc.com.knowyc.helper.PrefManager;


public class MyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private PrefManager pref;
    private TextView name, email, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        mobile = (TextView) findViewById(R.id.mobile);

        // enabling toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pref = new PrefManager(getApplicationContext());


        // Checking if user session
        // if not logged in, take user to sms screen
        if (!pref.isLoggedIn()) {
            logout();
        }

        // Displaying user information from shared preferences
        HashMap<String, String> profile = pref.getUserDetails();
        name.setText("Name: " + profile.get("name"));
        email.setText("Email: " + profile.get("email"));
        mobile.setText("Mobile: " + profile.get("mobile"));
    }

    /**
     * Logging out user
     * will clear all user shared preferences and navigate to
     * sms activation screen
     */
    private void logout() {
        pref.clearSession();

        Intent intent = new Intent(MyActivity.this, SmsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
