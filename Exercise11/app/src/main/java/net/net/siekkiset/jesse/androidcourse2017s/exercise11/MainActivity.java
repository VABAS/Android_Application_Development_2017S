package net.net.siekkiset.jesse.androidcourse2017s.exercise11;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Product> demoProducts = new ArrayList<>();
    SQLiteDatabase shoppingListDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shoppingListDatabase = initDbConnection();

        Cursor c = shoppingListDatabase.rawQuery("SELECT * FROM shoppinglist", null);
        while(c.moveToNext()) {
            demoProducts.add(new Product(c.getString(0), c.getInt(1), c.getFloat(2)));
        }
        c.close();
        refreshListView();

    }

    private void refreshListView() {
        ArrayAdapter<Product> adapter = new ProductAdapter(this, demoProducts);
        ListView listView = (ListView)findViewById(R.id.shoppingItemListView);
        listView.setAdapter(adapter);
    }

    protected SQLiteDatabase initDbConnection() {
        return initDbConnection(false);
    }
    protected SQLiteDatabase initDbConnection(boolean addDemoEntries) {
        SQLiteDatabase db = openOrCreateDatabase("soppingListDb", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS shoppinglist(name VARCHAR, count INTEGER, price REAL);");
        if (addDemoEntries) {
            db.execSQL("DELETE FROM shoppinglist;");
            db.execSQL("INSERT INTO shoppinglist values('Milk', 3, 1.75);");
            db.execSQL("INSERT INTO shoppinglist values('Butter', 1, 0.99);");
            db.execSQL("INSERT INTO shoppinglist values('Cheese', 1, 3.4);");
        }
        return db;
    }

    protected void addProductButtonClicked(View view) {
        final Dialog newProductDialog = new Dialog(this);
        newProductDialog.setContentView(R.layout.add_product_dialog);
        Button addButton = (Button)newProductDialog.findViewById(R.id.addButton);
        Button cancelButton = (Button)newProductDialog.findViewById(R.id.cancelButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =
                        ((AutoCompleteTextView)newProductDialog.findViewById(R.id.nameAutoCompleteTextView))
                                .getText().toString();
                String countS =
                        ((AutoCompleteTextView)newProductDialog.findViewById(R.id.countAutoCompleteTextView))
                                .getText().toString();
                String priceS =
                        ((AutoCompleteTextView)newProductDialog.findViewById(R.id.priceAutoCompleteTextView))
                                .getText().toString();
                int count;
                try {
                    count = Integer.parseInt(countS);
                }
                catch (NumberFormatException e) {
                    Toast.makeText(
                            getBaseContext(),
                            R.string.count_format_error,
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
                float price;
                try {
                    price = Float.parseFloat(priceS);
                }
                catch (NumberFormatException e) {
                    Toast.makeText(
                            getBaseContext(),
                            R.string.price_format_error,
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                shoppingListDatabase.execSQL(
                        "INSERT INTO shoppinglist values('"+name+"', "+count+", "+price+");"
                );
                refreshListView();
                newProductDialog.dismiss();
                Toast.makeText(
                        getBaseContext(), R.string.product_added_notify, Toast.LENGTH_SHORT
                ).show();

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProductDialog.dismiss();
                Toast.makeText(
                        getBaseContext(),
                        R.string.adding_canceled_notify,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        newProductDialog.show();
    }

    protected void addProduct(Product product) {
        shoppingListDatabase.execSQL("INSERT INTO shoppinglist VALUES('"
                                     + product.name +"', "
                                     + product.count +", "
                                     + product.price +");");
        refreshListView();
    }

    protected void deleteProductClicked(View view) {

    }
}
