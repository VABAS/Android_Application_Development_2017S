package net.siekkiset.jesse.androidcourse2017s.exercise22;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public void loginButtonClicked(View view) {
        AutoCompleteTextView loginField = (AutoCompleteTextView)findViewById(R.id.loginField);
        EditText pwField = (EditText)findViewById(R.id.pwField);

        String text = loginField.getText().toString()
                    + " " + pwField.getText().toString();

        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoCompleteTextView actv = (AutoCompleteTextView)
                findViewById(R.id.loginField); // add stings to control
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                new String[] {"Pasi","Juha","Kari","Jouni","Esa","Hannu"});
        actv.setAdapter(aa);
    }
}
