package com.example.vetruongmanager.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
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

public class GetDataFromOrderNumber extends AsyncTask<String, Void, String> {
    private Activity activity;
    private ProgressDialog loading;



    public GetDataFromOrderNumber(Activity activity){

            this.activity = activity;
            loading = new ProgressDialog(activity);

    }


    protected void onPreExecute() {
        this.loading.setMessage("Loading Data");
        this.loading.show();
    }

    @Override
    protected String doInBackground(String... params){

        String order_number = params[0];
        String stringUrl = "https://script.google.com/macros/s/AKfycbwX3LGV7oTZTi4QiTA7DoFp9ZfDKC0qe6U7gTbWanb9XqT0yz4/exec?stt=" + order_number;
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
        EditText editName = activity.findViewById(R.id.edit_name);
        EditText editClass = activity.findViewById(R.id.edit_class);
        EditText editPhone = activity.findViewById(R.id.edit_phone);

        Button   editButton = activity.findViewById(R.id.button_edit);
        Button confirmButton = activity.findViewById(R.id.button_confirm);
        Button refundButton = activity.findViewById(R.id.button_refund);

        try {
            JSONObject data = new JSONObject(result);
            String response = data.getString("response");





         if (response.equals("OK"))
            {
                JSONArray values = data.getJSONArray("values");
                JSONObject object = values.getJSONObject(0);

                String name = object.getString("name"); //Index of name in data = 1
                String lop = object.getString("lop");
                String phoneNumber = object.getString("phone");

                //Set text for 3 EditTexts: Name, Class, PhoneNumber

                editName.setText(name);
                editName.setEnabled(true);

                editClass.setText(lop);
                editClass.setEnabled(true);

                editPhone.setText(phoneNumber);
                editPhone.setEnabled(true);

                editButton.setEnabled(true);
                confirmButton.setEnabled(true);
                refundButton.setEnabled(true);
            }
            else
            {

                String values = data.getString("values");

                Toast.makeText(activity,
                        values, Toast.LENGTH_LONG).show();

                editName.setText("Null");
                editName.setEnabled(false);

                editClass.setText("Null");
                editClass.setEnabled(false);

                editPhone.setText("Null");
                editPhone.setEnabled(false);

                editButton.setEnabled(false);
                confirmButton.setEnabled(false);
                refundButton.setEnabled(false);
            }

        } catch (JSONException e) {
            Toast.makeText(activity,
                    "Vui Lòng Kiểm Tra Lại Kết Nối Mạng", Toast.LENGTH_LONG).show();
            Log.d("Error", e.toString());

            editName.setEnabled(false);
            editClass.setEnabled(false);
            editPhone.setEnabled(false);


            editButton.setEnabled(false);
            confirmButton.setEnabled(false);
            refundButton.setEnabled(false);
        }


    }
}
