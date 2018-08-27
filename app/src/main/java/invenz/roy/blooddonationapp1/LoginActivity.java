package invenz.roy.blooddonationapp1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import invenz.roy.blooddonationapp1.utils.Constants;
import invenz.roy.blooddonationapp1.utils.Urls;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private EditText etPhoneNumber;
    private Button btContinue;
    private SignInButton btSignInGmail;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isExists;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int isFullyRegistered = sharedPreferences.getInt(Constants.FULLY_REGISTERED_KEY, 0);


        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!= null && isFullyRegistered == 2){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }else  if(currentUser!= null && isFullyRegistered == 1){
            startActivity(new Intent(LoginActivity.this, GiveMoreInfoActivity.class));
            finish();
        }


        /*         when Continue button is clicked    */
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPhoneNo = etPhoneNumber.getText().toString().trim();

                if (sPhoneNo.isEmpty()  || sPhoneNo.length() < 11){
                    etPhoneNumber.setError("Enter a valid mobile");
                    etPhoneNumber.requestFocus();
                    return;

                }else {
                    Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                    intent.putExtra("mobile", sPhoneNo);
                    startActivity(intent);

                }
            }
        });


        /*#####                       signIn With Gmail Button                             ########*/
        btSignInGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

                Toast.makeText(LoginActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

            }
        });



    }

    /*##               initialising components       ###*/
    private void init() {

        etPhoneNumber = findViewById(R.id.editTextMobile);
        btContinue = findViewById(R.id.buttonContinue_loginAct);
        mAuth = FirebaseAuth.getInstance();
        btSignInGmail = findViewById(R.id.idSigninGmail);


        /*Configure Google Sign In*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }





    /*####                             onActivityResult  signIn With Gmail                            #####*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*####       Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);               ######*/
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                /*########         Google Sign In was successful, authenticate with Firebase        #######*/
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);


            } catch (ApiException e) {
                /*               Google Sign In failed, update UI appropriately              */
                Log.w("roy", "Google sign in failed", e);

            }
        }
    }




    /*###                             firebaseAuthWithGoogle                        ###*/
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("roy", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            /*            Sign in success, update UI with the signed-in user's information              */

                            editor.putString(Constants.LOGGED_IN_WITH_KEY, "email");
                            editor.commit();

                            Log.d("roy", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String firebaseId = user.getUid();


                            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.IS_FIREBASE_ID_EXISTS_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                String code = jsonObject.getString("code");


                                                if (code.equals("exists")){
                                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                    //intent.putExtra("phone_no", "no");
                                                    startActivity(intent);

                                                    finish();
                                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                                                }else {
                                                    Intent intent = new Intent(LoginActivity.this, GiveMoreInfoActivity.class);
                                                    //intent.putExtra("phone_no", "no");
                                                    startActivity(intent);

                                                    finish();
                                                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                                                }
                                                Toast.makeText(LoginActivity.this, ""+isExists, Toast.LENGTH_SHORT).show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("roy", "getParams(LoginActivity): "+error);
                                    error.getStackTrace();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> existMap = new HashMap<>();
                                    existMap.put("firebase_id", firebaseId);
                                    return existMap;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);


                        } else {
                            /*                     If sign in fails, display a message to the user.                     */
                            Log.w("roy", "signInWithCredential:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }




    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            startActivity(new Intent(LoginActivity.this, GiveMoreInfoActivity.class));
            finish();
        }
        //updateUI(currentUser);
    }







}
