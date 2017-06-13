package iitd.collegeconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class Profile extends AppCompatActivity {
    private TextView name;
    private TextView department;
    private TextView year;
    private TextView hostel;
    private TextView bio;
    private TextView contact;
    private ImageView img;

    private Button feedbtn;
    private Button chatbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.profile_name);
        department = (TextView) findViewById(R.id.profile_department);
        year = (TextView) findViewById(R.id.profile_year);
        hostel = (TextView) findViewById(R.id.profile_hostel);
        contact = (TextView) findViewById(R.id.profile_contact);
        bio = (TextView) findViewById(R.id.profile_bio);
        img = (ImageView) findViewById(R.id.profile_image);
        feedbtn = (Button) findViewById(R.id.profile_feedbtn);
        chatbtn = (Button) findViewById(R.id.profile_chatbtn);



//        name.setText("my name");
//        department.setText("Computer Science");
//        year.setText("2013");
//        hostel.setText("Zanskar");
        //server connection



        //Pass reg_form to database
        Globals gl = (Globals) getApplicationContext();
        String username = gl.getUsername();
        if(gl.getis_other()){
            username = gl.getother_username();
            gl.setis_other();

            UserDetails.chat_with_uname = gl.getother_username();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/profile/" + username;

        JsonObjectRequest profile_req = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setImage(response.getString("photo"));
                            name.setText(response.getString("name"));
                            department.setText(response.getString("degree"));
                            year.setText(response.getString("year"));
                            hostel.setText(response.getString("hostel"));
                            contact.setText(response.getString("contact"));
                            bio.setText(response.getString("bio"));

                            UserDetails.chat_with = response.getString("name");
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

    public void setImage(String imgstring){
        byte[] decodedString = Base64.decode(imgstring, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img.setImageBitmap(decodedByte);
    }

    public void profile_onclickfeed(View v){
        Intent intent = new Intent(Profile.this,Feed.class);
        startActivity(intent);
    }

    public void profile_onclickoptions(View v){
        Intent intent = new Intent(Profile.this,SidePan.class);
        startActivity(intent);
    }

    public void profile_onclickchat(View v){
        Intent intent = new Intent(this,Conversation.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if(itemid == R.id.profile_search){
            Intent intent = new Intent(Profile.this,ProfileSearch.class);
            startActivity(intent);
        }
        else if(itemid ==R.id.profile_startchat){
            Intent intent = new Intent(Profile.this,Chat.class);
            startActivity(intent);
        }
        else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
