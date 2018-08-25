package invenz.roy.blooddonationapp1.notification;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import invenz.roy.blooddonationapp1.utils.Constants;
import invenz.roy.blooddonationapp1.utils.Urls;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "roy" ;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        /*          getting user id      */
        mAuth = mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();

        /*       storing token into SharedPreferences    */
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.TOKEN_KEY, refreshedToken);
        editor.commit();

        sendRegistrationToServer(refreshedToken);
    }

    /*####                  sending token to the server                   ####*/
    private void sendRegistrationToServer(final String refreshedToken) {

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, Urls.STORE_TOKEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("message");
                            String code = jsonObject.getString("code");
                            Log.d(TAG, "onResponse(MyFirebaseInstanceIdService): "+msg);
                            Toast.makeText(MyFirebaseInstanceIdService.this, msg, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse(MyFirebaseInstanceIdService): "+error );
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String ,String> tokenMap = new HashMap<>();
                tokenMap.put("firebase_id", userId);
                tokenMap.put("token", refreshedToken);
                return tokenMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}
