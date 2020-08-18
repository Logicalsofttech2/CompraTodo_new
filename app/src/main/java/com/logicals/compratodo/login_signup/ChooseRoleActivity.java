package com.logicals.compratodo.login_signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.logicals.compratodo.R;
import com.logicals.compratodo.customer.mainhomeactivity.UserHomeActivity;
import com.logicals.compratodo.preferences.SharedPrefrence;

public class ChooseRoleActivity extends AppCompatActivity {
    CardView buyer, seller;
    SharedPrefrence sharedPrefrence;
    Button btn_next;
    int chooseRole;
    String INTENT;
    boolean flag=false;
    ImageView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);
        buyer = findViewById(R.id.buyer);
        seller = findViewById(R.id.seller);
        sharedPrefrence= SharedPrefrence.getInstance(ChooseRoleActivity.this);
        btn_next=findViewById(R.id.btn_next);
        menu=findViewById(R.id.menu);






        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });







        if (getIntent()!=null){
             INTENT=getIntent().getStringExtra("intent");
           // Log.e("check intent",INTENT);

        }



        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseRole=sharedPrefrence.getchooseRole("role");
                if (flag==false){
                    Toast.makeText(ChooseRoleActivity.this, "Please choose your Role", Toast.LENGTH_SHORT).show();
                }else {

                    if (chooseRole==1){

                        if (INTENT.equals("login")){
                            startActivity(new Intent(ChooseRoleActivity.this, CustomerLoginActivity.class));
                            finish();
                        }else if (INTENT.equals("home")){
                            startActivity(new Intent(ChooseRoleActivity.this, UserHomeActivity.class));
                            finish();
                        }


                    }else {

                        if (INTENT.equals("login")){
                            startActivity(new Intent(ChooseRoleActivity.this, VendorLoginActivity.class));
                            finish();
                        }else if(INTENT.equals("home")){
                            startActivity(new Intent(ChooseRoleActivity.this, VendorLoginActivity.class));
                            finish();
                        }




                    }
                }
            }
        });








        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                sharedPrefrence.setchooseRole("role",2);
                seller.setCardBackgroundColor(Color.parseColor("#02B533"));
                seller.setCardElevation(5);
                buyer.setCardBackgroundColor(null);
            }
        });


        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             flag=true;
                sharedPrefrence.setchooseRole("role",1);
                buyer.setCardBackgroundColor(Color.parseColor("#02B533"));
                buyer.setCardElevation(5);
                seller.setCardBackgroundColor(null);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}