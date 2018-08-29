package invenz.roy.blooddonationapp1.activities;

import android.app.DatePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.utils.Urls;

public class AddDonorActivity extends AppCompatActivity {


    private static final String TAG = "roy" ;
    private EditText etDonorName, etPhoneNo, etAddress;
    private TextView tvLastDonationDate, tvDateDontKnow;
    private Button btAddDonor;
    private Spinner spCity, spBloodGroup;
    private String sPhoneNo, sDonorName,sAddress, sCity, sBloodGroup, mPhone;
    private ArrayAdapter<String> cityAdapter,  bloodGroupAdapter ;

    private int myDate, myMonth, myYear;

    private DatePickerDialog datePickerDialog;
    private String donationDate, sDonationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donor);

        etDonorName = findViewById(R.id.idDonorName_AddDonorActivity);
        etPhoneNo = findViewById(R.id.idContactNo_AddDonorActivity);
        spCity = findViewById(R.id.idCity_AddDonorActivity);
        spBloodGroup = findViewById(R.id.idBloodGroup_AddDonorActivity);
        etAddress = findViewById(R.id.idAddress_AddDonorActivity);
        tvLastDonationDate = findViewById(R.id.idLastDonationDate_AddDonorActivity);
        tvDateDontKnow = findViewById(R.id.idDateDontKnow_AddDonorActivity);
        btAddDonor = findViewById(R.id.idAdd_AddDonorActivity);


        String[] cities = {"-- Select City --", "Barisal", "Chittagong", "Dhaka", "Khulna", "Mymensingh", "Rajshahi", "Rangpur", "Sylhet"};
        String[] bloodGroups = {"-- Select Blood Group --","A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

        cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        bloodGroupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bloodGroups);

        spCity.setAdapter(cityAdapter);
        spBloodGroup.setAdapter(bloodGroupAdapter);


        /*###                       Click text to show DatePickerDialog                       ###*/
        tvLastDonationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                final int date = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                datePickerDialog = new DatePickerDialog(AddDonorActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Toast.makeText(AddDonorActivity.this, dayOfMonth+"-"+month+"-"+year, Toast.LENGTH_SHORT).show();

                        myDate = dayOfMonth;
                        myMonth = month+1;
                        myYear = year;

                        //tvLastDonationDate.setText(myDate+"/"+myMonth+"/"+myYear);

                        donationDate = myDate+"/"+myMonth+"/"+myYear;

                        tvLastDonationDate.setText(donationDate);

                    }
                }, date, month,year);

                datePickerDialog.show();

            }
        });
        /*                                                                      */

        tvDateDontKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLastDonationDate.setText("Dont know");
            }
        });


        btAddDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sDonorName = etDonorName.getText().toString().trim();
                sPhoneNo = etPhoneNo.getText().toString().trim();
                sAddress = etAddress.getText().toString().trim();
                sDonationDate = tvLastDonationDate.getText().toString();


                if (sDonorName.equals("")){
                    etDonorName.setError("Donor Name Please");

                }else  if(sPhoneNo.equals("")){
                    etPhoneNo.setError("Donor Phone No please");

                }else  if(sPhoneNo.length()<11){
                    etPhoneNo.setError("Phone No length must be 11");

                }else if(sAddress.equals("")){
                    etAddress.setError("Donor Address Please");

                }else if (spCity.getSelectedItemId() == 0){
                    Toast.makeText(AddDonorActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();

                }else if (spBloodGroup.getSelectedItemId() == 0){
                    Toast.makeText(AddDonorActivity.this, "Please Select Blood Group", Toast.LENGTH_SHORT).show();

                }else if(sDonationDate.equals("") || sDonationDate.equals("Set Now")){
                    Toast.makeText(AddDonorActivity.this, "Please select Last donation date", Toast.LENGTH_SHORT).show();

                }else {
                    //Snackbar.make(findViewById(android.R.id.content), "Donor Added Successfully ", Snackbar.LENGTH_SHORT).show();

                    sCity = spCity.getSelectedItem().toString();
                    sBloodGroup = spBloodGroup.getSelectedItem().toString();

                    /*###                 saving donor to the server               ###*/
                    addDonorByUser();

                }


            }
        });


    }



    /*###                 saving donor to the server               ###*/
    private void addDonorByUser() {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.ADD_DONOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("message");
                            String code = jsonObject.getString("code");

                            if (code.equals("success")){
                                //Toast.makeText(AddDonorActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();

                                etDonorName.setText("");
                                etAddress.setText("");
                                etPhoneNo.setText("");

                            }else {
                                //Toast.makeText(AddDonorActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(AddDonorActivity): ");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> addDonorMap = new HashMap<>();

                addDonorMap.put("who_added_firebase_id", FirebaseAuth.getInstance().getUid());
                addDonorMap.put("donor_name", sDonorName);
                addDonorMap.put("address", sAddress);
                addDonorMap.put("phone_no", sPhoneNo);
                addDonorMap.put("city", sCity);
                addDonorMap.put("blood_group", sBloodGroup);
                addDonorMap.put("last_donation_date", sDonationDate);

                return addDonorMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddDonorActivity.this);
        requestQueue.add(stringRequest);
    }
    /*                                                                                  */

}
