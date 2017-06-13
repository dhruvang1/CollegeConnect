package iitd.collegeconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileSearchList extends AppCompatActivity {
    private JSONObject profile_form;
    private ListView profile_listview;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> mylist;
    private JSONArray serverprofile_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search_list);

        Intent intent = getIntent();
        String jsonstring = intent.getStringExtra("profile_data");
        try {
            profile_form = new JSONObject(jsonstring);
        }catch(JSONException e){
            e.printStackTrace();
        }

        Log.d("check profileform",profile_form.toString());

        profile_listview = (ListView) findViewById(R.id.profilesearchlist_list);
        mylist = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,mylist);
        profile_listview.setAdapter(adapter);


        final Globals gl = (Globals) getApplicationContext();

        Log.d("check search","here");

        profile_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gl.setis_other();
                try {
                    JSONObject jobj = serverprofile_list.getJSONObject(position);
                    String uname = jobj.getString("username");
                    gl.setother_username(uname);
                }catch(JSONException e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(ProfileSearchList.this,Profile.class);
                startActivity(intent);
            }
        });


        //sending query to server
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/search/p";


        JsonObjectRequest register_req = new JsonObjectRequest(Request.Method.POST, url, profile_form,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("check profile response ",response.toString());
                        try {
                            serverprofile_list = (JSONArray) response.get("profile_search");
                            int length = serverprofile_list.length();
                            for(int i=0;i<length;i++){
                                JSONObject current = serverprofile_list.getJSONObject(i);
                                Log.d("check profile ",current.toString());
                                String display_name = current.getString("name") + " (" + current.getString("entry_no") + ")";
                                mylist.add(display_name);
                            }
//                            Log.d("checksize",mylist.get(2));
                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
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

