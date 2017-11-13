package net.siekkiset.jesse.androidcourse2017s.nfcrecearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void readTagButtonClicked (View view) {
        startActivity(new Intent(this, NfcReadActivity.class));
    }
    protected void writeTagButtonClicked (View view) {
        startActivity(new Intent(this, PreWriteActivity.class));
    }
}