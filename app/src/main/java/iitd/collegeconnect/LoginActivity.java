package iitd.collegeconnect;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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


public class LoginActivity extends AppCompatActivity {
    private EditText rollNo;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        rollNo = (EditText) findViewById(R.id.login_rollNo);
        password = (EditText) findViewById(R.id.login_password);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Login");

    }

    public void onClickSignIn(View view) {
        final JSONObject login_form = new JSONObject();
        try {
            login_form.put("username", rollNo.getText().toString());
            login_form.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("SignIN", login_form.toString());

        //fill connect server

        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/login";

        JsonObjectRequest login_req = new JsonObjectRequest(Request.Method.POST, url, login_form,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("check",response.toString());
                            String status = response.getString("status");
                            if (status.matches("success")) {
                                Globals gl = ((Globals)getApplicationContext());
                                gl.setUsername(login_form.getString("username"));

                                UserDetails.username = login_form.getString("username");
                                UserDetails.name = response.getString("name");

                                Intent intent = new Intent(LoginActivity.this,Feed.class);
                                startActivity(intent);

                            } else {
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
        queue.add(login_req);
//        finish();
    }

    @Override
    public void onBackPressed(){

    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_feed, menu);
//
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.feed_search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        return true;
//    }

}
