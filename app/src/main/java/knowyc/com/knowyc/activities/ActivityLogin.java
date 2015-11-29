package knowyc.com.knowyc.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.parse.ParseObject;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;

import knowyc.com.knowyc.R;

/**
 * Created by Sumod on 29-Nov-15.
 */
public class ActivityLogin extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_activity, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_request_otp_activity, container, false);

            Button b1 = (Button) rootView.findViewById(R.id.send_otp_button);
            //sends the otp. updates text view to 'otp sent'
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = getActivity().getIntent();
                    String uid = intent.getStringExtra("uid");
                    ((EditText) getActivity().findViewById(R.id.aadhar_user_input_otp)).setText(uid);
                    if(uid.length() != 12) {
                        Toast toast = Toast.makeText(getActivity(), "Please enter a valid 12-digit aadhar number", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    String son1 = "{ \"aadhaar-id\": \""+uid+"\", \"channel\":\"SMS\", \"location\": { \"type\": \"gps\", \"latitude\": \"73.2\", \"longitude\": \"22.3\", \"altitude\": \"0\" } }";

                    String[] inputArr = new String[] {
                            uid,
                            son1,
                    };

                    TextView t1 = (TextView) rootView.findViewById(R.id.out);
                    t1.setText(("Sending OTP."));

                    String res[] = new String[0];
                    try {
                        res = new RequestOTPTask().execute(uid, son1).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    t1.setText(res[0]);

                }
            });

            //confirms otp. if true then sets textview to 'otp entered correctly', 'incorrect*' otherwise.
            //to do parse json output.
            Button b2 = (Button) rootView.findViewById(R.id.check_otp_button);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("I am here", "YE");
                    //get the various required fields
                    String otp = ((EditText) rootView.findViewById(R.id.enter_otp)).getText().toString();

                    if(otp.length() != 6) {
                        Toast toast = Toast.makeText(getActivity(), "Please enter a valid 6-digit OTP", Toast.LENGTH_SHORT);
                        toast.show();
                        ((EditText) rootView.findViewById(R.id.enter_otp)).setText("");
                        return;
                    }
                    String aadhar = ((EditText) rootView.findViewById(R.id.aadhar_user_input_otp)).getText().toString();
                    String son1 = "{\"consent\":\"Y\", \"auth-capture-request\":{\"aadhaar-id\": \""+aadhar+"\", \"modality\":\"otp\", \"otp\":\""+otp+"\", \"device-id\":\"MAC\", \"certificate-type\":\"preprod\",  \"location\": { \"type\": \"gps\", \"latitude\": \"73.2\", \"longitude\": \"22.3\", \"altitude\": \"0\" } } }";
                    TextView t1 = (TextView) rootView.findViewById(R.id.out);


                    String[] inputArr = new String[] {
                            aadhar,
                            son1,
                    };

                    t1.setText(("Checking Password"));

                    String res[] = new String[7];
                    try {
                        res = new CheckOTPTask().execute(aadhar, son1).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    // TODO change to ==.. for dev purposes!!!! :DDDD:
                    if (res[6] == "true" || res[6] != "true"){
                        t1.setText("Logged IN!");

                        try {
                            // TODO add parse user: db here!
                            ParseObject newUser = new ParseObject("Users");
                            //newUser.put("objectId", "bar"/*res[0]*/);
                            newUser.put("ID", res[0]);
                            newUser.put("Name", res[4]);
                            newUser.put("Address", res[5]);
                            newUser.put("Photo", res[1]);
                            //TODO add more fields
                            newUser.saveInBackground();
                        }
                        catch (Exception e)
                        {
                            Log.e("Exception", "Data Invalid");
                        }

                        Log.d("VAL", "value in users stored");


                        // Welcome new user!
                        Toast toast = Toast.makeText(getActivity(), "Welcome " + res[4], Toast.LENGTH_SHORT);
                        toast.show();


                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("SIGNUP_DONE", true);
                        editor.commit();

                        // Activity calling!!

                        Intent intent = new Intent(getActivity(), UpdateAddressActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getActivity().finish();
                    }
                    else{
                        t1.setText("Try Again!");
                    }
                }
            });

            return rootView;
        }

        public static class RequestOTPTask extends AsyncTask<String, Void, String[]> {

            @Override
            protected String[] doInBackground(String... params) {
                Log.d("I am here", "YE");
                //get the various required fields

                String son1 = params[1];
                String []resulting = new String[1];
                Boolean otp_sent = false;
                try {
                    String url = "https://ac.khoslalabs.com/hackgate/hackathon/otp";
                    HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.setConnectTimeout(10000);
                    httpcon.connect();
                    //write
                    OutputStream os = httpcon.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(son1.toString());
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);

                    }

                    br.close();
                    String result = sb.toString();

                    JSONObject response = new JSONObject(result);
                    if(result.indexOf("success\":true") > 0) {
                        otp_sent= true;
                        resulting[0] = "Sent!";
                    }
                    if(result.indexOf("success\":false") > 0) {
                        otp_sent= true;
                        resulting[0] = "Invalid Aadhar Number!";
                    }

                    Log.d("AsyncTask", "done done Details Correct");
                    httpcon.disconnect();

                    int returncode = response.getInt("returnCode");
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if(otp_sent==true) {Log.d("AsyncTask", "OTP succesfully sent");return resulting; }
                resulting[0] = "Check Connectivity.";
                return resulting;

            }
        }

        public static class CheckOTPTask extends AsyncTask<String, Void, String[]> {
            @Override
            protected String[] doInBackground(String... params) {

                String son1 = params[1];
                Boolean otp_sent = false;
                String[] output = new String[7];
                try{
                    String url = "https://ac.khoslalabs.com/hackgate/hackathon/kyc/raw";
                    HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
                    httpcon.setDoOutput(true);
                    httpcon.setRequestProperty("Content-Type", "application/json");
                    httpcon.setRequestProperty("Accept", "application/json");
                    httpcon.setRequestMethod("POST");
                    httpcon.setConnectTimeout(10000);
                    httpcon.connect();
                    //write
                    OutputStream os = httpcon.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(son1.toString());
                    writer.close();
                    os.close();

                    //Read
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);

                    }

                    br.close();
                    String result = sb.toString();
                    JSONObject response = new JSONObject(result);
                    /*Checking response */
                    if(response!=null){
                        Log.d("RESPONSE", "FOUND");
                        Log.d("stream", result); //this contains the fat json response


                        if(result.indexOf("photo") > 0) {
                            otp_sent= true;
                            String goon =  "aadhaar";


                            JSONObject object = response;
                            String syncresponse = object.getString("kyc");
                            JSONObject object2 = new JSONObject(syncresponse);
                            output[0]= object2.getString("aadhaar-id");
                            output[1]= object2.getString("photo");
                            String poi= object2.getString("poi");
                            JSONObject object21 = new JSONObject(poi);
                            output[2] = object21.getString("gender");
                            output[4] = object21.getString("name");
                            output[3] = object21.getString("dob");
                            String poa = object2.getString("poa");
                            object21 = new JSONObject(poa);
                            output[5] = "";
                            if(object21.has("house")) {
                                if(output[5].length()>0){output[5]= output[5] + ", ";}
                                output[5] = output[5] + object21.getString("house");
                            }
                            if(object21.has("street")) {
                                if(output[5].length()>0) {
                                    output[5]= output[5] + ", ";}
                                output[5] = output[5] + object21.getString("street");
                            }
                            if(object21.has("lm")) {
                                if(output[5].length()>0){output[5] =output[5] + ", ";}
                                output[5] = output[5] + object21.getString("lm");
                            }
                            if(object21.has("loc")) {
                                if(output[5].length()>0){output[5]= output[5] + ", ";}
                                output[5] = output[5] + object21.getString("loc");
                            }
                            if(object21.has("vtc")) {
                                if(output[5].length()>0){output[5]= output[5] + ", ";}
                                output[5] = output[5] + object21.getString("vtc");
                            }
                            if(object21.has("subdist")) {
                                if(output[5].length()>0){output[5] = output[5] + ", ";}
                                output[5] = output[5] + object21.getString("subdist");
                            }

                            if(object21.has("state")) {
                                if(output[5].length()>0){output[5]= output[5] + ", ";}
                                output[5] = output[5] + object21.getString("state");
                            }
                            if(object21.has("pc")) {
                                if(output[5].length()>0){output[5]= output[5] + ", ";}
                                output[5] = output[5] + object21.getString("pc");
                            }

                            for(int i = 0 ; i<=4 ; i++){
                                Log.d("JSON", output[i]);
                            }

                        }
                    }


                    httpcon.disconnect();

                    int returncode = response.getInt("returnCode");
                }catch(Exception e){

                }
                if(otp_sent==true) {
//                    t1.setText("OTP entered correctly.");
                    Log.d("AsyncTask", "Otp Entered correctly");
                    output[6] = "true";
                }
                else{
//                    t1.setText("Incorrect OTP entered.");
                    Log.d("AsyncTask", "Otp Entered was wrong");
                    output[6] = "false" ;
                }

                return output;
            }
        }
    }

}
