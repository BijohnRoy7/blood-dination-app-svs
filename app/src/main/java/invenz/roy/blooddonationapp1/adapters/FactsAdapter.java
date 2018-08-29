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
import invenz.roy.blooddonationapp1.models.Fact;

public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.MyViewHolder> {

    private Context context;
    private List<Fact> factList;


    public FactsAdapter(Context context, List<Fact> factList) {
        this.context = context;
        this.factList = factList;
    }

    @NonNull
    @Override
    public FactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_fact, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FactsAdapter.MyViewHolder holder, int position) {

        Fact fact = factList.get(position);

        holder.tvQues.setText(fact.getQues());
        holder.tvAns.setText("- "+fact.getAns());
    }

    @Override
    public int getItemCount() {
        return factList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvQues, tvAns;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvQues = itemView.findViewById(R.id.idQues_singleFact);
            tvAns = itemView.findViewById(R.id.idAns_singleFact);

        }
    }
}
