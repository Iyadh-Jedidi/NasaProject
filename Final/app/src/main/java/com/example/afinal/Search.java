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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.Model.Category;
import com.example.afinal.Model.SearchModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class Search extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button loadBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    public void onStart(){
        super.onStart();

    }

    Category catog=new Category();
    String history="";
    public void search( View view){
        get();

    }

    //get the informaion from API
    void get() {
        RequestQueue requestQueue = Volley.newRequestQueue(Search.this);
        View contentView =findViewById(R.id.test);
        EditText t = contentView.findViewById(R.id.search);
        String search;
        search= t.getText().toString();

            history+=search+"\n";
            SQLiteDatabase myDB = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
            myDB.execSQL(
                    "CREATE TABLE IF NOT EXISTS history (description VARCHAR(200))"
            );
            ContentValues row1 = new ContentValues();
            row1.put("description", history);
            myDB.insert("history", null, row1);
            history="";
            String url = "https://genelab-data.ndc.nasa.gov/genelab/data/search?term="+search+"&type=nih_geo_gse";


            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                JSONObject hits1 = response.getJSONObject("hits");
                                JSONArray hits2= hits1.getJSONArray("hits");
                                if (hits2.length() != 0) {
                                    JSONObject c = hits2.getJSONObject(0);
                                    JSONObject b = c.getJSONObject("_source");
                                    catog.setExplanation((String) b.get("Study Description"));
                                    System.out.println("3malet recherche");
                                    if (catog.getExplanation() != null || catog.getExplanation() != ""){
                                        load();
                                    }
                                    else{
                                        catog.setExplanation("No data found");
                                        load();
                                    }


                                }
                                else{
                                    catog.setExplanation("No data found");
                                }

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

    // load information in the TextView
    void  load(){
        View contentViewLoad =findViewById(R.id.test);
        TextView tLoad = contentViewLoad.findViewById(R.id.description);
        tLoad.setText(catog.getExplanation());
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
            Intent search= new Intent(Search.this, Search.class);
            startActivity(search);
        }else if(id == R.id.nav_home){
            Intent home= new Intent(Search.this,MainActivity.class);
            startActivity(home);
        }else if(id == R.id.nav_history){
            Intent historyIntent= new Intent(Search.this,History.class);

            startActivity(historyIntent);

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
