package invenz.roy.blooddonationapp1.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.utils.Urls;

public class PostRequestActivity extends AppCompatActivity {

    private static final String TAG = "roy" ;
    private Spinner spCity, spBloodGroup;
    private EditText etLocation, etPhoneNo, etDescription;
    private Button btPost;

    private ArrayAdapter<String> cityAdapter, bloodGroupAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request);


        /*###                  initialization                   ####*/
        etLocation = findViewById(R.id.idLocation_postRequest);
        etPhoneNo = findViewById(R.id.idPhoneNo_postRequest);
        etDescription = findViewById(R.id.idDescription_postRequest);

        spBloodGroup = findViewById(R.id.idBloodGroup_postRequest);
        spCity = findViewById(R.id.idCity_postRequest);

        btPost = findViewById(R.id.idPost_postRequest);

        String[] city = {"--Select City--", "Barisal", "Chittagong", "Dhaka", "Khulna", "Mymensingh", "Rajshahi", "Rangpur", "Sylhet"};
        String[] bloodGroups = {"--Select Blood Group--", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

        cityAdapter = new ArrayAdapter<>(PostRequestActivity.this, android.R.layout.simple_list_item_1, city);
        bloodGroupAdapter = new ArrayAdapter<>(PostRequestActivity.this, android.R.layout.simple_list_item_1, bloodGroups);

        spCity.setAdapter(cityAdapter);
        spBloodGroup.setAdapter(bloodGroupAdapter);


        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spCity.getSelectedItemId() == 0){
                    Toast.makeText(PostRequestActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                }else if (spBloodGroup.getSelectedItemId() == 0){
                    Toast.makeText(PostRequestActivity.this, "Please Select Blood Group", Toast.LENGTH_SHORT).show();
                }else if (etLocation.getText().toString().equals("")){
                    etLocation.setError("Please enter location");
                    //Toast.makeText(PostRequestActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                }else if (etPhoneNo.getText().toString().equals("")){
                    etPhoneNo.setError("Please enter Contact No.");
                    //Toast.makeText(PostRequestActivity.this, "Please enter Contact No.", Toast.LENGTH_SHORT).show();
                }else if (etDescription.getText().toString().equals("")){
                    etDescription.setError("Please enter details");
                    //Toast.makeText(PostRequestActivity.this, "Please enter details", Toast.LENGTH_SHORT).show();
                }else {


                    String sCity = spCity.getSelectedItem().toString();
                    String sBloodGroup = spBloodGroup.getSelectedItem().toString();
                    String sLocation = etLocation.getText().toString();
                    String sPhoneNo = etPhoneNo.getText().toString();
                    String sDetails = etDescription.getText().toString();

                    //Log.d(TAG, "onClick(PostRequestActivity): " + sCity + ", " + sBloodGroup + ", " + sLocation + ", " + sPhoneNo + ", " + sDetails);
                    Toast.makeText(PostRequestActivity.this, "Ok", Toast.LENGTH_SHORT).show();

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String firebaseId = mAuth.getCurrentUser().getUid();

                    /*####             method for sending request to the server              ####*/
                    sendRequestToServer(sCity, sBloodGroup, sLocation, sPhoneNo, sDetails, firebaseId);

                }
            }
        });



    }


    /*####             method for sending request to the server              ####*/
    private void sendRequestToServer(final String sCity, final String sBloodGroup, final String sLocation, final String sPhoneNo, final String sDetails, final String firebaseId) {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.SAVE_POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String msg = jsonObject.getString("message");
                            String code = jsonObject.getString("code");

                            if (code.equals("yes")){

                                //Toast.makeText(PostRequestActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();

                                etLocation.setText("");
                                etPhoneNo.setText("");
                                etDescription.setText("");

                            }else {

                                //Toast.makeText(PostRequestActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(PostReqActivity): "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> postMap = new HashMap<>();

                postMap.put("Blood_Group", sBloodGroup);
                postMap.put("City", sCity);
                postMap.put("Location", sLocation);
                postMap.put("PhoneNo", sPhoneNo);
                postMap.put("Details", sDetails);
                postMap.put("firebaseId", firebaseId);

                return postMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PostRequestActivity.this);
        requestQueue.add(stringRequest);

    }
    /*                                                                              */


}
