package invenz.roy.blooddonationapp1.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.utils.Urls;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserInfoFragment extends Fragment {

    private static final String TAG ="roy" ;
    private TextView tvDonationDate, tvDontKnow;
    private EditText etUserName, etEmail, etPhoneNo, etAlternatePhoneNo, etBloodGroup, etDivision, etDistrict;
    private Button btEditInfo;
    private View view;
    private FirebaseAuth mAuth;
    private String userId;


    public EditUserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_edit_user_info, container, false);

        /*###            calling method for initializing components           ###*/
        init();


        /*#####          calling method for getting user info             ####*/
        getUserInfo();


        tvDontKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDonationDate.setText("Dont know");
            }
        });

        /*###              when edit button is clicked              ###*/
        btEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sUserName = etUserName.getText().toString().trim();
                String sPhonNo = etPhoneNo.getText().toString().trim();
                String sEmail = etEmail.getText().toString().trim();
                String sDivision = etDivision.getText().toString().trim();
                String sDistrict = etDistrict.getText().toString().trim();
                String sAlternatePhoneNo = etAlternatePhoneNo.getText().toString().trim();
                String sDonationDate  = tvDonationDate.getText().toString().trim();

                if (sUserName.isEmpty() || sPhonNo.isEmpty() || sEmail.isEmpty() || sDivision.isEmpty() || sDistrict.isEmpty()){

                    Toast.makeText(getContext(), "Please fill requiered fields", Toast.LENGTH_SHORT).show();

                }else {

                    editUserInfo(sUserName, sPhonNo, sEmail, sDivision, sDistrict, sAlternatePhoneNo, sDonationDate);
                }


            }
        });


        return view;
    }


    /*###                        editting user info                ###*/
    private void editUserInfo(final String sUserName, final String sPhonNo, final String sEmail, final String sDivision, final String sDistrict, final String sAlternatePhoneNo, final String sDonationDate) {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.EDIT_USER_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject serverResponse = new JSONObject(response);
                            String code = serverResponse.getString("code");
                            String msg = serverResponse.getString("message");

                            if(code.equals("success")){

                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                etUserName.setText("");
                                etEmail.setText("");
                                etPhoneNo.setText("");
                                etAlternatePhoneNo.setText("");
                                etDivision.setText("");
                                etDistrict.setText("");
                                etBloodGroup.setText("");


                            }else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(EditUserInfoFragment) 1: "+error);
                error.getStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String , String> editUserMap = new HashMap<>();

                editUserMap.put("user_name", sUserName);
                editUserMap.put("mobile_no", sPhonNo);
                editUserMap.put("mobile_no_2", sAlternatePhoneNo);
                editUserMap.put("division", sDivision);
                editUserMap.put("email", sEmail);
                editUserMap.put("district", sDistrict);
                editUserMap.put("firebase_id", userId);
                editUserMap.put("donation_date", sDonationDate);

                return editUserMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    /*###                          initializing components             ###*/
    private void init() {
        etUserName = view.findViewById(R.id.idUserName_editUserInfo);
        etPhoneNo = view.findViewById(R.id.idMobileNo_editUserInfo);
        etAlternatePhoneNo = view.findViewById(R.id.idAlternateMobileNo_editUserInfo);
        etEmail = view.findViewById(R.id.idEmail_editUserInfo);
        etBloodGroup = view.findViewById(R.id.idblood_group_editUserInfo);
        etDivision = view.findViewById(R.id.idDivision_editUserInfo);
        etDistrict = view.findViewById(R.id.idDistrict_editUserInfo);
        btEditInfo = view.findViewById(R.id.idEditBtn_editUserInfo);
        tvDonationDate = view.findViewById(R.id.idDonationDate_editUserInfo);
        tvDontKnow = view.findViewById(R.id.idDontKnow_editUserInfo);


        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();

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

                            etUserName.setText(userName);
                            etPhoneNo.setText(mobileNo);
                            etEmail.setText(email);
                            etDivision.setText(division);
                            etDistrict.setText(district);
                            etBloodGroup.setText(bloodGroup);
                            etAlternatePhoneNo.setText(mobileNo2);
                            tvDonationDate.setText(lastDonationDate);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(EditUserInfoFragment) 2: "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("firebase_id", userId);
                return map;
            }
        };



        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }




}
