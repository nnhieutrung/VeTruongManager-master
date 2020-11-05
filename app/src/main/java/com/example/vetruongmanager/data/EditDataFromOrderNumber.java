package com.example.vetruongmanager.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vetruongmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class EditDataFromOrderNumber extends AsyncTask<String, Void, String> {
    private Activity activity;
    private ProgressDialog loading;

    public EditDataFromOrderNumber(Activity activity){

            this.activity = activity;
            loading = new ProgressDialog(activity);

    }


    protected void onPreExecute() {
        this.loading.setMessage("Sending Data");
        this.loading.show();
    }

    @Override
    protected String doInBackground(String... params){

        String ordernumber = params[0];
        String name= params[1];
        String lop = params[2];
        String phone = params[3];
        String stringUrl = "https://script.google.com/macros/s/AKfycbzBZLXvA8_UQ40y2zGJeO6XZ5MFfeM_UhUH7GI7q1fLVuKtSasS/exec?stt=" + ordernumber + "&name=" + name + "&lop="+ lop + "&phone=" + phone;
        StringBuilder content = new StringBuilder();
        try
        {
            // create a url object
            URL url = new URL(stringUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        return content.toString();
    }
    protected void onPostExecute(String result){
        super.onPostExecute(result);

        if (loading.isShowing()) {
            loading.dismiss();
        }
        //Read JSON data
        try {
            JSONObject data = new JSONObject(result);
            String values = data.getString("values");


            Toast.makeText(activity,
                    values, Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Toast.makeText(activity,
                    "Vui Lòng Kiểm Tra Lại Kết Nối Mạng", Toast.LENGTH_LONG).show();
            Log.d("Error", e.toString());


        }


    }
}
