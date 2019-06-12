package com.example.afinal;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.Model.Category;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Picture of the day");
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        get();
        load();
    }
    Category catog=new Category();
    public void apod( View view){
        get();
        load();
        load();
    }


    void get() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        String url = "https://api.nasa.gov/planetary/apod?api_key=axzwR3f1fLgv6rJaG7LEJQRkr7I67qPRe8U2IOuz";

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            catog.setDate ((String)response.get("date"));
                            catog.setExplanation( (String)response.get("explanation"));
                            catog.setUrl((String)response.get("url"));
                            catog.setTitle((String)response.get("title"));
                            System.out.println(catog.getTitle());

                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {}
                });
        requestQueue.add(jsonObjectRequest);
    }
    void  load(){
        View contentView =findViewById(R.id.test);
        TextView t = contentView.findViewById(R.id.titleViewID);
        t.setText(catog.getTitle());
        System.out.println(t.getText());
        TextView d = contentView.findViewById(R.id.dateID);
        d.setText(catog.getDate());
        System.out.println(catog.getDate());
        TextView desc =  contentView.findViewById(R.id.descID);
        desc.setText(catog.getExplanation());
        System.out.println(desc.getText());
        ImageView i = contentView.findViewById(R.id.imageID);
        System.out.println("imageurl"+catog.getUrl());
        Picasso.with(getBaseContext()).load(catog.getUrl()).into(i);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            // Handle the camera action
            Intent search = new Intent(MainActivity.this, Search.class);
            startActivity(search);
        } else if (id == R.id.nav_home) {
            Intent home = new Intent(MainActivity.this, MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_history) {
            Intent historyIntent = new Intent(MainActivity.this, History.class);
            startActivity(historyIntent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
