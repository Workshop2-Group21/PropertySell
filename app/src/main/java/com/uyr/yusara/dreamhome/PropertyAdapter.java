package com.uyr.yusara.dreamhome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uyr.yusara.dreamhome.Modal.Product;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Product> productList;

    public PropertyAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.layout_property, parent, false)
        );
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.textViewName.setText(product.getName());
        holder.textViewBrand.setText(product.getBrand());
        holder.textViewDesc.setText(product.getDescription());
        holder.textViewPrice.setText("INR " + product.getPrice());
        holder.textViewQty.setText("Available Units: " + product.getQty());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName, textViewBrand, textViewDesc, textViewPrice, textViewQty;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textview_name);
            textViewBrand = itemView.findViewById(R.id.textview_brand);
            textViewDesc = itemView.findViewById(R.id.textview_desc);
            textViewPrice = itemView.findViewById(R.id.textview_price);
            textViewQty = itemView.findViewById(R.id.textview_quantity);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
/*            Product product = productList.get(getAdapterPosition());
            Intent intent = new Intent(mCtx, UpdateProductActivity.class);
            intent.putExtra("product", product);
            mCtx.startActivity(intent);*/
        }
    }
}
