package com.example.mentalhub.ProblemSolving;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhub.ProblemSolving.Bulimia.Bulimia;
import com.example.mentalhub.ProblemSolving.Bulimia.TutorialBulimia;
import com.example.mentalhub.ProblemSolving.Dieting.Dieting;
import com.example.mentalhub.ProblemSolving.Dieting.TutorialDieting;
import com.example.mentalhub.ProblemSolving.Oral.Oral;
import com.example.mentalhub.ProblemSolving.Oral.TutorialOral;
import com.example.mentalhub.R;

import java.util.List;

public class CustomMenuAdapter extends RecyclerView.Adapter<CustomMenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItems;
    private Context context;

    public CustomMenuAdapter(List<MenuItem> menuItems, Context context) {
        this.menuItems = menuItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.textViewTitle.setText(menuItem.getTitle());
        holder.textViewDescription.setText(menuItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTitle;
        TextView textViewDescription;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MenuItem menuItem = menuItems.get(position);

            // Navigate to the appropriate activity based on the clicked menu item
            Intent intent;
            switch (position) {
                case 0:
                    intent = new Intent(context, TutorialBulimia.class);
                    break;
                case 1:
                    intent = new Intent(context, TutorialDieting.class);
                    break;
                case 2:
                    intent = new Intent(context, TutorialOral.class);
                    break;
                default:
                    intent = null;
            }

            if (intent != null) {
                context.startActivity(intent);
            }
        }
    }
}
