package invenz.roy.blooddonationapp1.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.activities.AddDonorActivity;
import invenz.roy.blooddonationapp1.activities.BloodBankActivity;
import invenz.roy.blooddonationapp1.activities.FactsActivity;
import invenz.roy.blooddonationapp1.activities.MyAccountActivity;
import invenz.roy.blooddonationapp1.activities.PostRequestActivity;
import invenz.roy.blooddonationapp1.activities.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private LinearLayout linearLayoutSearch, linearLayoutPostRequest, linearLayoutRequests, linearLayoutBloodBanks;
    private LinearLayout linearLayoutAddDonor, linearLayoutMyAccount, linearLayoutFacts, linearLayoutAboutApp;
    private View view;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);


        /*###                      initialize components                       ####*/
        initializeComponents();


        /*####         all events are here         ######*/
        allOnClicks();

        return view;
    }


    /*####              Initializing Components                           ####*/
    private void initializeComponents() {

        linearLayoutSearch = view.findViewById(R.id.idSearch_HOME);
        linearLayoutPostRequest = view.findViewById(R.id.idPostRequeast_HOME);
        linearLayoutRequests = view.findViewById(R.id.idRequests_HOME);
        linearLayoutBloodBanks = view.findViewById(R.id.idBloodBanks_HOME);
        linearLayoutAddDonor = view.findViewById(R.id.idAddDonor_HOME);
        linearLayoutMyAccount = view.findViewById(R.id.idMyAccount_HOME);
        linearLayoutFacts = view.findViewById(R.id.idFacts_HOME);
        linearLayoutAboutApp = view.findViewById(R.id.idAboutApp_HOME);

    }


    private void allOnClicks() {

        linearLayoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));

            }
        });



        linearLayoutPostRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostRequestActivity.class));
            }
        });




        linearLayoutBloodBanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getContext(), BloodBankActivity.class));

            }
        });

        linearLayoutAddDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddDonorActivity.class));

            }
        });


        linearLayoutMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyAccountActivity.class));
            }
        });


        linearLayoutFacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FactsActivity.class));

            }
        });

    }



}
