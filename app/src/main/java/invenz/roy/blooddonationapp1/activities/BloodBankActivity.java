package invenz.roy.blooddonationapp1.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.adapters.BloodBankAdapter;
import invenz.roy.blooddonationapp1.models.BloodBank;
import invenz.roy.blooddonationapp1.utils.Urls;

public class BloodBankActivity extends AppCompatActivity {

    private static final String TAG = "roy" ;
    private Spinner citySpinner;
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayAdapter cityAdapter;
    private String selectedCity;
    private List<BloodBank> bloodBanks ;
    private RecyclerView recyclerViewBloodBank;
    private BloodBankAdapter bloodBankAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);


        citySpinner = findViewById(R.id.idCitySpinner_BloodBank);
        recyclerViewBloodBank = findViewById(R.id.idBloodBankRecView_BloodBankAct);
        recyclerViewBloodBank.setLayoutManager(new LinearLayoutManager(BloodBankActivity.this));

        cityList.add("-- select city --");
        cityList.add("Barisal");
        cityList.add("Chittagong");
        cityList.add("Dhaka");
        cityList.add("Khulna");
        cityList.add("Mymensingh");
        cityList.add("Rajshahi");
        cityList.add("Rangpur");
        cityList.add("Sylhet");

        cityAdapter = new ArrayAdapter(BloodBankActivity.this, android.R.layout.simple_list_item_1, cityList);
        citySpinner.setAdapter(cityAdapter);

        /*###                      when city is selected              ####*/
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {


                if (position == 0){
                    Snackbar.make(findViewById(android.R.id.content), "Select city please", Snackbar.LENGTH_SHORT).show();

                }else {
                    selectedCity = cityList.get(position);


                    /*###          getting Blood Banks              ###*/
                    getBloodBanksByCityName(selectedCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*                                                                 */



    }


    /*###          my method for getting Blood Banks by city             ###*/
    private void getBloodBanksByCityName(final String selectedCity) {


        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.GET_BLOOD_BANKS_BY_CITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        bloodBanks = new ArrayList<>();

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            
                            for(int i=0; i<jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = String.valueOf(jsonObject.getInt("id"));
                                String name = jsonObject.getString("name");
                                String address = jsonObject.getString("address");
                                String phoneNo = jsonObject.getString("phone");
                                String openAt = jsonObject.getString("openTime");
                                String success = jsonObject.getString("success");

                                BloodBank bloodBank = new BloodBank(id, name, address, phoneNo, openAt);
                                bloodBanks.add(bloodBank);


                            }

                            bloodBankAdapter = new BloodBankAdapter(BloodBankActivity.this, bloodBanks);
                            recyclerViewBloodBank.setAdapter(bloodBankAdapter);
                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d(TAG, "onErrorResponse(BloodBankActivity): "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<>();
                map.put("city", selectedCity);

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BloodBankActivity.this);
        requestQueue.add(stringRequest);
        
        
    }
    
    /*                                                                  */




}
