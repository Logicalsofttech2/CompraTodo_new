package com.logicals.compratodo.customer.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.logicals.compratodo.customer.activity.PlaceorderActivity;
import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.activity.AddAddressActivity;
import com.logicals.compratodo.customer.model.AddressListModel;
import com.logicals.compratodo.https.HttpsRequest;
import com.logicals.compratodo.interfacess.Consts;
import com.logicals.compratodo.interfacess.Helper;
import com.logicals.compratodo.model.UserDTO;
import com.logicals.compratodo.preferences.SharedPrefrence;
import com.logicals.compratodo.preferences.Shared_Pref;
import com.logicals.compratodo.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.HeroViewHolder> {
    HashMap<String, String> param = new HashMap<>();
    Context mCtx;
    String delete_id;
    ArrayList<AddressListModel> heroList=new ArrayList<>();
    private RadioButton lastCheckedRB = null;
   // Session session;
     Dialog dialog;
     UserDTO userDTO;
     SharedPrefrence sharedPrefrence;
    String check_api;

    public AddressListAdapter(Context mCtx, ArrayList<AddressListModel> addressListModel,String check_api) {
        this.mCtx = mCtx;
        this.heroList = addressListModel;
        this.check_api=check_api;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_address_list, parent, false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, final int position) {
        AddressListModel hero = heroList.get(position);
        final String house_no=heroList.get(position).getHouseNo();
        final String area =heroList.get(position).getArea();
        final String pincode=heroList.get(position).getPinCode();
        final String city=heroList.get(position).getCity();
        final String State=heroList.get(position).getState();
        final String Landmark =heroList.get(position).getLandmark();
        String type=heroList.get(position).getType();
        String mobile= heroList.get(position).getMobile();
        //session=new Session(mCtx);
        sharedPrefrence= SharedPrefrence.getInstance(mCtx);
        userDTO=sharedPrefrence.getParentUser(Consts.USER_DTO);
        delete_id=heroList.get(position).getId();
        String USerAddressType = heroList.get(position).getType().substring(0, 1).toUpperCase() + heroList.get(position).getType().substring(1).toLowerCase();

          holder.type.setText(USerAddressType);

        holder.radio_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RadioButton checked_rb = (RadioButton) v;
                if(lastCheckedRB != null){
                    lastCheckedRB.setChecked(false);
                }
                lastCheckedRB = checked_rb;
                Shared_Pref.setAddresId(mCtx, house_no + "," + area + "," + Landmark + "," + city + "," + State + " " + pincode);

                mCtx.startActivity(new Intent(mCtx, PlaceorderActivity.class)
                        .putExtra(Consts.BILLING,check_api)
                        .putExtra(Consts.ADDRESS,house_no + "," + area + "," + Landmark + "," + city + "," + State + " " + pincode));

            }
        });



        holder.lin_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCtx.startActivity(new Intent(mCtx,
                        AddAddressActivity.class)
                        .putExtra("Type","Edit")
                        .putExtra("Data", hero)); }
        });



        holder.lin_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog = new Dialog(mCtx);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.logout_alert);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;
                    dialog.getWindow().setAttributes(lp);
                    Button ok_btn = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    Button cancel_btn = (Button) dialog.findViewById(R.id.dialogButtonCancel);


                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    TextView dynamic_msg = (TextView) dialog.findViewById(R.id.dynamic_msg);
                     dynamic_msg.setText("Do You Want To Delete This Address ?");

                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                          param.put(Consts.USER_ID,userDTO.getId());
                          param.put(Consts.ADDRESS_ID,heroList.get(position).getId());

                       delete_api(position, Consts.DELETEADDRESS);

                        }
                    });

                    dialog.show();

            }
        });


        holder.delivery_address.setText(house_no +","+area+","+city+","+Landmark+","+State+" "+pincode);





    }


    private void delete_api(final int position,String apiname) {

            ProjectUtils.showProgressDialog(mCtx,false,mCtx.getResources().getString(R.string.please_wait));
            new HttpsRequest(apiname,param,mCtx).imagePost("TAG", new Helper() {
                @Override
                public void backResponse(boolean flag, String msg, JSONObject response) {
                    ProjectUtils.pauseProgressDialog();
                    if(flag){
                        Toast.makeText(mCtx, "Your Address Has Been Deleted Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        removeAt(position);
                    }else {
                        Toast.makeText(mCtx, ""+msg, Toast.LENGTH_SHORT).show();

                        //Utils.alert_dialoge(mCtx,response.body().getMsg());

                    }
                }
            });
        }







    public void removeAt(int position) {
        heroList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, heroList.size());
    }





    @Override
    public int getItemCount() {
        return heroList.size();
    }

    class HeroViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView delivery_address,lin_edit,lin_delete,type;
        CardView linear_layout;
        RadioButton radio_btn;
        public HeroViewHolder(View itemView) {
            super(itemView);
            type=itemView.findViewById(R.id.type);
            radio_btn=itemView.findViewById(R.id.radio_btn);
            delivery_address = itemView.findViewById(R.id.delivery_address);
            lin_delete=itemView.findViewById(R.id.lin_delete);
            lin_edit=itemView.findViewById(R.id.lin_edit);
        }
    }
}



