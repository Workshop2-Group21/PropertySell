package com.uyr.yusara.dreamhome.WishList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.uyr.yusara.dreamhome.Agent.AllPostActivityAgent;
import com.uyr.yusara.dreamhome.AllPostActivity;
import com.uyr.yusara.dreamhome.Modal.Wishlist;
import com.uyr.yusara.dreamhome.R;

public class WishlistAdapter extends FirestoreRecyclerAdapter<Wishlist, WishlistAdapter.WishlistHolder> {

    private OnItemClickListener listener;

    public WishlistAdapter(@NonNull FirestoreRecyclerOptions<Wishlist> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WishlistHolder holder, int position, @NonNull Wishlist model) {

        holder.textView_address.setText(model.getAddress());
        holder.textView_price.setText(model.getPrice());
        holder.textView_titletype.setText(model.getPropertytype());
        Picasso.get().load(model.getPostImage()).into(holder.productimage);

    }

    @NonNull
    @Override
    public WishlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wishlist,
                parent, false);
        return new WishlistHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class WishlistHolder extends RecyclerView.ViewHolder{
        TextView textView_address;
        TextView textView_price;
        TextView textView_titletype;
        ImageView productimage;

        public WishlistHolder(View itemView){
            super(itemView);

            productimage = itemView.findViewById(R.id.post_product_image);
            textView_address = itemView.findViewById(R.id.wishlist_address);
            textView_price = itemView.findViewById(R.id.wishlist_price);
            textView_titletype = itemView.findViewById(R.id.wishlist_title_type);
            productimage = itemView.findViewById(R.id.post_product_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);


                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
