package invenz.roy.blooddonationapp1.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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

public class MyAccountActivity extends AppCompatActivity {

    private static final String TAG = "roy";
    private TextView tvName, tvDivision, tvDistrict, tvContactNo, tvEmail, tvBloodGroup, tvDonationDate, tvDontKnow;

    private int myDate, myMonth, myYear;
    private DatePickerDialog datePickerDialog;

    private AlertDialog.Builder alertDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        tvName = findViewById(R.id.idName_myAccountActivity);
        tvDivision = findViewById(R.id.idDivision_myAccountActivity);
        tvDistrict = findViewById(R.id.idDistrict_myAccountActivity);
        tvEmail = findViewById(R.id.idEmail_myAccountActivity);
        tvBloodGroup = findViewById(R.id.idBloodGroup_myAccountActivity);
        tvDonationDate = findViewById(R.id.idDonationDate_myAccountActivity);
        tvContactNo = findViewById(R.id.idMobile_myAccountActivity);
        tvDontKnow = findViewById(R.id.idDontKnow_myAccountActivity);

        alertDialogBuilder = new AlertDialog.Builder(MyAccountActivity.this);


        /*#####          getting user info here             ####*/
        getUserInfo();



        /*###########*/
        tvDonationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                final int date = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                datePickerDialog = new DatePickerDialog(MyAccountActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Toast.makeText(MyAccountActivity.this, dayOfMonth+"-"+month+"-"+year, Toast.LENGTH_SHORT).show();

                        myDate = dayOfMonth;
                        myMonth = month+1;
                        myYear = year;

                        //tvLastDonationDate.setText(myDate+"/"+myMonth+"/"+myYear);

                        final String donationDate = myDate+"/"+myMonth+"/"+myYear;

                        /*###          alert dialog        ###*/
                        alertDialogBuilder.setTitle("Donation date change...");
                        alertDialogBuilder.setMessage("Do you really want to change donation date? ");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                saveDonationDate(donationDate);
                                tvDonationDate.setText(donationDate);

                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", null);
                        alertDialogBuilder.show();



                    }
                }, date, month,year);

                datePickerDialog.show();


            }
        });
        /*                                                  */


        tvDontKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*###          alert dialog        ###*/
                alertDialogBuilder.setTitle("Donation date change...");
                alertDialogBuilder.setMessage("Do you really want to change donation date? ");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        saveDonationDate("Dont know");
                        tvDonationDate.setText("Don't know");

                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                alertDialogBuilder.show();


            }
        });

    }

    private void saveDonationDate(final String donationDate) {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.SAVE_DONATION_DATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("message");
                            Toast.makeText(MyAccountActivity.this, msg, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(MyAccountActivity): "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String , String> map = new HashMap<>();
                map.put("firebase_id", FirebaseAuth.getInstance().getUid());
                map.put("donation_date", donationDate);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MyAccountActivity.this);
        requestQueue.add(stringRequest);

    }


    /*#####          getting user info here             ####*/
    private void getUserInfo() {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.GET_USER_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String userName = jsonObject.getString("user_name");
                            String mobileNo = jsonObject.getString("mobile_no");
                            String email = jsonObject.getString("email");
                            String division = jsonObject.getString("division");
                            String district = jsonObject.getString("district");
                            String bloodGroup = jsonObject.getString("blood_group");
                            String mobileNo2 = jsonObject.getString("mobile_no_2");
                            String lastDonationDate = jsonObject.getString("last_donation_date");

                            tvName.setText(userName);
                            tvContactNo.setText(mobileNo);
                            tvEmail.setText(email);
                            tvDivision.setText(division);
                            tvDistrict.setText(district);
                            tvBloodGroup.setText(bloodGroup);
                            //tv.setText(mobileNo2);
                            tvDonationDate.setText(lastDonationDate);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(MyAccountActivity) 2: "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("firebase_id", FirebaseAuth.getInstance().getUid());
                return map;
            }
        };



        RequestQueue requestQueue = Volley.newRequestQueue(MyAccountActivity.this);
        requestQueue.add(stringRequest);

    }







}
