package com.example.vetruongmanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.vetruongmanager.MainActivity;
import com.example.vetruongmanager.R;

public class IntroActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    //Constant time delay
    private final int DELAY = 1500;

    //Fields (Widgets)
    private ImageView logoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PERMISSIONS = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION};
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        super.onCreate(savedInstanceState);


        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        getWindow().setBackgroundDrawable(null);

        //Methods to call
        initializeView();
        animatedLogo();
        goToMainActivity();
    }

    private void initializeView() {
        logoView = findViewById(R.id.intro_logo);
    }

    private void animatedLogo() {
        //This method will animate logo
        Animation fadingAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadingAnimation.setDuration(DELAY);

        logoView.startAnimation(fadingAnimation);
    }

    private void goToMainActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) {
            //This method will take the user to main actitvity when the animation is finished
            new Handler().postDelayed(() -> {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
            }, DELAY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}