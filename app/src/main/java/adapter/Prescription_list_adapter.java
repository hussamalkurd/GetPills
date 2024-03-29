package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viethoa.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

import com.getpills.R;
import model.Medical_category_model;



public class Prescription_list_adapter extends RecyclerView.Adapter<Prescription_list_adapter.MyViewHolder>
        implements RecyclerViewFastScroller.BubbleTextGetter {

    private List<Medical_category_model> modelList;
    private List<Medical_category_model> mFilteredList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_prescription_title);
            count = (TextView) view.findViewById(R.id.tv_prescription_count);
        }
    }

    public Prescription_list_adapter(List<Medical_category_model> modelList) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
    }

    @Override
    public Prescription_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_prescription, parent, false);

        context = parent.getContext();

        return new Prescription_list_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Prescription_list_adapter.MyViewHolder holder, int position) {
        Medical_category_model mList = modelList.get(position);

        holder.title.setText(mList.getTitle());
        holder.count.setText("( " + mList.getPCount() + " )");
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public String getTextToShowInBubble(int position) {
        if (position < 0 || position >= modelList.size())
            return null;

        String name = modelList.get(position).getTitle();
        if (name == null || name.length() < 1)
            return null;

        return modelList.get(position).getTitle().substring(0, 1);
    }

    public void filter(List<Medical_category_model> models, String query) {

        query = query.toLowerCase();

        final ArrayList<Medical_category_model> filteredModelList = new ArrayList<>();
        for (Medical_category_model model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query) || model.getPCount().toLowerCase().contains(query)) {
                filteredModelList.add(model);
            }
        }

        this.mFilteredList = filteredModelList;
        this.modelList = filteredModelList;
        notifyDataSetChanged();
    }


}
