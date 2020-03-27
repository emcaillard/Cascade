package com.example.cascade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> images;

    private ArrayList<Integer> imagesDownloaded;

    public GridAdapter(Context cont, ArrayList<String> img){
        this.context = cont;
        this.images =img;

        imagesDownloaded = new ArrayList<Integer>();

        imagesDownloaded.add(R.drawable.empty);
        imagesDownloaded.add(R.drawable.bluecircle);
        imagesDownloaded.add(R.drawable.purplecircle);
        imagesDownloaded.add(R.drawable.yellowcircle);
        imagesDownloaded.add(R.drawable.redcircle);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView != null){
            return convertView;
        }

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(0,0,0,0);
        imageView.setAdjustViewBounds(true);

        switch(images.get(position)){
            case "blue":
                imageView.setImageResource(imagesDownloaded.get(1));
                break;
            case "purple":
                imageView.setImageResource(imagesDownloaded.get(2));
                break;
            case "yellow":
                imageView.setImageResource(imagesDownloaded.get(3));
                break;
            case "red":
                imageView.setImageResource(imagesDownloaded.get(4));
                break;
            case "empty":
                imageView.setImageResource(imagesDownloaded.get(0));
                break;
        }

        return imageView;
    }
}
