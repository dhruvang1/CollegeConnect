package iitd.collegeconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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

public class ConversationNew extends AppCompatActivity {
    private EditText name;
    private ListView conversation_listview;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> mylist;
    private JSONArray serverprofile_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_new);

        name = (EditText) findViewById(R.id.conversationNew_name);
        conversation_listview = (ListView) findViewById(R.id.conversationnew_list);


        mylist = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,mylist);
        conversation_listview.setAdapter(adapter);

        conversation_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject jobj = serverprofile_list.getJSONObject(position);
                    String other_uname = jobj.getString("username");
                    String other_name = jobj.getString("name");
                    UserDetails.chat_with = other_name;
                    UserDetails.chat_with_uname = other_uname;

                }catch(JSONException e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(ConversationNew.this,Chat.class);
                startActivity(intent);
            }
        });
    }

    public void conversationnew_onclicksearch(View v){
//        mylist.clear();
//        adapter.notifyDataSetChanged();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://139.59.61.95:1337/search/p";

        JSONObject obj = new JSONObject();
        try{
            obj.put("name",name.getText().toString());
            obj.put("entry_no","");
            obj.put("degree","");
            obj.put("hostel","");
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest register_req = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("check conv response ",response.toString());
                        try {
                            mylist.clear();
                            serverprofile_list = (JSONArray) response.get("profile_search");
                            int length = serverprofile_list.length();
                            for(int i=0;i<length;i++){
                                JSONObject current = serverprofile_list.getJSONObject(i);
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
