package net.net.siekkiset.jesse.androidcourse2017s.exercise12;

import android.content.Context;

public class Place {
    public String name;
    public String imageName;

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }
}
