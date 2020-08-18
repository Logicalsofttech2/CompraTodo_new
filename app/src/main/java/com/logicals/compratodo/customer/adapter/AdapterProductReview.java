package com.logicals.compratodo.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.model.ProductReviewDTO;
import com.logicals.compratodo.databinding.AdapterProductReviewBinding;
import java.util.ArrayList;

    public class AdapterProductReview extends RecyclerView.Adapter<AdapterProductReview.MyViewHolder> {
        LayoutInflater inflater;
        AdapterProductReviewBinding binding;
        Context mContext;
        ArrayList<ProductReviewDTO> productDTOS = new ArrayList<>();
        int limit;


        public AdapterProductReview(Context mContext, ArrayList<ProductReviewDTO> productDTOS,int limit) {
            this.mContext = mContext;
            this.productDTOS = productDTOS;
            this.limit=limit;
        }


        @NonNull
        @Override
        public AdapterProductReview.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext()); }
            binding = DataBindingUtil.inflate(inflater, R.layout.adapter_product_review, parent, false);
            return new AdapterProductReview.MyViewHolder(binding);

        }

        @Override
        public void onBindViewHolder(@NonNull AdapterProductReview.MyViewHolder holder, int position) {

            ProductReviewDTO productReviewDTO=productDTOS.get(position);
            binding.setProductreview(productReviewDTO);
            binding.RatingBar.setRating(Float.parseFloat(productReviewDTO.getRating()));
            binding.date.setText(productReviewDTO.getCreated_date());

         //   String input = "2012/01/20 12:05:10.321";
         //   String output = input.substring(0, 10);  // Output : 2012/01/20




        }

        @Override
        public int getItemCount() {

          /*  if (limit==0){
                return productDTOS.size();
            }else {
                return limit;
            }
*/
            return productDTOS.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            AdapterProductReviewBinding binding;

            public MyViewHolder(@NonNull AdapterProductReviewBinding view) {
                super(view.getRoot());
                this.binding = view;
            }
        }



    }

