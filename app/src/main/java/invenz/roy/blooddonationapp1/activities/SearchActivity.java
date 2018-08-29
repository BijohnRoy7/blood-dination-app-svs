package invenz.roy.blooddonationapp1.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.adapters.SearchDonorAdapter;
import invenz.roy.blooddonationapp1.models.SearchDonor;
import invenz.roy.blooddonationapp1.utils.Urls;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "roy" ;
    private FirebaseAuth mAuth;
    private List<SearchDonor> donorList;
    private SearchDonorAdapter donorAdapter;

    private EditText etSeaech;
    private RecyclerView recyclerViewDonors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSeaech = findViewById(R.id.idSearchDonor_searchActivity);
        recyclerViewDonors= findViewById(R.id.idSeacrDonorRecView_searchActivity);
        recyclerViewDonors.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        mAuth = FirebaseAuth.getInstance();

        donorList = new ArrayList<>();

        /*###                  getting all donor list                     ####*/
        getAllDonors();



        /*####                searching donor bt blood group, division or district                      ###*/
        etSeaech.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // getAllDonors();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")){
                    Toast.makeText(SearchActivity.this, "Ok", Toast.LENGTH_SHORT).show();

                    donorAdapter = new SearchDonorAdapter(SearchActivity.this, donorList);
                    recyclerViewDonors.setAdapter(donorAdapter);


                }else {

                    Toast.makeText(SearchActivity.this, ""+s, Toast.LENGTH_SHORT).show();

                    String searchedText = s.toString().toLowerCase().trim();
                    Iterator<SearchDonor> it = donorList.iterator();

                    List<SearchDonor> donors = new ArrayList<>();

                    while (it.hasNext()){

                        SearchDonor donor = it.next();

                        if (donor.getBloodGroup().toLowerCase().contains(searchedText)
                                || donor.getDivition().toLowerCase().contains(searchedText)
                                || donor.getDistrict().toLowerCase().contains(searchedText)){

                            donors.add(donor);
                        }

                    }

                    donorAdapter = new SearchDonorAdapter(SearchActivity.this, donors);
                    recyclerViewDonors.setAdapter(donorAdapter);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*                                                                                  */



    }



    /*###                  method for getting all donor list                     ####*/
    private void getAllDonors() {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.SEARCH_ALL_DONORS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        donorList.clear();

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0; i<jsonArray.length(); i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String user_name = jsonObject.getString("user_name");
                                String token = jsonObject.getString("token");
                                String mobile_no = jsonObject.getString("mobile_no");
                                String mobile_no_2 = jsonObject.getString("mobile_no_2");
                                String email = jsonObject.getString("email");
                                String division = jsonObject.getString("division");
                                String district = jsonObject.getString("district");
                                String blood_group = jsonObject.getString("blood_group");


                                SearchDonor donor = new SearchDonor(id,user_name,token, mobile_no, mobile_no_2, email, division, district,blood_group, "yo!");

                                donorList.add(donor);
                            }

                            donorAdapter = new SearchDonorAdapter(SearchActivity.this, donorList);
                            recyclerViewDonors.setAdapter(donorAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(SearchActivity): "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("firebase_uid", mAuth.getUid());

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SearchActivity.this);
        requestQueue.add(stringRequest);

    }



}
