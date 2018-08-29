package invenz.roy.blooddonationapp1.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.adapters.FactsAdapter;
import invenz.roy.blooddonationapp1.models.Fact;
import invenz.roy.blooddonationapp1.utils.Urls;

public class FactsActivity extends AppCompatActivity {

    private static final String TAG = "roy" ;
    private RecyclerView factsRecyclerView;
    private List<Fact> factList;
    private FactsAdapter factsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        factsRecyclerView = findViewById(R.id.idFactsRecView_FactsActivity);
        factsRecyclerView.setLayoutManager(new LinearLayoutManager(FactsActivity.this));

        factList = new ArrayList<>();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(JsonArrayRequest.Method.POST, Urls.GET_ALL_FACTS_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int size = response.length();

                        for (int i=0; i<size; i++){

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String ques = jsonObject.getString("ques");
                                String ans = jsonObject.getString("ans");

                                Fact fact = new Fact(id, ques, ans);
                                factList.add(fact);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        factsAdapter = new FactsAdapter(FactsActivity.this, factList);
                        factsRecyclerView.setAdapter(factsAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse(FactsActivity): "+error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(FactsActivity.this);
        requestQueue.add(jsonArrayRequest);


    }
}
