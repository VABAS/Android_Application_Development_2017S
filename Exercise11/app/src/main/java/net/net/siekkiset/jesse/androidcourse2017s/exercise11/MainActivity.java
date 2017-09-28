package net.net.siekkiset.jesse.androidcourse2017s.exercise11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Product> demoProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIXME: Demo content
        demoProducts.add(new Product("Milk", 3, 1.75));
        demoProducts.add(new Product("Butter", 1, 0.99));
        demoProducts.add(new Product("Cheese", 1, 3.40));

        ArrayAdapter<Product> adapter = new ProductAdapter(this, demoProducts);
        ListView listView = (ListView)findViewById(R.id.shoppingItemListView);
        listView.setAdapter(adapter);
    }

    protected void addProductButtonClicked(View view) {

    }

    protected void deleteProductClicked(View view) {

    }
}
