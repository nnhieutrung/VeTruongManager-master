package com.example.vetruongmanager.ui;

import android.app.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.vetruongmanager.R;
import com.example.vetruongmanager.data.EditDataFromOrderNumber;
import com.example.vetruongmanager.data.GetDataFromOrderNumber;

import com.example.vetruongmanager.data.QrChange;
import com.google.zxing.Result;


import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRChangeFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText searchEditText;
    private Button editButton;
    private Button changeButton;
    private FrameLayout frameLayout;
    private CodeScanner mCodeScanner;
    private ImageView searchEditTextButton;

    public  String ordernumber ;

    public QRChangeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QRChangeFragment newInstance(String param1, String param2) {
        QRChangeFragment fragment = new QRChangeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity activity = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_qr_change, container, false);
        searchEditText = rootView.findViewById(R.id.edit_text_order_number);
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    // Toast.makeText(activity, searchEditText.getText(), Toast.LENGTH_SHORT).show();
                    try {

                            searchData(activity, searchEditText.getText().toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        searchEditTextButton = rootView.findViewById(R.id.edit_text_order_number_button);
        searchEditTextButton.setClickable(true);
        searchEditTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    searchData(activity, searchEditText.getText().toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        editButton = rootView.findViewById(R.id.button_edit);
        changeButton = rootView.findViewById(R.id.button_change);
        frameLayout = rootView.findViewById(R.id.layout_qr_scanner);





        CodeScannerView scannerView = rootView.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      // Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();

                       new QrChange(activity).execute(ordernumber , result.getText() );


                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return rootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void confirmInfomation(View v) {
        frameLayout.setVisibility(View.VISIBLE);
    }

    private void searchData(Activity activity, String order_number) throws IOException {

        editButton.setEnabled(false);
        changeButton.setEnabled(false);

        EditText editName = activity.findViewById(R.id.edit_name);
        EditText editClass = activity.findViewById(R.id.edit_class);
        EditText editPhone = activity.findViewById(R.id.edit_phone);

        editName.setText("");
        editClass.setText("");
        editPhone.setText("");
        frameLayout.setVisibility(View.GONE);
          new GetDataFromOrderNumber(activity).execute(order_number);





        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName = activity.findViewById(R.id.edit_name);
                EditText editClass = activity.findViewById(R.id.edit_class);
                EditText editPhone = activity.findViewById(R.id.edit_phone);
  new EditDataFromOrderNumber(activity).execute( order_number , editName.getText().toString() , editClass.getText().toString() , editPhone.getText().toString() );

            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ordernumber = order_number;
                    confirmInfomation(v);
            }
        });


    }


}

