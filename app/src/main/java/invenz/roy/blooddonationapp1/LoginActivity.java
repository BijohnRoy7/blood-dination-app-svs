package invenz.roy.blooddonationapp1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import invenz.roy.blooddonationapp1.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private EditText etPhoneNumber, etUserName ;
    private Button btContinue;
    private SignInButton btSignInGmail;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }


        /*         when Continue button is clicked    */
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPhoneNo = etPhoneNumber.getText().toString().trim();
                String sUserName = etUserName.getText().toString().trim();

                if (sPhoneNo.isEmpty()  || sPhoneNo.length() < 11){
                    etPhoneNumber.setError("Enter a valid mobile");
                    etPhoneNumber.requestFocus();
                    return;

                }else if(sUserName.isEmpty()){
                    etUserName.setError("Enter UserName");
                    etUserName.requestFocus();
                    return;

                }else {
                    Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                    intent.putExtra("mobile", sPhoneNo);
                    intent.putExtra("username", sUserName);
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
        etUserName = findViewById(R.id.editTextUserName);
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
                            Log.d("roy", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(LoginActivity.this, GiveMoreInfoActivity.class));
                            finish();
                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

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
