package net.siekkiset.jesse.androidcourse2017s.nfcrecearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PreWriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_write);
    }

    protected void doWriteButtonClicked (View view) {
        TextView tv = (TextView)findViewById(R.id.textToWrite);
        Intent write = new Intent(this, NfcWriteActivity.class);
        write.putExtra("text", tv.getText().toString());
        startActivity(write);
    }
}
