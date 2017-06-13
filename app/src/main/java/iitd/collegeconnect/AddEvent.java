package iitd.collegeconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddEvent extends AppCompatActivity {
    private static final int PICK_IMAGE_CONTENT = 1;
    private ImageView img;
    private EditText title;
    private EditText place;
    private EditText date;
    private EditText time;
    private EditText description;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        img = (ImageView) findViewById(R.id.addEvent_image);
        title = (EditText) findViewById(R.id.addevent_title);
        place = (EditText) findViewById(R.id.addevent_place);
        date = (EditText) findViewById(R.id.addevent_date);
        time = (EditText) findViewById(R.id.addevent_time);
        description = (EditText) findViewById(R.id.addevent_description);
    }

    public void addEvent_onClickImage(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        // Show only images, no videos or anything else
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_CONTENT){
            if(resultCode == RESULT_OK){
                Uri img_uri =  data.getData();
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), img_uri);
                    img.setImageBitmap(bitmap);
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void addEvent_onClickPost(View v){
        JSONObject event_form = new JSONObject();
        try{
            Globals gl = ((Globals)getApplicationContext());
            event_form.put("photo",getStringImage(bitmap));
            event_form.put("uploader",gl.getUsername());
            event_form.put("title",title.getText().toString());
            event_form.put("venue",place.getText().toString());
            event_form.put("date",date.getText().toString());
            event_form.put("time",time.getText().toString());
            event_form.put("desc",description.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        //addevent post request
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/event";

        JsonObjectRequest register_req = new JsonObjectRequest(Request.Method.POST, url, event_form,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("check ",response.toString());
                        try {
                            String status = response.getString("status");
                            Log.d("check ","status "+ status);
                            if (status.matches("success")) {
                                Log.d("check ","go to feed");
                                Intent intent = new Intent(AddEvent.this, Feed.class);
                                startActivity(intent);
                            } else {
                                Log.d("check ","else called");
                                Toast.makeText(getBaseContext(), status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }
        );

        //Add the request to requestqueue
        queue.add(register_req);

        //go to feed
//        Intent intent = new Intent(AddEvent.this,Feed.class);
//        startActivity(intent);
    }
}
