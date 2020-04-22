package com.example.persandaapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.IDN;
import java.util.ArrayList;
import java.util.HashMap;


public  class SearchPayment extends AppCompatActivity implements View.OnClickListener {


    Button buttonSearchPayment;
    Spinner dropdownMonth;
    Spinner dropdownYear;
    static String nameMonth;
    static String nameYear;
    String url= "https://script.google.com/macros/s/AKfycbz2mchX1vFiNT-wIUYqjRFzbfXIRQyUWrqSs10xUb9mK-z6z5Fq/exec";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_payment_set);

        dropdownMonth = (Spinner) findViewById(R.id.et_month);
        dropdownYear = (Spinner) findViewById(R.id.et_year);
        getMonth();
        getYear();
        buttonSearchPayment = (Button)findViewById(R.id.btn_search_payment);
        buttonSearchPayment.setOnClickListener(this);

    }

    private void getMonth(){
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url+"?action=getMonthList", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject getResponse) {
                parseMonthList(getResponse);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        Toast.makeText(SearchPayment.this,"[ERROR] Unable to Connect to Server",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        int socketTimeOut = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void parseMonthList(JSONObject jsonResponse){

        ArrayList < String> list = new ArrayList<>();
        //list.add("Select Options...");

        try {
            JSONArray jarray = jsonResponse.getJSONArray("items");
            JSONObject jo = null;
            for (int i = 0; i < jarray.length(); i++) {
                //System.out.println("LOOP " + i);
                jo = jarray.getJSONObject(i);
                String name = jo.getString("monthList");
                list.add(name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println(list);

        final ArrayAdapter adapterMonth = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownMonth.setPrompt("Select Options...");
        dropdownMonth.setAdapter(adapterMonth);
        //dropdownMonth.setSelection(adapter.getCount());
        dropdownMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> selected, View v, int position, long id) {

                //On selecting spinner item
                nameMonth = selected.getItemAtPosition(position).toString().trim();
                //Temp: create toast
                Toast.makeText(getApplicationContext(),"This month is selected: "+nameMonth, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Nothing happened
            }
        });
    }

    private void getYear(){
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url+"?action=getYearList", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject getResponse) {
                parseYearList(getResponse);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        Toast.makeText(SearchPayment.this,"[ERROR] Unable to Connect to Server",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        int socketTimeOut = 5000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void parseYearList(JSONObject jsonResponse){

        ArrayList < String> list = new ArrayList<>();
        //list.add("Select Options...");

        try {
            JSONArray jarray = jsonResponse.getJSONArray("items");
            JSONObject jo = null;
            for (int i = 0; i < jarray.length(); i++) {
                //System.out.println("LOOP " + i);
                jo = jarray.getJSONObject(i);
                String name = jo.getString("yearList");
                list.add(name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println(list);

        final ArrayAdapter adapterYear = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownYear.setPrompt("Select Options...");
        dropdownYear.setAdapter(adapterYear);
        //dropdownYear.setSelection(adapter.getCount());
        dropdownYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> selected, View v, int position, long id) {

                //On selecting spinner item
                nameYear = selected.getItemAtPosition(position).toString().trim();
                //Temp: create toast
                Toast.makeText(getApplicationContext(),"This year is selected: "+nameYear, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Nothing happened
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v==buttonSearchPayment){
            System.out.println("Click query payment");
            Intent intent = new Intent(getApplicationContext(),QueryPayment.class);
            startActivity(intent);
        }
    }
}
