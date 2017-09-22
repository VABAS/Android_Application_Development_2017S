package net.siekkiset.jesse.androidcourse2017s.exercise8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    protected ImageView imageView;
    protected AutoCompleteTextView autoCompleteTextView;

    // Button onclick event.
    protected void loadImageButtonClicked (View view) {
        // Getting textView text and converting it to string.
        String url = autoCompleteTextView.getText().toString();
        // Using loadImage-method to load image into imageView.
        loadImage(url);

    }

    // Loads image from url to ImageView.
    protected void loadImage(String url) {
        Glide.with(this).load(url).into(imageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = this.findViewById(R.id.imageView);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView.setText("http://");
        loadImage("https://jesse.siekkiset.net/kuvat/mie.png");
    }
}
