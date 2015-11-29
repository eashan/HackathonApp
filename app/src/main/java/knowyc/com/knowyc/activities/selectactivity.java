package knowyc.com.knowyc.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import knowyc.com.knowyc.R;

public class selectactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectactivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selectactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void AadhaarLogin(View v) {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent i=new Intent(selectactivity.this,MainActivity.class);

            startActivity(i);

    }

    //product qr code mode
    public void BasicLogin(View v) {
        //start the scanning activity from the com.google.zxing.client.android.SCAN intent
        Intent i=new Intent(selectactivity.this,SmsActivity.class);

        startActivity(i);
    }
}
