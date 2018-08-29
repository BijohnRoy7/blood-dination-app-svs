package invenz.roy.blooddonationapp1.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import invenz.roy.blooddonationapp1.Manifest;
import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.models.BloodBank;
import invenz.roy.blooddonationapp1.utils.Urls;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.MyViewHolder> {

    private Context context;
    private List<BloodBank> bloodBankList;

    public BloodBankAdapter(Context context, List<BloodBank> bloodBankList) {
        this.context = context;
        this.bloodBankList = bloodBankList;
    }


    @NonNull
    @Override
    public BloodBankAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_blood_bank_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodBankAdapter.MyViewHolder holder, int position) {

        final BloodBank bloodBank = bloodBankList.get(position);

        holder.tvName.setText(bloodBank.getName());
        holder.tvAddress.setText("Address: "+bloodBank.getAddress());
        holder.tvOpenAt.setText("Open: "+bloodBank.getOpenTime());
        holder.tvPhone.setText("Phone No:"+bloodBank.getPhoneNo());


        /*####                   call when click on an item               #####*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Calling Confirmation");
                builder.setMessage("Do you want to call now?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+bloodBank.getPhoneNo()));


                        if (intent.resolveActivity(context.getPackageManager())!=null){
                            context.startActivity(intent);
                        }


                    }
                });

                builder.setNegativeButton("No", null);
                builder.show();

            }
        });
        /*                                                                      */
    }


    @Override
    public int getItemCount() {
        return bloodBankList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAddress, tvPhone, tvOpenAt;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.idName_singleBloodBank);
            tvAddress = itemView.findViewById(R.id.idAddress_singleBloodBank);
            tvOpenAt = itemView.findViewById(R.id.idOpenAt_singleBloodBank);
            tvPhone = itemView.findViewById(R.id.idPhone_singleBloodBank);

        }
    }
}
