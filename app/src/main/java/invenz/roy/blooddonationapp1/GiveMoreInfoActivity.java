package invenz.roy.blooddonationapp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GiveMoreInfoActivity extends AppCompatActivity {

    private EditText etUserName, etPhoneNo;
    private Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_more_info);

        etUserName = findViewById(R.id.idUsername_GiveMoreInfoAct);
        etPhoneNo = findViewById(R.id.idMobileNo_GiveMoreInfoAct);
        btSubmit = findViewById(R.id.idSubmit_GiveMoreInfoAct);




    }

    public void submitOnClickListener(View view) {

        String sUserName = etUserName.getText().toString().trim();
        String sMobileNo = etPhoneNo.getText().toString().trim();

        if (sUserName.isEmpty() || sMobileNo.isEmpty()){
            etUserName.setError("Username please..");
            etPhoneNo.setError("Mobile no please..");
            return;

        }else {
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(GiveMoreInfoActivity.this, HomeActivity.class);
            intent.putExtra("mobile", sMobileNo);
            intent.putExtra("username", sUserName);
            startActivity(intent);

        }

    }
}
