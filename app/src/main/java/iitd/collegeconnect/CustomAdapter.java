package iitd.collegeconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<JSONObject>{
    private ArrayList<JSONObject> array;

    private static class HolderClass{
        TextView title;
        TextView place;
        TextView time;
        TextView description;
        ImageView img;
        Button morebtn;
    }

    public CustomAdapter(Context context, ArrayList<JSONObject> j){
        super(context,R.layout.event,j);
        this.array = j;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        JSONObject j = getItem(position);
        Log.d("check","in getview j: " + Integer.toString(position) + " " + j.toString());

        final HolderClass viewholder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event,parent,false);

            viewholder = new HolderClass();
            viewholder.title= (TextView) convertView.findViewById(R.id.event_title);
            viewholder.place= (TextView) convertView.findViewById(R.id.event_place);
            viewholder.time = (TextView) convertView.findViewById(R.id.event_time);
            viewholder.description = (TextView) convertView.findViewById(R.id.event_description);
            viewholder.img = (ImageView) convertView.findViewById(R.id.event_image);
            viewholder.morebtn = (Button) convertView.findViewById(R.id.event_more);

            convertView.setTag(viewholder);
        }else{
            viewholder = (HolderClass) convertView.getTag();
        }

        try {

            viewholder.title.setText(j.getString("title"));
            viewholder.place.setText(j.getString("venue"));
            viewholder.time.setText(j.getString("date") + "  " + j.getString("time"));
            viewholder.description.setText(j.getString("desc"));
            viewholder.img.setImageBitmap(getBitmap(j.getString("photo")));
            Log.d("check ","called");
        }catch(JSONException e){
            Log.d("check ","not called");
            e.printStackTrace();
        }

        viewholder.morebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("checkk","clicked");

                try{
                    if (array.get(position).getBoolean("status")== false) {
                        array.get(position).put("status",true);
                        viewholder.description.setVisibility(View.VISIBLE);
                        viewholder.img.setVisibility(View.VISIBLE);
                        viewholder.morebtn.setText("LESS");
                    } else {
                        array.get(position).put("status",false);
                        viewholder.img.setVisibility(View.GONE);
                        viewholder.description.setVisibility(View.GONE);
                        viewholder.morebtn.setText("MORE");
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }

    public Bitmap getBitmap(String imgstring){
        byte[] decodedString = Base64.decode(imgstring, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
