package com.example.pokeviewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokeviewer.api.models.evolution.PokemonEvolutionDetails;
import com.example.pokeviewer.api.models.list.PokemonListItem;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.List;

public class EvolutionAdapter extends RecyclerView.Adapter<EvolutionAdapter.EvolutionViewHolder> {

    List<Evolution> evolutionList;
    Context context;
    String headerSpeciesName;
    boolean showImages = true;

    public EvolutionAdapter(List<Evolution> evolutionList, Context context, String headerSpeciesName) {
        this.evolutionList = evolutionList;
        this.context = context;
        this.headerSpeciesName = headerSpeciesName;
    }

    @NonNull
    @Override
    public EvolutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create an instance of the layout_evolution_item view
        View view = LayoutInflater.from(context).inflate(R.layout.layout_evolution_item,parent,false);

        return new EvolutionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvolutionViewHolder holder, int position) {
        PokemonListItem preEvolution = evolutionList.get(position).preEvolution;
        PokemonListItem postEvolution = evolutionList.get(position).postEvolution;

        holder.btnViewPreInfo.setText("View " + preEvolution.name);
        holder.btnViewPostInfo.setText("View " + postEvolution.name);

        // Set the pre-evolution textview to the proper name and use Picasso to fetch the corresponding image
        holder.tvwPreEvolution.setText(preEvolution.getNameCapital());

        //check if the don't show images setting is selected
        loadPreferences();
        if (showImages)
            Picasso.get().load(preEvolution.getImageURL()).fit().into(holder.imgPreEvolution);

        // Both buttons should be visible by default, the might vanish if certain conditions are met
        holder.btnViewPreInfo.setVisibility(View.VISIBLE);
        holder.btnViewPostInfo.setVisibility(View.VISIBLE);

        // If either the pre-evolution or post-evolution pokemon are the pokemon from which this instance
        // of the evolution activity was created, set it's "view info" button to vanish. The user can
        // press the "back" button at the top of the activity to navigate back to that pokemon's info
        if (preEvolution.name.equals(headerSpeciesName.toLowerCase())) {
            holder.btnViewPreInfo.setVisibility(View.GONE);
        }
        else if (postEvolution.name.equals(headerSpeciesName.toLowerCase())) {
            holder.btnViewPostInfo.setVisibility(View.GONE);
        }

        // If the post-evolution indicates that there are no evolutions,
        if (postEvolution.name.equals("noEvo")) {
            // Set the post-evolution textview, imageview, and "view info" button to vanish, and set
            // the "evolves to" textview to indicate that there are no evolutions
            holder.tvwPostEvolution.setVisibility(View.GONE);
            holder.imgPostEvolution.setVisibility(View.GONE);
            holder.imgDownArrow.setVisibility(View.GONE);
            holder.btnViewPostInfo.setVisibility(View.GONE);
            holder.tvwEvolvesTo.setText("This Pokemon has no evolutions");
        }
        // Otherwise,
        else {
            // Set the "evolves to" textview to list conditions for evolution
            holder.tvwEvolvesTo.setText(formatEvolutionDetails(postEvolution.evolutionDetails));

            // Set the post-evolution textview to the proper name and use Picasso to fetch the corresponding image
            holder.tvwPostEvolution.setText(postEvolution.getNameCapital());
            //check if the don't show images setting is selected
            loadPreferences();
            if (showImages)
                Picasso.get().load(postEvolution.getImageURL()).fit().into(holder.imgPostEvolution);
        }

        // When btnViewPreInfo is clicked,
        holder.btnViewPreInfo.setOnClickListener(view -> {
            // Create an intent for a PokeInfoActivity instance
            Intent intent = new Intent(context, PokeInfoActivity.class);
            String preEvoID = null;

            // Attempt to fetch the corresponding pokemon's id
            try {
                preEvoID = preEvolution.getID();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            // Store the pokemon's id in an intent extra and use the intent to open up that pokemon's
            // info activity
            intent.putExtra("ID", preEvoID);
            context.startActivity(intent);
        });

        // When btnViewPostInfo is clicked,
        holder.btnViewPostInfo.setOnClickListener(view -> {
            // Create an intent for a PokeInfoActivity instance
            Intent intent = new Intent(context, PokeInfoActivity.class);
            String postEvoID = null;

            // Attempt to fetch the corresponding pokemon's id
            try {
                postEvoID = postEvolution.getID();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            // Store the pokemon's id in an intent extra and use the intent to open up that pokemon's
            // info activity
            intent.putExtra("ID", postEvoID);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return evolutionList.size();
    }

    // Method formats conditions for a pokemon's evolution
    public String formatEvolutionDetails(PokemonEvolutionDetails evolutionDetails) {
        String evoDetails = "";

        // For each possible condition, if it is not either null or false, add the condition to the
        // string list of conditions
        if (!(evolutionDetails.gender == null)) {
            if (evolutionDetails.gender == 1) {
                evoDetails += "Must Be Female";
            }
            if (evolutionDetails.gender == 2) {
                evoDetails += "Must Be Male";
            }
        }
        if (!(evolutionDetails.held_item == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Be Holding " + splitAndCapitalize(evolutionDetails.held_item.name);
        }
        if (!(evolutionDetails.item == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Requires A " + splitAndCapitalize(evolutionDetails.item.name);
        }
        if (!(evolutionDetails.known_move == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Know " + splitAndCapitalize(evolutionDetails.known_move.name);
        }
        if (!(evolutionDetails.known_move_type == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Know A " + splitAndCapitalize(evolutionDetails.known_move_type.name) + " Type Move";
        }
        if (!(evolutionDetails.location == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Be At " + splitAndCapitalize(evolutionDetails.location.name);
        }
        if (!(evolutionDetails.min_affection == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Requires At Least " + evolutionDetails.min_affection + " Affection";
        }
        if (!(evolutionDetails.min_beauty == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Requires At Least " + evolutionDetails.min_beauty + " Beauty";
        }
        if (!(evolutionDetails.min_happiness == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Requires At Least " + evolutionDetails.min_happiness + " Happiness";
        }
        if (!(evolutionDetails.min_level == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Level " + evolutionDetails.min_level;
        }
        if (evolutionDetails.needs_overworld_rain == true) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Be Raining";
        }
        if (!(evolutionDetails.party_species == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Have A " + splitAndCapitalize(evolutionDetails.party_species.name) + " In Party";
        }
        if (!(evolutionDetails.party_type == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Have A " + splitAndCapitalize(evolutionDetails.party_type.name) + " Type Pokemon In Party";
        }
        if (!(evolutionDetails.relative_physical_stats == null)) {
            evoDetails = addCommaSpace(evoDetails);
            if (evolutionDetails.relative_physical_stats == 1) {
                evoDetails += "Must Have Higher Attack Than Defense";
            }
            if (evolutionDetails.relative_physical_stats == 0) {
                evoDetails += "Must Have Attack Equal To Defense";
            }
            if (evolutionDetails.relative_physical_stats == -1) {
                evoDetails += "Must Have Lower Attack Than Defense";
            }
        }
        if (!(evolutionDetails.time_of_day.isEmpty())) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Be " + splitAndCapitalize(evolutionDetails.time_of_day);
        }
        if (!(evolutionDetails.trade_species == null)) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Be Traded With " + splitAndCapitalize(evolutionDetails.trade_species.name);
        }
        if (evolutionDetails.turn_upside_down == true) {
            evoDetails = addCommaSpace(evoDetails);
            evoDetails += "Must Be Holding 3DS Upside Down";
        }

        // If all evolution conditions were null or false,
        if(evoDetails.isEmpty()) {
            // List the trigger for the evolution instead
            evoDetails += "Triggered By " + splitAndCapitalize(evolutionDetails.trigger.name);
        }

        return evoDetails;
    }

    // Method adds a comma and space to a string
    public String addCommaSpace(String evoDetails) {
        if (!evoDetails.equals("")) {
            evoDetails += ", ";
        }
        return evoDetails;
    }

    // Method removes dashes and capitalizes each word in a string
    public String splitAndCapitalize(String detail) {
        String temp = "";
        String[] words = detail.split("-");

        for (String word : words) {
            temp += word.substring(0,1).toUpperCase() + word.substring(1) + " ";
        }

        return temp.trim();
    }

    // ---------------------------------------------------------------------------------------------

    class EvolutionViewHolder extends RecyclerView.ViewHolder {
        TextView tvwPreEvolution;
        TextView tvwEvolvesTo;
        TextView tvwPostEvolution;
        ImageView imgPreEvolution;
        ImageView imgPostEvolution;
        ImageView imgDownArrow;
        Button btnViewPreInfo;
        Button btnViewPostInfo;

        public EvolutionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvwPreEvolution = itemView.findViewById(R.id.tvwPreEvolution);
            tvwEvolvesTo = itemView.findViewById(R.id.tvwEvolvesTo);
            tvwPostEvolution = itemView.findViewById(R.id.tvwPostEvolution);
            imgPreEvolution = itemView.findViewById(R.id.imgPreEvolution);
            imgPostEvolution = itemView.findViewById(R.id.imgPostEvolution);
            imgDownArrow = itemView.findViewById(R.id.imgDownArrow);
            btnViewPreInfo = itemView.findViewById(R.id.btnViewPreInfo);
            btnViewPostInfo = itemView.findViewById(R.id.btnViewPostInfo);
        }
    }

    //load the don't show images setting
    private void loadPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.group3", Context.MODE_PRIVATE);
        showImages = !sharedPreferences.getBoolean("dontDisplayImages", false);
    }
}
