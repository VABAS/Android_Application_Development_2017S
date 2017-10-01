package net.net.siekkiset.jesse.androidcourse2017s.exercise11;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase shoppingListDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shoppingListDatabase = initDbConnection();
        refreshListView();

    }

    private void refreshListView() {
        Cursor c = shoppingListDatabase.rawQuery("SELECT id,name,count,price FROM shoppinglist", null);
        ArrayList<Product> demoProducts = new ArrayList<>();
        while(c.moveToNext()) {
            demoProducts.add(
                    new Product(c.getInt(0),c.getString(1), c.getInt(2), c.getFloat(3))
            );
        }
        c.close();
        ArrayAdapter<Product> adapter = new ProductAdapter(this, demoProducts);
        ListView listView = (ListView)findViewById(R.id.shoppingItemListView);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String dbId = String.valueOf(view.getTag());
                shoppingListDatabase.execSQL("DELETE FROM shoppinglist WHERE id=" + dbId);
                refreshListView();
                return true;
            }
        });
    }

    protected SQLiteDatabase initDbConnection() {
        return initDbConnection(false);
    }
    protected SQLiteDatabase initDbConnection(boolean addDemoEntries) {
        SQLiteDatabase db = openOrCreateDatabase("shoppingListDb", MODE_PRIVATE, null);
        //db.execSQL("drop table shoppinglist");
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS shoppinglist(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, count INTEGER, price REAL);"
        );
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
        final Button addButton = (Button)newProductDialog.findViewById(R.id.addButton);
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
                addProduct(new Product(-1, name, count, price));
                refreshListView();
                newProductDialog.dismiss();
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
        shoppingListDatabase.execSQL("INSERT INTO shoppinglist VALUES(null, '"
                                     + product.name + "', "
                                     + product.count + ", "
                                     + product.price + ");");
        refreshListView();
        // Selecting sum of prices multiplied by counts with applicable SQL-query.
        Cursor c = shoppingListDatabase.rawQuery("SELECT SUM(price*count) FROM shoppinglist", null);
        c.moveToFirst();
        Toast.makeText(
                getBaseContext(), R.string.product_added_notify, Toast.LENGTH_SHORT
        ).show();
        Toast.makeText(
                getBaseContext(),
                getString(R.string.total_text) + ": " + String.format("%.2f", c.getFloat(0)),
                Toast.LENGTH_SHORT
        ).show();
    }
}
