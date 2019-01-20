package thesis.agriproducts.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import thesis.agriproducts.R;
import thesis.agriproducts.model.entities.Deal;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    class InboxViewHolder extends RecyclerView.ViewHolder {

        TextView txtSender, txtProduct;
        ImageView imgInboxThumb;

        private InboxViewHolder(View itemView) {
            super(itemView);

            txtSender = itemView.findViewById(R.id.txtInboxSender);
            txtProduct = itemView.findViewById(R.id.txtInboxProduct);
            imgInboxThumb = itemView.findViewById(R.id.imgInboxThumb);
        }
    }

    private Context mCtx;
    private List<Deal> inboxList;

    public InboxAdapter(Context mCtx, List<Deal> inboxList) {
        this.mCtx = mCtx;
        this.inboxList = inboxList;
    }

    @NonNull
    @Override
    public InboxAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_inbox, null);
        return new InboxAdapter.InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.InboxViewHolder holder, int position) {
        Deal inbox = inboxList.get(position);

//        holder.txtSender.setText(inbox.getSenderName());
//        holder.txtProduct.setText(inbox.getProductName());

        holder.imgInboxThumb.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_home_black_24dp));
    }


    @Override
    public int getItemCount() {
        return inboxList.size();
    }

}