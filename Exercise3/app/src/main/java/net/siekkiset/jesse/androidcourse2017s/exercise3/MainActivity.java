package net.siekkiset.jesse.androidcourse2017s.exercise3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public void showMap(View view) {
        // get lat and lng values
        EditText editText1 = (EditText) findViewById(R.id.editTextLat);
        EditText editText2 = (EditText) findViewById(R.id.editTextLon);
        String numberOne = editText1.getText().toString();
        String numberTwo = editText2.getText().toString();
        double lat = Double.parseDouble(numberOne);
        double lng = Double.parseDouble(numberTwo);
        // show map
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + lat + "," + lng));
        //intent.setData(Uri.parse("geo:" + lat + "," + lng));
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
