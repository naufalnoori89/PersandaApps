package com.example.persandaapps;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewPayment extends AppCompatActivity {

    ListView listView;
    ListAdapter adapter;
    ProgressDialog loading;
    String url= "https://script.google.com/macros/s/AKfycbz2mchX1vFiNT-wIUYqjRFzbfXIRQyUWrqSs10xUb9mK-z6z5Fq/exec";
    String action = "?action=getPayment";
    String urlFull = url+action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_payment);
        listView = (ListView) findViewById(R.id.list_payment);
        getItems();
        //queryItems();
    }

    private void getItems(){
        loading =  ProgressDialog.show(this,"Loading","Please Wait While System Retrieving Data...",false,true);


        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, urlFull, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseItems(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        Toast.makeText(ViewPayment.this,"[ERROR] Unable to Connect to Server",Toast.LENGTH_LONG).show();
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

    private void parseItems(JSONObject jsonResponse) {

        System.out.println("Starting Json Parsing Method");
        System.out.println(jsonResponse);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        //HashMap<String, String> item = null;

        try {
            JSONArray jarray = jsonResponse.getJSONArray("items");
            JSONObject jo = null;
            for (int i = 0; i < jarray.length(); i++) {

                //System.out.println("LOOP " + i);
                jo = jarray.getJSONObject(i);

                String paymentDate = jo.getString("paymentDate");
                String paymentTenant = jo.getString("paymentTenant");
                String paymentAmount = jo.getString("paymentAmount");


                HashMap<String, String> item = new HashMap<>();
                item.put("paymentDate", paymentDate);
                item.put("paymentTenant", paymentTenant);
                item.put("paymentAmount", "RM " + paymentAmount);
                list.add(item);
                //System.out.print(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] from = {"paymentDate", "paymentTenant", "paymentAmount"};
        System.out.println(from);
        int[] to = {R.id.payment_date, R.id.payment_tenant, R.id.payment_amount};

        SimpleAdapter adapter=new SimpleAdapter(this,list,R.layout.list_payment_row,from,to);
        listView.setAdapter(adapter);
        loading.dismiss();
    }

}
