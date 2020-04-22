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
import java.util.Map;

public class AddUtility extends AppCompatActivity implements View.OnClickListener{

    EditText editTextAmount;
    Button buttonAddUtility;
    Spinner dropdown;
    String vendor;
    //**************WILL BE SAVED IN CONFIG FILE*************
    String urlBase= "https://script.google.com/macros/s/AKfycbz2mchX1vFiNT-wIUYqjRFzbfXIRQyUWrqSs10xUb9mK-z6z5Fq/exec";
    String methodGET = "?action=getVendorList";
    String methodPOST = "?action=addUtility";

    //*******************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_utility);

        dropdown = (Spinner) findViewById(R.id.et_utility_vendors);
        editTextAmount = (EditText)findViewById(R.id.et_utility);
        getVendorList();
        buttonAddUtility = (Button)findViewById(R.id.btn_add_utility);
        buttonAddUtility.setOnClickListener(this);

    }

    private void getVendorList(){
        String url = urlBase + methodGET;

        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject getResponse) {
                parseVendorList(getResponse);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void parseVendorList(JSONObject jsonResponse){

        ArrayList < String> list = new ArrayList<>();
        //list.add("Select Options...");

        try {
            JSONArray jarray = jsonResponse.getJSONArray("items");
            JSONObject jo = null;
            for (int i = 0; i < jarray.length(); i++) {
                //System.out.println("LOOP " + i);
                jo = jarray.getJSONObject(i);
                String name = jo.getString("vendorList");
                list.add(name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("List of vendors: " + list);

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setPrompt("Select Options...");
        dropdown.setAdapter(adapter);
        //dropdown.setSelection(adapter.getCount());
        //adapter.notifyDataSetChanged();
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> selected, View v, int position, long id) {

                //On selecting spinner item
                vendor = selected.getItemAtPosition(position).toString().trim();

                //Temp: create toast
                Toast.makeText(getApplicationContext(),"This name is selected: " + vendor, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //DO NOTHING
            }
        });
    }

    private void addUtilityToSheet() {
        final ProgressDialog loading = ProgressDialog.show(this,"Adding Utility Amount","Please wait");
        final String utility = editTextAmount.getText().toString().trim();


        System.out.println("Starting adding user " + vendor); //QC checkpoint
        System.out.println("Starting update user payment = " + utility); //QC checkpoint

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBase,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String postResponse) {
                        loading.dismiss();
                        Toast.makeText(AddUtility.this,postResponse,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addUtility");
                parmas.put("vendorName",vendor);
                parmas.put("amount",utility);

                return parmas;
            }
        };

        int socketTimeOut = 50000;//

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonAddUtility){
            addUtilityToSheet();
        }
    }
}
