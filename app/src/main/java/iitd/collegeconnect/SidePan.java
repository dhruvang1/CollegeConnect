package iitd.collegeconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SidePan extends AppCompatActivity {
    private ImageView img;
    private TextView name;
    private TextView logout;
    private TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_pan);

        img = (ImageView) findViewById(R.id.sidepan_image);
        name = (TextView) findViewById(R.id.sidepan_name);
        logout = (TextView) findViewById(R.id.sidepan_logout);
        about = (TextView) findViewById(R.id.sidepan_about);


        Globals gl = (Globals) getApplicationContext();
        String username = gl.getUsername();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/profile/" + username;

        JsonObjectRequest profile_req = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setImage(response.getString("photo"));
                            name.setText(response.getString("name"));
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

        queue.add(profile_req);

    }

    public void sidepan_onclickname(View v){
        Intent intent = new Intent(this,EditProfile.class);
        startActivity(intent);
    }


    public void sidepan_onclicklogout(View v){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void sidepan_onclickabout(View v){
        Intent intent = new Intent(this,About.class);
        startActivity(intent);
    }

    public void sidepan_onclickprofile(View v){
        Intent intent = new Intent(this,Profile.class);
        startActivity(intent);
    }

    public void sidepan_onclickfeed(View v){
        Intent intent = new Intent(this,Feed.class);
        startActivity(intent);
    }

    public void sidepan_onclickchat(View v){
        Intent intent = new Intent(this,Conversation.class);
        startActivity(intent);
    }


    public void setImage(String imgstring){
        byte[] decodedString = Base64.decode(imgstring, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img.setImageBitmap(decodedByte);
    }


}
