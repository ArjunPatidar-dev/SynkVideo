package com.example.synkvideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.synkvideo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInSignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = SignInSignUpActivity.this.getClass().getSimpleName();
    private final int SIGN_IN = 0;
    private final int SIGN_UP = 1;
    private int OPENED_SCREEN = -1;

    private TextView tvTitle;
    private TextView tvUserNameTitle;
    private TextView tvPassTitle;
    private TextView btnSign;
    private TextView tvBottomTitle;
    private TextView btnBottomSign;

    private EditText etFullName;
    private EditText etEmailId;
    private EditText etPassword;

    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListener();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i(TAG, "onStart: currentUser ::" + currentUser);
        if (currentUser == null) {
            Log.i(TAG, "onStart: currentUser is null");
            openSignUpScreen();
        }else {
            Log.i(TAG, "onStart: currentUser :" +currentUser.toString());
            callHome();
        }
    }
    private void callHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvUserNameTitle = findViewById(R.id.tv_username_title);
        tvPassTitle = findViewById(R.id.tv_pass_title);
        btnSign = findViewById(R.id.btn_signin);
        tvBottomTitle = findViewById(R.id.tv_bottom_title);
        btnBottomSign = findViewById(R.id.tv_bottom_signup);

        etFullName = findViewById(R.id.et_username_in);
        etEmailId = findViewById(R.id.et_email_in);
        etPassword = findViewById(R.id.et_pass_in);

        OPENED_SCREEN = SIGN_IN;
    }

    private void initListener() {
        btnSign.setOnClickListener(this);
        btnBottomSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                String emailId = etEmailId.getText().toString();
                String password = etPassword.getText().toString();
                if (OPENED_SCREEN == SIGN_IN) {
                    Toast.makeText(this, "Sign in clicked", Toast.LENGTH_SHORT).show();

                    if (checkField(etEmailId, emailId, "Email")
                            && checkField(etPassword, password, "Password")) {
                        mAuth.signInWithEmailAndPassword(emailId, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Log.d(TAG, "signInWithEmailAndPassword:success task ::" + task.getResult());
                                            Toast.makeText(SignInSignUpActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Log.i(TAG, "onComplete: user ::" + user);
                                            Log.i(TAG, "onComplete: user ::" + user.toString());
                                            callHome();
                                        }else {
                                            Toast.makeText(SignInSignUpActivity.this, "Check email or password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(this, "Sign up clicked", Toast.LENGTH_SHORT).show();
                    String userName = etFullName.getText().toString();

                    if (checkField(etFullName, userName, "Full Name")
                            && checkField(etEmailId, emailId, "Email")
                            && checkField(etPassword, password, "Password")) {

                        mAuth.createUserWithEmailAndPassword(emailId, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.i(TAG, "onComplete: task :::" + task.getResult());
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignInSignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Log.i(TAG, "onComplete: user :::" + user);
                                            callHome();
                                        }else {
                                            Toast.makeText(SignInSignUpActivity.this, "Registration fail", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
                break;
            case R.id.tv_bottom_signup:
                if (OPENED_SCREEN == SIGN_IN) {
                    openSignUpScreen();
                } else {
                    openSignInScreen();
                }
                break;
            default:
                break;
        }
    }

    private void openSignUpScreen() {
        tvTitle.setText(getResources().getText(R.string.sign_up));
        tvUserNameTitle.setVisibility(View.VISIBLE);
        etFullName.setVisibility(View.VISIBLE);
        tvPassTitle.setText(getResources().getText(R.string.create_pass));
        btnSign.setText(getResources().getText(R.string.sign_up));
        tvBottomTitle.setText("Already have an account?   ");
        btnBottomSign.setText(getResources().getText(R.string.sign_in));
        etFullName.setText("");
        etEmailId.setText("");
        etPassword.setText("");

        OPENED_SCREEN = SIGN_UP;
    }

    private void openSignInScreen() {
        tvTitle.setText(getResources().getText(R.string.sign_in));
        tvUserNameTitle.setVisibility(View.GONE);
        etFullName.setVisibility(View.GONE);
        tvPassTitle.setText(getResources().getText(R.string.password));
        btnSign.setText(getResources().getText(R.string.sign_in));
        tvBottomTitle.setText("Don't have an account?   ");
        btnBottomSign.setText(getResources().getText(R.string.sign_up));
        etEmailId.setText("");
        etPassword.setText("");

        OPENED_SCREEN = SIGN_IN;
    }

    private boolean checkField(EditText editText, String value, String error) {
        if (value == null || value.isEmpty()) {
            editText.setText("Enter your" + error);
            editText.setFocusable(true);
            return false;
        }
        return true;
    }
}