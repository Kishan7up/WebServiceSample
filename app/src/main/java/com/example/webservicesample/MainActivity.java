package com.example.webservicesample;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.JsonParser;
import com.loopj.android.http.*;


import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    int status = 0;
    TextView txt_Respose;
    Button webCall;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_Respose = findViewById(R.id.txt_Respose);
        webCall = findViewById(R.id.webCall);


        webCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> paramMap = new
                        HashMap<String, String>();
                paramMap.put("CompanyId", "12");
                paramMap.put("LoginUserID", "divya");

                AndroidNetworking.post("https://dhruveni.co.in:88/Dashboard/Total")
                        .addBodyParameter(paramMap)
                        .addUrlEncodeFormBodyParameter("application/x-www-form-urlencoded")
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                            @Override
                            public void onResponse(Response okHttpResponse, String response) {
                                txt_Respose.setText(response);
                            }

                            @Override
                            public void onError(ANError anError) {
                                txt_Respose.setText(anError.getErrorDetail());
                              //  Log.d("djfdsjf",anError.getErrorDetail());

                            }
                        });

            }
        });
    }


    public void getPublicTimeline() throws JSONException {


        HashMap<String, String> paramMap = new
                HashMap<String, String>();
        paramMap.put("CompanyId", "12");
        paramMap.put("LoginUserID", "divya");


        RequestParams params = new RequestParams(paramMap);


        TwitterRestClient.post("Total", params, new TextHttpResponseHandler() {

            ProgressDialog pd;
            @Override
            public void onStart() {

                pd = new ProgressDialog(MainActivity.this);
                pd.setTitle("Please Wait..");
                pd.setMessage("We Are getting Data From Internet");
                pd.setIndeterminate(false);
                pd.setCancelable(false);

                pd.show();
             // CreateProgressDialog();
              //  ShowProgressDialog();

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                //txt_Respose.setText("input parameter Should not correct ");

                Log.d("sdfl",responseString+"StatusCode"+statusCode);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                // If the response is JSONObject instead of expected JSONArray
                JsonParser parser = new JsonParser();
                String retVal = parser.parse(response).getAsString();
                try {
                    JSONArray jsonArray = new JSONArray(retVal);

                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        txt_Respose.setText(jsonObject.getString("TotalFollowUp"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
/*
                txt_Respose.setText(response);
*/
            }

            @Override
            public void onFinish() {
                pd.dismiss();
            }

          /*  @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                txt_Respose.setText(errorResponse);


            }
*/
        });
    }


    public void CreateProgressDialog()
    {

        progressdialog = new ProgressDialog(MainActivity.this);

        progressdialog.setIndeterminate(false);

        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressdialog.setCancelable(true);

        progressdialog.setMax(100);

        progressdialog.show();

    }
    public void ShowProgressDialog()
    {
        status = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(status < 100){

                    status +=1;

                    try{
                        Thread.sleep(20);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            progressdialog.setProgress(status);

                            if(status == 100){

                                progressdialog.dismiss();
                            }
                        }
                    });
                }
            }
        }).start();

    }
}
