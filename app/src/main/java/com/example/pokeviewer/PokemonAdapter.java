package com.example.pokeviewer;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.pokeviewer.api.models.list.PokemonListItem;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    List<PokemonListItem> listOfPokemon;
    Context context;
    boolean showImages = true;

    public PokemonAdapter(List<PokemonListItem> listOfPokemon, Context context) {
        this.listOfPokemon = listOfPokemon;
        this.context = context;
        //loadPreferences();
    }
    
    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pokemon_item,parent,false);

        return new PokemonViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull PokemonViewHolder holder) {
        super.onViewRecycled(holder);
        holder.view.setBackgroundColor(Color.BLACK);
        holder.tvPokeIndex.setTextColor(Color.BLACK);
        holder.tvPokeName.setTextColor(Color.BLACK);
        holder.ivPokePic.setImageResource(0);
        holder.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        //Get the pokemon item
        PokemonListItem p = listOfPokemon.get(position);
        //Set the value of said item to its capital name
        holder.tvPokeName.setText(p.getNameCapital());

        //Attempt to set the idea (because a URI syntax could occur via a parse)
        try {
            holder.tvPokeIndex.setText("#" + p.getID());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //check if the don't show images setting is selected
        loadPreferences();


        //Do we show images? If not hide the images on the home page.
        if (!showImages) {
            holder.ivPokePic.setVisibility(View.GONE);
        }

        //Use glide instead of picasso because it loads images smoother in a recycle view (by far!)
        Glide.with(holder.itemView.getContext()).asBitmap().load(p.getImageURL()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                //Set the iamge to the bitmap resource
                if (showImages) {
                    holder.ivPokePic.setImageBitmap(resource);
                }
                //Remove the progress bar
                holder.progressBar.setVisibility(View.GONE);

                //Now use the palette library to get the domainte color
                Palette.from(resource).generate(palette -> {
                    Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                    if (dominantSwatch == null ) {
                        return;
                    }
                    //Animate said color from black
                    ObjectAnimator.ofObject(holder.view, "backgroundColor", new ArgbEvaluator(), Color.BLACK, dominantSwatch.getRgb())
                            .setDuration(300)
                            .start();
                    //Also change the name of the pokemon if the color is to dark to see
                    if (isColorDark(dominantSwatch.getRgb())) {
                        holder.tvPokeName.setTextColor(Color.WHITE);
                        holder.tvPokeIndex.setTextColor(Color.WHITE);
                    } else {
                        holder.tvPokeName.setTextColor(Color.BLACK);
                        holder.tvPokeIndex.setTextColor(Color.BLACK);
                    }

                });
            }
        });

        //holder.btnViewPoke.setText("View " + p.getNameCapital());

        //!!!uncomment once PokeInfoActivity exists!!!
        holder.view.setOnClickListener(view -> {
            Intent intent = new Intent(context, com.example.pokeviewer.PokeInfoActivity.class);
            String ID = null;
            try {
                ID = p.getID();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            intent.putExtra("ID",ID);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {return listOfPokemon.size();}

    //used to filter the list based on the users search
    public void filteredList(ArrayList<PokemonListItem> filteredList){
        //sets the list equal to the filtered list and notifies the data set has changed
        listOfPokemon = filteredList;
        notifyDataSetChanged();
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder{
        TextView tvPokeName;
        TextView tvPokeIndex;
        ImageView ivPokePic;
        ProgressBar progressBar;
        View view;

        public PokemonViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            tvPokeName = view.findViewById(R.id.tvPokeName);
            tvPokeIndex = view.findViewById(R.id.tvPokeIndex);
            ivPokePic = view.findViewById(R.id.ivPokePic);
            progressBar = view.findViewById(R.id.progressBar);
            //btnViewPoke = view.findViewById(R.id.btnViewPoke);
        }
    }

    //load the don't show images setting
    private void loadPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.group3", Context.MODE_PRIVATE);
        showImages = !sharedPreferences.getBoolean("dontDisplayImages", false);
    }

    /***
     * isColorDark - Checks if a color is dark based on a RGB value. If the color is it will return a boolean.
     * @param color
     * @return
     */
    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.7){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
}
