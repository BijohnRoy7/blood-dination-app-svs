package invenz.roy.blooddonationapp1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import invenz.roy.blooddonationapp1.R;
import invenz.roy.blooddonationapp1.models.BloodBank;
import invenz.roy.blooddonationapp1.models.SearchDonor;

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorAdapter.MyViewHolder> {

    private Context context;
    private List<SearchDonor> donorList;

    public SearchDonorAdapter(Context context, List<SearchDonor> donorList) {
        this.context = context;
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public SearchDonorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_donor, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchDonorAdapter.MyViewHolder holder, int position) {

        SearchDonor donor = donorList.get(position);

        holder.tvName.setText("Name: "+donor.getName());
        holder.tvDivision.setText("Division: "+donor.getDivition());
        holder.tvDistrict.setText("District: "+donor.getDistrict());
        holder.tvBloodGroup.setText("Blood Group: "+donor.getBloodGroup());
        holder.tvAvailable.setText("Available: "+donor.getAvailable());

    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDivision, tvDistrict, tvBloodGroup, tvAvailable;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.idName_singleDonor);
            tvDivision = itemView.findViewById(R.id.idDivision_singleDonor);
            tvDistrict = itemView.findViewById(R.id.idDistrict_singleDonor);
            tvBloodGroup = itemView.findViewById(R.id.idBloodGroup_singleDonor);
            tvAvailable = itemView.findViewById(R.id.idAvailable_singleDonor);

        }
    }
}
