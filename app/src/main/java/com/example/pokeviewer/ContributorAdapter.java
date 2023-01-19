package com.example.pokeviewer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ContributorViewHolder> {

    ArrayList<Contributor> contributors; //List of contributors
    Context context; //Context of said adpater

    public ContributorAdapter(ArrayList<Contributor> contributors, Context context) {
        this.contributors = contributors;
        this.context = context;
    }


    @NonNull
    @Override
    public ContributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contributor_item,parent,false);

        return new ContributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorViewHolder holder, int position) {

        Contributor c = contributors.get(position);
        holder.txtName.setText(c.name);

        holder.btnViewGit.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,c.githubLink);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {return contributors.size();}

    class ContributorViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        Button btnViewGit;

        public ContributorViewHolder(@NonNull View view){
            super(view);
            txtName = view.findViewById(R.id.txtName);
            btnViewGit = view.findViewById(R.id.btnViewGit);
        }
    }
}
