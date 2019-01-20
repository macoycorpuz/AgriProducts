package thesis.agriproducts.view.activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.util.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import thesis.agriproducts.R;

public class LoginActivity extends AppCompatActivity{

    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView mErrorView;
    private String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.txtEmail);
        mPasswordView = findViewById(R.id.txtPassword);
        mErrorView = findViewById(R.id.txtLoginError);

        Button mLoginButton = findViewById(R.id.btnLogin);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mSignUpButton = findViewById(R.id.btnRegister);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void attemptLogin() {
        mErrorView.setVisibility(View.GONE);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (!Utils.getUtils().isEmptyFields(email, password)) {
            mErrorView.setText(R.string.error_login);
            mErrorView.setVisibility(View.VISIBLE);
        } else if (!Utils.getUtils().isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
        }
        else if (!Utils.getUtils().isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
        } else {
            Utils.getUtils().hideKeyboard(this);
            login(email, password);
        }
    }

    //TODO: Add Admin login
    //TODO: Check if activated
    private void login(String email, String password) {
        final ProgressDialog pDialog = Utils.showProgressDialog(this, "Logging In...");
        ApiServices api = Api.getInstance().getApiServices();
        Call<Result> call = api.login(email, password);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull Response<Result> response) {
                try {
                    Utils.dismissProgressDialog(pDialog);
                    if(response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if(response.body().getError())
                        throw new Exception(response.body().getMessage());
                    finish();
                    SharedPrefManager.getInstance().userLogin(getApplicationContext(), response.body().getUser());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                } catch (Exception ex) {
                    error = "Api Response Error: " + ex.getMessage();
                    mErrorView.setText(error);
                    mErrorView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@Nullable Call<Result> call,@NonNull Throwable t) {
                Utils.dismissProgressDialog(pDialog);
                error = "Api Failure: " + t.getMessage();
                mErrorView.setText(error);
                mErrorView.setVisibility(View.VISIBLE);
            }
        });
    }
}

