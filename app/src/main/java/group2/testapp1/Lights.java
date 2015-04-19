package group2.testapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;


 // Created by Matthew on 4/19/15.

public class Lights extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("", "Inside Lights.class");
        //create intent
/*        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);*/
        //create textView
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("test");
        //set the view
        setContentView(textView);

        //setContentView(R.layout.activity_display_message);
    }
}
