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
        String stringUrl = "https://script.google.com/macros/s/AKfycbx1HcGi33WXU-ezWmZdzlVuDIEJdTKgBakLLd9oDAZ8E4aEzc8HihW6/exec?ticketid=" + ticketid + "&&check=" + check;
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
            if (!response.equals("Error2"))
            {
                JSONObject info = data.getJSONObject("info");
                String name = info.getString("name");
                String lop = info.getString("lop");
                String phone = info.getString("phone");

                EditText Namecheck = activity.findViewById(R.id.namecheck);
                EditText Classcheck = activity.findViewById(R.id.classcheck);
                EditText Phonecheck = activity.findViewById(R.id.phonecheck);
                EditText Statsuscheck = activity.findViewById(R.id.statuscheck);

                Namecheck.setText(name);
                Classcheck.setText(lop);
                Phonecheck.setText(phone);
                Statsuscheck.setText(values);

                if (response.equals("OK"))
                {
                    Namecheck.setTextColor(0xff25DF08);
                    Classcheck.setTextColor(0xff25DF08);
                    Phonecheck.setTextColor(0xff25DF08);
                    Statsuscheck.setTextColor(0xff25DF08);
                }
                else
                {
                    Namecheck.setTextColor(0xffff0000);
                    Classcheck.setTextColor(0xffff0000);
                    Phonecheck.setTextColor(0xffff0000);
                    Statsuscheck.setTextColor(0xffff0000);
                }
            }

        } catch (JSONException e) {
            Toast.makeText(activity,
                    "Vui Lòng Kiểm Tra Lại Kết Nối Mạng", Toast.LENGTH_LONG).show();
            Log.d("Error", e.toString());


        }


    }
}
