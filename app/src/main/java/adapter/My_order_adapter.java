package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.getpills.R;
import model.My_order_model;


public class My_order_adapter extends RecyclerView.Adapter<My_order_adapter.MyViewHolder> {

    private List<My_order_model> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, status, date, time, price, items;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_my_order_number);
            status = (TextView) view.findViewById(R.id.tv_my_order_status);
            date = (TextView) view.findViewById(R.id.tv_my_order_date);
            time = (TextView) view.findViewById(R.id.tv_my_order_time);
            price = (TextView) view.findViewById(R.id.tv_my_order_price);
            items = (TextView) view.findViewById(R.id.tv_my_order_items);

        }

    }

    public My_order_adapter(List<My_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_order, parent, false);

        context = parent.getContext();

        return new My_order_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(My_order_adapter.MyViewHolder holder, int position) {
        My_order_model mList = modelList.get(position);

        holder.title.setText(mList.getSale_id());

        if(mList.getStatus().equals("0")) {
            holder.status.setText(context.getResources().getString(R.string.pending));
        }else if(mList.getStatus().equals("1")){
            holder.status.setText(context.getResources().getString(R.string.confirm));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_1));
        }else if(mList.getStatus().equals("2")){
            holder.status.setText(context.getResources().getString(R.string.delivered));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_2));
        }else if(mList.getStatus().equals("3")){
            holder.status.setText(context.getResources().getString(R.string.cancel));
            holder.status.setTextColor(context.getResources().getColor(R.color.color_3));
        }

        holder.date.setText(mList.getOn_date());
        holder.time.setText(mList.getPayment_type());
        holder.price.setText(mList.getTotal_amount());
        holder.items.setText(mList.getTotal_items());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}