package com.example.andrey.petsitter.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrey.petsitter.Models.Classified;
import com.example.andrey.petsitter.R;

import java.util.ArrayList;

/**
 * Created by Andrey on 26.1.2015 г..
 */
public class ClassifiedsAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private ArrayList<Classified> classifieds;
    private ClassifiedHolderItem holder;

    public ClassifiedsAdapter(Context context, int resource, ArrayList<Classified> classifieds) {
        this.context = context;
        this.resource = resource;
        this.classifieds = classifieds;
    }

    @Override
    public int getCount() {
        return classifieds.size();
    }

    @Override
    public Classified getItem(int position) {
        return classifieds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ClassifiedHolderItem();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.picPet);
            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.region = (TextView) convertView.findViewById(R.id.tvRegion);

            convertView.setTag(holder);
        } else {
            holder = (ClassifiedHolderItem) convertView.getTag();
        }

        Bitmap bitmap = getItem(position).getImage();

        holder.image.setImageBitmap(bitmap);
        holder.title.setText(getItem(position).getTitle());
        holder.region.setText(getItem(position).getAddress());

        return convertView;
    }
}
