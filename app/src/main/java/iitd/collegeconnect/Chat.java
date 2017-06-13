package iitd.collegeconnect;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar ab =getSupportActionBar();
        ab.setTitle(UserDetails.chat_with);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        layout = (LinearLayout)findViewById(R.id.chat_layout1);
        sendButton = (ImageView)findViewById(R.id.chat_sendButton);
        messageArea = (EditText)findViewById(R.id.chat_messageArea);
        scrollView = (ScrollView)findViewById(R.id.chat_scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://collegeconnect-b7b03.firebaseio.com/messages/" + UserDetails.username + "_"  + UserDetails.chat_with_uname + "_" + UserDetails.chat_with);
        reference2 = new Firebase("https://collegeconnect-b7b03.firebaseio.com//messages/" + UserDetails.chat_with_uname + "_" + UserDetails.username + "_" + UserDetails.name);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                messageArea.setText("");
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    scrollView.postDelayed(new Runnable() {
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    },200L);

                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox(message, 1);
                }
                else{
                    addMessageBox(message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//        float text_size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        float text_size = 20.0f;

        if(type == 2) {
            lp.setMargins(0, 0, 90, 10);
            textView.setLayoutParams(lp);
            textView.setTextSize(text_size);
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }
        else{
            lp.setMargins(90, 0, 0, 10);
            textView.setLayoutParams(lp);
            textView.setTextSize(text_size);
            textView.setGravity(Gravity.RIGHT);
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }

        layout.addView(textView);
//        scrollView.fullScroll(View.FOCUS_DOWN);

        scrollView.postDelayed(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        },500L);
    }
}
