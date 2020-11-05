package com.example.vetruongmanager.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vetruongmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class QrCheckin extends AsyncTask<String, Void, String> {
    private Activity activity;
    private ProgressDialog loading;



    public QrCheckin(Activity activity){

            this.activity = activity;
            loading = new ProgressDialog(activity);

    }


    protected void onPreExecute() {
        this.loading.setMessage("Check-in...");
        this.loading.show();
    }

    @Override
    protected String doInBackground(String... params){

        String ticketid = params[0];
        String check = params[1];
        String stringUrl = "https://script.google.com/macros/s/AKfycbyejkJsUB-UdYx_JdOkvnmEm9L9G3Ps9ohxiIfeKoTUCdiYDkc/exec?ticketid=" + ticketid + "&&check=" + check;
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
            String response = data.getString("response");
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
