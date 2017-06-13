package iitd.collegeconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class EditProfile extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private EditText cpassword;
    private EditText rollno;
    private EditText department;
    private EditText year;
    private EditText phone;
    private EditText bio;
    private EditText hostel;

    private Button savebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = (EditText) findViewById(R.id.editprofile_name);
        password = (EditText) findViewById(R.id.editprofile_password);
        cpassword = (EditText) findViewById(R.id.editprofile_cPassword);
        rollno = (EditText) findViewById(R.id.editprofile_rollNo);
        department = (EditText) findViewById(R.id.editprofile_department);
        year = (EditText) findViewById(R.id.editprofile_year);
        phone = (EditText) findViewById(R.id.editprofile_phone);
        hostel = (EditText) findViewById(R.id.editprofile_hostel);
        bio = (EditText) findViewById(R.id.editprofile_bio);

        savebtn = (Button) findViewById(R.id.editprofile_save);


        //showing the current profile
        Globals gl = (Globals) getApplicationContext();
        String username = gl.getUsername();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/profile/" + username;

        JsonObjectRequest profile_req = new JsonObjectRequest(Request.Method.GET,url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("check ep: ",response.toString());
                        try {
                            name.setText(response.getString("name"));
                            department.setText(response.getString("degree"));
                            year.setText(response.getString("year"));
                            hostel.setText(response.getString("hostel"));
                            rollno.setText(response.getString("entry_no"));
                            phone.setText(response.getString("contact"));
                            bio.setText(response.getString("bio"));
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

    public void editprofile_onclicksave(View v){
        Log.d("check ","save called");
        Globals gl = (Globals) getApplicationContext();
        String username = gl.getUsername();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/update/" + username;

        JSONObject profile_form = new JSONObject();
        try{
            profile_form.put("name",name.getText().toString());
            profile_form.put("password",password.getText().toString());
            profile_form.put("degree",department.getText().toString());
            profile_form.put("year",year.getText().toString());
            profile_form.put("hostel",hostel.getText().toString());
            profile_form.put("entry_no",rollno.getText().toString());
            profile_form.put("contact",phone.getText().toString());
            profile_form.put("bio",bio.getText().toString());
        }catch (JSONException e){
            Log.d("check ","not called");
            e.printStackTrace();
        }

        Log.d("check ","done form");
        JsonObjectRequest profile_req = new JsonObjectRequest(Request.Method.PUT,url, profile_form,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("check response ",response.toString());
                        try {
                            if (response.getString("status").matches("success")) {

                                Intent intent = new Intent(EditProfile.this, Profile.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getBaseContext(), response.getString("status"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
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

}
