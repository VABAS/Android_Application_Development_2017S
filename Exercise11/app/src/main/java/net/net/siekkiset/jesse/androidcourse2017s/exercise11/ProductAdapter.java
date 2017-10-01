package net.net.siekkiset.jesse.androidcourse2017s.exercise11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    private TextView nameTextView;
    private TextView priceTextView;
    private TextView countTextView;


    public ProductAdapter(Context context, ArrayList<Product> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.product_list_fragment, parent, false);

            nameTextView = (TextView)convertView.findViewById(R.id.nameTextView);
            priceTextView = (TextView)convertView.findViewById(R.id.priceTextView);
            countTextView = (TextView)convertView.findViewById(R.id.countTextView);

        }

        Product item = getItem(position);
        if (item!= null) {
            nameTextView.setText(item.name);
            priceTextView.setText(String.format("%.2f", item.price));
            countTextView.setText(String.valueOf(item.count));
        }
        convertView.setTag(item.dbId);
        return convertView;
    }
}
