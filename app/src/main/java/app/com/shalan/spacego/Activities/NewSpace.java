package app.com.shalan.spacego.Activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import app.com.shalan.spacego.R;

public class NewSpace extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_space);
    }

    public void setCutomView(){
        TextView textView = (TextView) findViewById(R.id.testing);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Panama-Light.otf") ;
        textView.setTypeface(typeface);
    }
}
