package com.example.afinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Rover extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_rover);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mars Rover");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Button buttonParse = findViewById(R.id.loadBtn);

        mQueue = Volley.newRequestQueue(this);

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse() {

        String url ="https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=axzwR3f1fLgv6rJaG7LEJQRkr7I67qPRe8U2IOuz";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("photos");

                            LinearLayout gallery = findViewById(R.id.gallery);
                            LayoutInflater inflater =LayoutInflater.from(Rover.this);
                            for (int i = 0; i < 7; i++) {
                                JSONObject pic = jsonArray.getJSONObject(i);
                                String date = pic.getString("earth_date");
                                String url=pic.getString("img_src");
                                    View view = inflater.inflate(R.layout.item,gallery,false);
                                    TextView textView = view.findViewById(R.id.text);

                                    ImageView imageView = view.findViewById(R.id.image);
                                textView.setText("Date: "+date);
                                Picasso.with(Rover.this).load(url).into(imageView);
                                    gallery.addView(view);



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(10000,2, (float) 2.0));
        mQueue.add(request);
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
            Intent search= new Intent(Rover.this, Search.class);
            startActivity(search);
        }else if(id == R.id.nav_home) {
            Intent home = new Intent(Rover.this, MainActivity.class);
            startActivity(home);
        }else if(id == R.id.nav_rover){
            Intent rover= new Intent(Rover.this,Rover.class);
            startActivity(rover);
        }else if(id == R.id.nav_history){
            Intent home= new Intent(Rover.this,History.class);
            SharedPreferences gameSettings = getSharedPreferences("MyGamePreferences", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = gameSettings.edit();
            prefEditor.putString("history", "test");
            prefEditor.commit();
            startActivity(home);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}