package knowyc.com.knowyc.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import knowyc.com.knowyc.R;

/**
 * Created by vaibhavnagda on 29/11/15.
 */
public class UpdateAddressActivity extends AppCompatActivity implements View.OnClickListener{


    private Button button_yes, button_no, update_address;
    private EditText ed;
    private EditText address_editText;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private int returnCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);



        ed = (EditText) findViewById(R.id.ediaddr);
        address_editText = (EditText) findViewById(R.id.ediaddr);
        button_no = (Button) findViewById(R.id.correct_address_image_button);
        button_yes = (Button) findViewById(R.id.wrong_address_image_button);
        update_address = (Button) findViewById(R.id.btn_update_address);
        progressBar = (ProgressBar) findViewById(R.id.updating_address_progressbar);
        relativeLayout = (RelativeLayout) findViewById(R.id.update_address_relativeLayout);

        update_address.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        button_yes.setOnClickListener(this);
        button_no.setOnClickListener(this);
        update_address.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.correct_address_image_button)){
            Intent i = new Intent(this, DocumentsSubmissionActivity.class);
            startActivity(i);
        }

        if (view == findViewById(R.id.wrong_address_image_button)){
            button_no.setVisibility(View.INVISIBLE);
            button_yes.setVisibility(View.INVISIBLE);
            update_address.setVisibility(View.VISIBLE);
        }

        if (view == findViewById(R.id.btn_update_address)){
            String address = address_editText.getText().toString();

            Intent i = getIntent();
            String aadhaar_no = i.getStringExtra("aadhaar_no");

            String[] params = {aadhaar_no, address};

            new postAddressAsyncTask().execute(params);


        }
    }

    private class postAddressAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            String returned_message = "null response";

            try {
                JSONObject jsonObject = new JSONObject();
                String aadhaar_no = params[0];
                String address = params[1];

                jsonObject.put("aadhaar", aadhaar_no);
                jsonObject.put("naddress", address);

                String url = "knowyc.herokuapp.com/update_address";
                String data = jsonObject.toString();
                String result;

                HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                httpcon.connect();

                //write
                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();

                //Read
                BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                result = sb.toString();

                JSONObject response = new JSONObject(result);
                httpcon.disconnect();

                returned_message = response.getString("message");
                returnCode = response.getInt("flag");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return returned_message;
        }

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.animate();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String returned_message) {

            progressBar.setEnabled(false);
            progressBar.setVisibility(View.GONE);

            if (returnCode == 0) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Address Updated!", Snackbar.LENGTH_LONG);
                snackbar.show();

                Intent i = new Intent(UpdateAddressActivity.this, DocumentsSubmissionActivity.class);
                startActivity(i);
            }

            super.onPreExecute();
        }


    }
}
