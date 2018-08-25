package invenz.roy.blooddonationapp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber, etUserName ;
    private Button btContinue;
    private FirebaseAuth mAuth;


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
    }

    /*##               initialising components       ###*/
    private void init() {

        etPhoneNumber = findViewById(R.id.editTextMobile);
        btContinue = findViewById(R.id.buttonContinue_loginAct);
        mAuth = FirebaseAuth.getInstance();
        etUserName = findViewById(R.id.editTextUserName);
    }

}
