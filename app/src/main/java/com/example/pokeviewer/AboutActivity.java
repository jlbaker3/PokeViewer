package com.example.pokeviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    Button btnGithub;
    RecyclerView recContributors;
    ArrayList<Contributor> contributors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Code for the back button in the top bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        //Get all of the views
        btnGithub = findViewById(R.id.btnGithub);
        recContributors = findViewById(R.id.recContributors);

        contributors = new ArrayList<>();
        //statically add group members to contributor array list
        contributors.add(new Contributor("Hawkins Amos",Uri.parse("https://github.com/SirAwesome-5")));
        contributors.add(new Contributor("Joshua Baker",Uri.parse("https://github.com/jlbaker3")));
        contributors.add(new Contributor("Chris Bellefeuille",Uri.parse("https://github.com/Chris-Bellefeuille-SVSU")));
        contributors.add(new Contributor("Brendan Fuller",Uri.parse("https://github.com/ImportProgram")));
        contributors.add(new Contributor("Ryley Taub",Uri.parse("https://github.com/rtaub")));

        //Make the adapter and bind it
        com.example.pokeviewer.ContributorAdapter adapter = new com.example.pokeviewer.ContributorAdapter(contributors,this);
        recContributors.setAdapter(adapter);
        recContributors.setLayoutManager(new LinearLayoutManager(this));



        //Button event for the other button to the project of the github account
        btnGithub.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://github.com/CS403Group3/");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        });
    }

    //Called when the toolbar activity is pressed
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Close the activity
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}