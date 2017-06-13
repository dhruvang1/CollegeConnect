package iitd.collegeconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class ProfileSearch extends AppCompatActivity {
    private EditText name;
    private EditText entryno;
    private EditText degree;
    private EditText hostel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search);

        name = (EditText) findViewById(R.id.profilesearch_name);
        entryno = (EditText) findViewById(R.id.profilesearch_entno);
        degree = (EditText) findViewById(R.id.profilesearch_degree);
        hostel = (EditText) findViewById(R.id.profilesearch_hostel);
    }

    public void profilesearch_onclicksearch(View v){
        JSONObject profile_form = new JSONObject();
        try{
            profile_form.put("name",name.getText().toString());
            profile_form.put("entry_no",entryno.getText().toString());
            profile_form.put("degree",degree.getText().toString());
            profile_form.put("hostel",hostel.getText().toString());

        }catch (JSONException e){
            e.printStackTrace();
        }


        Intent intent = new Intent(ProfileSearch.this, ProfileSearchList.class);
        intent.putExtra("profile_data",profile_form.toString());
        startActivity(intent);


    }
}
