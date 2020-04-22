package com.example.persandaapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonViewAddPayment, buttonViewListPayment, buttonSearchPayment, buttonAddUtility,
    buttonAddRent, buttonAddDeposit, buttonViewDeposit, buttonViewRent, buttonViewUtil, buttonViewDue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonViewAddPayment = findViewById(R.id.btn_view_add_payment);
        buttonViewAddPayment.setOnClickListener(this);
        buttonViewListPayment = findViewById(R.id.btn_view_list_payment);
        buttonViewListPayment.setOnClickListener(this);
        buttonSearchPayment = findViewById(R.id.btn_query_payment);
        buttonSearchPayment.setOnClickListener(this);
        buttonAddUtility = findViewById(R.id.btn_add_utility);
        buttonAddUtility.setOnClickListener(this);
        buttonAddRent = findViewById(R.id.btn_add_rent);
        buttonAddRent.setOnClickListener(this);
        buttonAddDeposit = findViewById(R.id.btn_add_deposit);
        buttonAddDeposit.setOnClickListener(this);
        buttonViewDeposit = findViewById(R.id.btn_view_deposit);
        buttonViewDeposit.setOnClickListener(this);
        buttonViewRent = findViewById(R.id.btn_view_rent);
        buttonViewRent.setOnClickListener(this);
        buttonViewUtil = findViewById(R.id.btn_view_utility);
        buttonViewUtil.setOnClickListener(this);
        buttonViewDue = findViewById(R.id.btn_view_due);
        buttonViewDue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v==buttonViewAddPayment){

            System.out.println("Click AddItem");
            Intent intent = new Intent(getApplicationContext(),AddPayment.class);
            startActivity(intent);
        }
        if(v==buttonViewListPayment){

            System.out.println("Click ListPayment");
            Intent intent = new Intent(getApplicationContext(),ViewPayment.class);
            startActivity(intent);
        }
        if(v==buttonSearchPayment){

            System.out.println("Click ListPayment");
            Intent intent = new Intent(getApplicationContext(),SearchPayment.class);
            startActivity(intent);
        }
        if(v==buttonAddUtility){

            System.out.println("Click AddUtility");
            Intent intent = new Intent(getApplicationContext(),AddUtility.class);
            startActivity(intent);
        }
        if(v==buttonAddRent){

            System.out.println("Click AddRent");
            Intent intent = new Intent(getApplicationContext(),AddRent.class);
            startActivity(intent);
        }
        if(v==buttonAddDeposit){

            System.out.println("Click AddDeposit");
            Intent intent = new Intent(getApplicationContext(),AddDeposit.class);
            startActivity(intent);
        }
        if(v==buttonViewDeposit){

            System.out.println("Click AddDeposit");
            Intent intent = new Intent(getApplicationContext(),ViewDeposit.class);
            startActivity(intent);
        }
        if(v==buttonViewRent){
            System.out.println("Click AddDeposit");
            Intent intent = new Intent(getApplicationContext(),ViewRent.class);
            startActivity(intent);
        }
        if(v==buttonViewUtil){
            System.out.println("Click View Utility");
            Intent intent = new Intent(getApplicationContext(),ViewUtility.class);
            startActivity(intent);
        }
        if(v==buttonViewDue){
            System.out.println("Click View Due");
            Intent intent = new Intent(getApplicationContext(),ViewDue.class);
            startActivity(intent);
        }
    }
}
