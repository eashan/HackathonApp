package knowyc.com.knowyc.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import knowyc.com.knowyc.R;

public class DocumentsSubmissionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton upload_sign, upload_photo, upload_address_proof;
    private static final int SELECT_PICTURE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    int document = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_submission);

        upload_sign = (ImageButton) findViewById(R.id.signature_image_button);
        upload_photo = (ImageButton)findViewById(R.id.photo_upload_button);
        upload_address_proof = (ImageButton) findViewById(R.id.address_proofs_upload_button);

        upload_photo.setOnClickListener(this);
        upload_sign.setOnClickListener(this);
        upload_address_proof.setOnClickListener(this);


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

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.signature_image_button)){
            if (!hasCamera())
                Toast.makeText(this, "No camera on device", Toast.LENGTH_SHORT).show();
            else {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                document = 0;
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
            }
        }

        if (v == findViewById(R.id.photo_upload_button)){
            if (!hasCamera())
                Toast.makeText(this, "No camera on device", Toast.LENGTH_SHORT).show();
            else {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                document = 1;
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
            }
        }

        if (v == findViewById(R.id.address_proofs_upload_button)){
            if (!hasCamera())
                Toast.makeText(this, "No camera on device", Toast.LENGTH_SHORT).show();
            else {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");

            JSONObject jsonObject = new JSONObject();
            try {
                String inputdata = jsonObject.toString();
                String url = "";

                if (document == 0) {
                    url = "knowyc.herokuapp.com/upload_docs/signature";
                    jsonObject.put("signature", encodeTobase64(photo));
                }
                if (document == 1){
                    url = "knowyc.herokuapp.com/upload_docs/profilepic";
                    jsonObject.put("profilepic", encodeTobase64(photo));
                }
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
                writer.write(inputdata);
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


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }


    //converting bitmap to byte[] and vice-versa:
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getByteArrayAsBitmap(byte[] imgByte) {
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
