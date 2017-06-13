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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private ImageView img;
    private EditText username;
    private EditText name;
    private EditText password;
    private EditText cpassword;
    private EditText hostel;
    private EditText rollno;
    private EditText department;
    private EditText year;
    private EditText phone;
    private EditText bio;


    private Bitmap bitmap;
    private int PICK_IMAGE_CONTENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Firebase.setAndroidContext(this);

        img = (ImageView) findViewById(R.id.register_image);
        username = (EditText) findViewById(R.id.register_username);
        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        cpassword = (EditText) findViewById(R.id.register_cPassword);
        rollno = (EditText) findViewById(R.id.register_rollNo);
        department = (EditText) findViewById(R.id.register_department);
        year = (EditText) findViewById(R.id.register_year);
        hostel = (EditText) findViewById(R.id.register_hostel);
        phone = (EditText) findViewById(R.id.register_phone);
        bio = (EditText) findViewById(R.id.register_bio);
    }

    public void reg_onClickRegister(View v){
        String loc_status = registerUser();
        //empty status means SUCCESS
        if(loc_status != ""){
            Toast.makeText(getBaseContext(),loc_status,Toast.LENGTH_SHORT).show();
        }else{

            JSONObject reg_form = new JSONObject();
            try{
                reg_form.put("photo",getStringImage(bitmap));
                reg_form.put("username",username.getText().toString());
                reg_form.put("name",name.getText().toString());
                reg_form.put("password",password.getText().toString());
                reg_form.put("entry_no",rollno.getText().toString());
                reg_form.put("degree",department.getText().toString());
                reg_form.put("year",year.getText().toString());
                reg_form.put("hostel",hostel.getText().toString());
                reg_form.put("contact",phone.getText().toString());
                reg_form.put("bio",bio.getText().toString());
                Log.d("check","JSONform created " + reg_form.getString("photo").length());
            }
            catch(JSONException e){
                e.printStackTrace();
            }

//            Intent intent = new Intent(RegisterActivity.this, Feed.class);
//            startActivity(intent);

            //Pass reg_form to database

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://139.59.61.95:1337/register";

            JsonObjectRequest register_req = new JsonObjectRequest(Request.Method.POST, url, reg_form,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("check ",response.toString());
                            try {
                                String status = response.getString("status");
                                Log.d("check ","status "+ status);
                                if (status.matches("success")) {
                                    Log.d("check ","go to feed");
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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

        }
    }

    // Function used to validate user data
    protected String registerUser(){


        String pwd = password.getText().toString();
        String cpwd = cpassword.getText().toString();
        String uname = name.getText().toString();
        if(uname.matches(""))
            return "Name cannot be empty";

        if(pwd.matches(""))
            return "Password cannot be empty";

        if(!pwd.matches(cpwd))
            return "Confirm Password doesn't match";

        return "";

    }

    public void register_onClickImage(View v){
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


}
