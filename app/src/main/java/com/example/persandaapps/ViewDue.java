package com.example.persandaapps;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ViewDue extends AppCompatActivity implements View.OnClickListener{

    Button buttonSearchDue;
    Spinner dropdownNameTenant;
    TextView dueNameTxt, dueDateTxt, dueAmountTxt;
    ProgressDialog loading;
    String nameTenant;
    //**************WILL BE SAVED IN CONFIG FILE*************
    String urlBase= "https://script.google.com/macros/s/AKfycbz2mchX1vFiNT-wIUYqjRFzbfXIRQyUWrqSs10xUb9mK-z6z5Fq/exec";
    //*******************************************************

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_due);
        dropdownNameTenant = (Spinner) findViewById(R.id.et_due_tenant_name);
        getInfo();
        buttonSearchDue = (Button)findViewById(R.id.btn_search_due);
        buttonSearchDue.setOnClickListener(this);
        dueNameTxt = (TextView)findViewById(R.id.due_tenant_name);
        dueDateTxt = (TextView)findViewById(R.id.due_last_payment);
        dueAmountTxt = (TextView)findViewById(R.id.due_amount);
    }

    private void getInfo(){

        String methodAPI = "?action=getNameList";
        String url = urlBase + methodAPI;

        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject getResponse) {
                parseNameList(getResponse);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        Toast.makeText(ViewDue.this,"[ERROR] Unable to Connect to Server",Toast.LENGTH_LONG).show();
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

    private void parseNameList(JSONObject jsonResponse){

        ArrayList < String> list = new ArrayList<>();
        //list.add("Select Options...");

        try {
            JSONArray jarray = jsonResponse.getJSONArray("items");
            JSONObject jo = null;
            for (int i = 0; i < jarray.length(); i++) {
                //System.out.println("LOOP " + i);
                jo = jarray.getJSONObject(i);
                String name = jo.getString("tenantName");
                list.add(name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("List of users: "+list);

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownNameTenant.setPrompt("Select Options...");
        dropdownNameTenant.setAdapter(adapter);
        //dropdown.setSelection(adapter.getCount());
        //adapter.notifyDataSetChanged();
        dropdownNameTenant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> selected, View v, int position, long id) {

                //On selecting spinner item
                nameTenant = selected.getItemAtPosition(position).toString().trim();
                System.out.println("Retrieving info (nameTenant) in getInfo: " + nameTenant);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Do Nothing***
            }
        });
    }

    private void queryItemsJson(){
        loading =  ProgressDialog.show(this,"Loading","Please Wait While System Retrieving Data...",false,true);
        System.out.println("Retrieving info (nameTenant) in queryItemsJson: " + nameTenant);
        String methodApiGetDeposit = "?action=getDue&tenantName=" + nameTenant;
        String urlGetDeposit = urlBase + methodApiGetDeposit;
        System.out.println("URL API getDeposit: " + urlGetDeposit);

        //GET requests
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, urlGetDeposit, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.toString().contains("noData")){
                    loading.dismiss();
                    Toast.makeText(ViewDue.this,"No Tenant Name Found" + nameTenant,Toast.LENGTH_LONG).show();
                    System.out.println("[RESPONSE]: DATA IS NULL");
                } else {
                    try {
                        parseJson(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        Toast.makeText(ViewDue.this,"[ERROR] Unable to Connect to Server",Toast.LENGTH_LONG).show();
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

    private void parseJson(JSONObject response) throws JSONException {

        System.out.println("[PROCESS]: Starting Json Parsing Method");
        JSONArray jarray = response.getJSONArray("items");
        System.out.println(jarray);
        JSONObject jo = null;
            jo = jarray.getJSONObject(0);
            String depositNameString = jo.getString("paymentTenant");
            String depositDateString = jo.getString("paymentDate");
            String depositAmountString = jo.getString("paymentAmount");

            System.out.println(depositNameString);
            System.out.println(depositAmountString);
            System.out.println(depositDateString);

            dueNameTxt.setText(depositNameString);
            dueDateTxt.setText(depositDateString);
            dueAmountTxt.setText(depositAmountString);
            loading.dismiss();

    }

    @Override
    public void onClick(View v) {
        if(v==buttonSearchDue){
            System.out.println("Click Search Deposit Button");
            dueNameTxt.setText("");
            dueAmountTxt.setText("");
            dueDateTxt.setText("");
            queryItemsJson();
        }
    }
}
