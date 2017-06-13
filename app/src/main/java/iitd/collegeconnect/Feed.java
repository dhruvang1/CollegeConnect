package iitd.collegeconnect;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Feed extends AppCompatActivity {
    private boolean flag_loading = false;
    private ArrayAdapter adapter;
    private ListView mylist;
    private Button more;
    private ArrayList<JSONObject> eventlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Log.d("check username: ",UserDetails.username);
        Log.d("check","feed opened");

        eventlist = new ArrayList<JSONObject>();

        more = new Button(this);
        more.setText("Load More");

        more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                additems();
            }
        });

        adapter = new CustomAdapter(this,eventlist);
        mylist = (ListView) findViewById(R.id.feed_feed);
        mylist.setAdapter(adapter);
        mylist.addFooterView(more);

        additems();

    }

    public void additems(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/feed/";

        if(eventlist.size() > 0){
            try {
                url += eventlist.get(eventlist.size() - 1).getInt("event_id");
            }catch(JSONException e){
                e.printStackTrace();
            }
        }else{
            url += "0";
        }


        JsonObjectRequest register_req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("check ",response.toString());
                        try {
                            JSONArray myfeed = (JSONArray) response.get("feed");
                            int len = myfeed.length();

                            for(int i=len-1;i>=0;i--){
                                JSONObject myobj = myfeed.getJSONObject(i);
                                myobj.put("status",false);
                                Log.d("check ",myobj.toString());
                                eventlist.add(myobj);
                            }
                            adapter.notifyDataSetChanged();
//                            flag_loading = false;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if(itemid == R.id.feed_addEvent){
            Intent intent = new Intent(Feed.this,AddEvent.class);
            startActivity(intent);
        }
        else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void feed_onclickprofile(View v){
        Intent intent = new Intent(this,Profile.class);
        startActivity(intent);
    }

    public void feed_onclickfeed(View v){
        Intent intent = new Intent(this,Feed.class);
        startActivity(intent);
    }

    public void feed_onclickoptions(View v){
        Intent intent = new Intent(this,SidePan.class);
        startActivity(intent);
    }

    public void feed_onclickchat(View v){
        Intent intent = new Intent(this,Conversation.class);
        startActivity(intent);
    }
}

