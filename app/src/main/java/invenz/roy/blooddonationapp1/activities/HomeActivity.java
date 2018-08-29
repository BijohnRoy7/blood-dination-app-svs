package invenz.roy.blooddonationapp1.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.fragments.EditUserInfoFragment;
import invenz.roy.blooddonationapp1.utils.Constants;
import invenz.roy.blooddonationapp1.utils.Urls;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "roy" ;
    private String phoneNumber, userName, email, division, district, bloodGroup;
    private FirebaseAuth mAuth;
    private String userId;
    private Fragment fragment = null;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /*###                 checking authentication              ###*/
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }



        /*###                 getting intent values                ###*/
        userId = mAuth.getUid();
        phoneNumber = getIntent().getStringExtra("phone_no");
        userName = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        division = getIntent().getStringExtra("division");
        district = getIntent().getStringExtra("district");
        bloodGroup = getIntent().getStringExtra("blood_group");

        Log.d("aaa", "onCreate(Home): "+phoneNumber);




        /*###          sending values to the server       ###*/
        saveUserIntoServer();



        /*###                    checking token and storing to the server              ###*/
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putInt(Constants.FULLY_REGISTERED_KEY, 2).commit();

        String token = sharedPreferences.getString(Constants.TOKEN_KEY, "token");
        //Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();

        if (!token.equals("token")){
            //Toast.makeText(MainActivity.this, "Not null", Toast.LENGTH_SHORT).show();
            sendRegistrationToServer(token);
        }

    }


    /*           sendRegistrationToServer                   */
    private void sendRegistrationToServer(final String token) {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.STORE_TOKEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("message");
                            String code = jsonObject.getString("code");
                            Log.d(TAG, "onResponse(HomeActivity): "+msg);
                            Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse(HomeActivity): "+error );
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String ,String> tokenMap = new HashMap<>();
                tokenMap.put("firebase_id", userId);
                tokenMap.put("token", token);
                return tokenMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }



    /*####                          storing firebaseId, username & phoneNo into server                   ####*/
    private void saveUserIntoServer() {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.SAVE_USER_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject myResponse = new JSONObject(response);

                            String msg = myResponse.getString("message");
                            String code = myResponse.getString("code");
                            Log.e(TAG, "onResponse(MainActivity): "+msg);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(MainActivity): "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> userMap = new HashMap<>();
                userMap.put("firebase_id", userId);
                userMap.put("user_name", userName);
                userMap.put("user_phone_no", phoneNumber);
                userMap.put("email", email);
                userMap.put("division", division);
                userMap.put("district", district);
                userMap.put("blood_group", bloodGroup);
                return userMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);

    }






    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_edit_info) {

             fragment = new EditUserInfoFragment();
             setFragment(fragment);

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;



    }

    public void setFragment(Fragment fragment) {


        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.idFrameLayout, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }
}
