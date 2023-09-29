package com.example.mentalhub.screening;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mentalhub.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> gamesList;
    ArrayList<Integer> gamesImages;
    LayoutInflater layoutInflater;

    public CustomAdapter(Context ctx, ArrayList<String> gamesList, ArrayList<Integer> gamesImages) {
        this.context = ctx;
        this.gamesList = gamesList;
        this.gamesImages = gamesImages;
        layoutInflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return gamesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.activity_custom_list_view, null);
        TextView txtView = convertView.findViewById(R.id.textView);
        ImageView gameIcon = convertView.findViewById(R.id.imageIcon);

        txtView.setText(gamesList.get(position));
        gameIcon.setImageResource(gamesImages.get(position));
        return convertView;
    }
}
