    package invenz.roy.blooddonationapp1.activities;

    import android.app.DatePickerDialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.support.v4.app.DialogFragment;
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

    import java.text.DateFormat;
    import java.util.Calendar;
    import java.util.Date;

    import invenz.roy.blooddonationapp1.R;
    import invenz.roy.blooddonationapp1.utils.Constants;

    public class GiveMoreInfoActivity extends AppCompatActivity {

        private EditText etUserName, etPhoneNo, etEmailAddress;
        private TextView tvLastDonationDate, tvDateDontKnow;
        private Button btSubmit;
        private Spinner spDivision, spDistrict, spBloodGroup;
        private String sPhoneNo, sEmailAddrress, sUserName, sDivision, sDistrict, sBloodGroup, mPhone;
        private ArrayAdapter<String> divisionAdapter, districtAdapter, bloodGroupAdapter ;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;
        private String loggedInWith;

        private int myDate, myMonth, myYear;

        private DatePickerDialog datePickerDialog;
        private String donationDate, sDonationDate;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_give_more_info);

            init();

            sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);

            editor = sharedPreferences.edit();
            editor.putInt(Constants.FULLY_REGISTERED_KEY, 1);
            editor.commit();

            loggedInWith = sharedPreferences.getString(Constants.LOGGED_IN_WITH_KEY, null);

            if (loggedInWith.equals("email")){
                etEmailAddress.setVisibility(View.GONE); /* logeed in with email, so hiding email editText */
                sPhoneNo = "no";

            }else {
                etPhoneNo.setVisibility(View.GONE); /* logeed in with phone, so hiding phone editText */
                sPhoneNo = sharedPreferences.getString(Constants.PHONE_NO_KEY, "");

            }


            /*      setting values for spinners     */
            String[] divisions = {"Barisal", "Chittagong", "Dhaka", "Khulna", "Mymensingh", "Rajshahi", "Rangpur", "Sylhet"};
            String[] districts = {"bandarban", "Chittagong", "Feni"};
            String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

            divisionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, divisions);
            districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, districts);
            bloodGroupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bloodGroups);

            spDivision.setAdapter(divisionAdapter);
            spDistrict.setAdapter(districtAdapter);
            spBloodGroup.setAdapter(bloodGroupAdapter);
            /*                                          */



            /*###                       Click text to show DatePickerDialog                       ###*/
            tvLastDonationDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar calendar = Calendar.getInstance();
                    final int date = calendar.get(Calendar.DATE);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);


                    datePickerDialog = new DatePickerDialog(GiveMoreInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            Toast.makeText(GiveMoreInfoActivity.this, dayOfMonth+"-"+month+"-"+year, Toast.LENGTH_SHORT).show();

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
                    tvLastDonationDate.setText("Don\'t know");
                }
            });



        }





        /*       initialising widgets        */
        private void init() {

            etUserName = findViewById(R.id.idUsername_GiveMoreInfoAct);
            etPhoneNo = findViewById(R.id.idMobileNo_GiveMoreInfoAct);
            etEmailAddress = findViewById(R.id.idEmail_GiveMoreInfoAct);
            btSubmit = findViewById(R.id.idSubmit_GiveMoreInfoAct);
            spDivision = findViewById(R.id.idDivision_GiveMoreInfoAct);
            spDistrict = findViewById(R.id.idDistrict_GiveMoreInfoAct);
            spBloodGroup = findViewById(R.id.idBloodGroup_GiveMoreInfoAct);
            tvLastDonationDate = findViewById(R.id.idLastDonationDate_GiveMoreInfoAct);
            tvDateDontKnow = findViewById(R.id.idDateDontKnow_GiveMoreInfoAct);

        }



        /*             when submit button is clicked                  */
        public void submitOnClickListener(View view) {

            sUserName = etUserName.getText().toString().trim();
            sDivision = spDivision.getSelectedItem().toString();
            sDistrict = spDistrict.getSelectedItem().toString();
            sBloodGroup = spBloodGroup.getSelectedItem().toString();

            if (donationDate.equals("")){
                sDonationDate = "Don\'t know";
            }
            sDonationDate = donationDate;


            if (sPhoneNo.equals("no")){
                sPhoneNo = etPhoneNo.getText().toString().trim();
                if (sPhoneNo.isEmpty()){
                    etPhoneNo.setError("Contact number please...");
                }

            }else {
                //sPhoneNo = getIntent().getStringExtra("mobile");
                sEmailAddrress = etEmailAddress.getText().toString().trim();
                if (sEmailAddrress.isEmpty()){
                    etEmailAddress.setError("Email address please");
                }
            }


            if (sUserName.isEmpty()  ){
                etUserName.setError("Username please..");
                return;

            }else if(sDivision.isEmpty()){
                Toast.makeText(this, "Select division please", Toast.LENGTH_SHORT).show();
            }
            if(sDistrict.isEmpty()){
                Toast.makeText(this, "Select district please", Toast.LENGTH_SHORT).show();
            }
            if(sBloodGroup.isEmpty()){
                Toast.makeText(this, "Select blood group please", Toast.LENGTH_SHORT).show();
            }

                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(GiveMoreInfoActivity.this, HomeActivity.class);
                intent.putExtra("username", sUserName);
                intent.putExtra("division", sDivision);
                intent.putExtra("district", sDistrict);
                intent.putExtra("blood_group", sBloodGroup);
                intent.putExtra("donation_date", sDonationDate);


                if (loggedInWith.equals("email")) {
                    intent.putExtra("phone_no", sPhoneNo);
                    intent.putExtra("email", "email");


                    Log.d("roy", "submitOnClickListener1: "+sUserName+", "+sDivision+", "+sDistrict+", "+sBloodGroup+", "+sEmailAddrress);

                }else {
                    intent.putExtra("email", sEmailAddrress);
                    intent.putExtra("phone_no", sPhoneNo);

                    Log.d("roy", "submitOnClickListener: "+sUserName+", "+sDivision+", "+sDistrict+", "+sBloodGroup+", "+sPhoneNo);

                }

                startActivity(intent);
                }



    }

