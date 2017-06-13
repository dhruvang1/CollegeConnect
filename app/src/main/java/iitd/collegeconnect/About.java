package iitd.collegeconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        t = (TextView) findViewById(R.id.about_text);
        t.setText("This application is free to use.\n\nDeveloped by:\n1.Dhruvang Makadia (2013CS50289)\n2.Sachin Kumar (2013CS50296)\n3.Sachin Meena (2013CS50297)");
    }
}
