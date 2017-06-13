package iitd.collegeconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Iterator;

public class Conversation extends AppCompatActivity {
    private ListView conversation_listview;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> mylist;
    private ArrayList<String> username_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        conversation_listview = (ListView) findViewById(R.id.conversation_list);
        mylist = new ArrayList<String>();
        username_list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,mylist);
        conversation_listview.setAdapter(adapter);

        conversation_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String other_uname = username_list.get(position);
                String other_name = mylist.get(position);

                UserDetails.chat_with = other_name;
                UserDetails.chat_with_uname = other_uname;

                Intent intent = new Intent(Conversation.this,Chat.class);
                startActivity(intent);
            }
        });

        //get data from server
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://collegeconnect-b7b03.firebaseio.com/messages.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Iterator<?> keys = response.keys();
                        while(keys.hasNext()){
                            String key = (String)keys.next();
                            String[] keyarr = key.split("_");
                            if(keyarr[0].matches(UserDetails.username)){
                                username_list.add(keyarr[1]);
                                mylist.add(keyarr[2]);
                            }
                        }
                        adapter.notifyDataSetChanged();
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
        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if(itemid == R.id.conversation_newchat){
            Intent intent = new Intent(Conversation.this,ConversationNew.class);
            startActivity(intent);
        }
        else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void conversation_onclickoptions(View v){
        Intent intent = new Intent(this,SidePan.class);
        startActivity(intent);
    }

    public void conversation_onclickprofile(View v){
        Intent intent = new Intent(this,Profile.class);
        startActivity(intent);
    }

    public void conversation_onclickfeed(View v){
        Intent intent = new Intent(this,Feed.class);
        startActivity(intent);
    }

}
