package thesis.agriproducts.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.R;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;

public class SignUpActivity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mNumberView;
    private EditText mAddressView;
    private TextView mErrorView;
    private ImageView mValidIdView;
    private String error;

    private int PICK_IMAGE_REQUEST = 1;
    private Uri fileUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Views
        mErrorView = findViewById(R.id.txtSignUpError);
        mNameView = findViewById(R.id.txtSignUpName);
        mEmailView = findViewById(R.id.txtSignUpEmail);
        mPasswordView = findViewById(R.id.txtSignUpPassword);
        mConfirmPasswordView = findViewById(R.id.txtSignUpConfirmPassword);
        mNumberView = findViewById(R.id.txtSignUpMobileNumber);
        mAddressView = findViewById(R.id.txtSignUpAddress);
        mValidIdView = findViewById(R.id.imgValidId);

        Button mSignUpButton = findViewById(R.id.btnSignUp);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        Button mSignInButton = findViewById(R.id.btnSignIn);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        Button mUploadValidId = findViewById(R.id.btnUploadValidID);
        mUploadValidId.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                fileUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(fileUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                mValidIdView.setImageBitmap(selectedImage);
                mValidIdView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
    }

    private void attemptSignUp() {
        mErrorView.setVisibility(View.GONE);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();
        String number = mNumberView.getText().toString();
        String address = mAddressView.getText().toString();

        if (!Utils.getUtils().isEmptyFields(name, email, password, confirmPassword, number, address)) {
            mErrorView.setText(R.string.error_sign_up);
            mErrorView.setVisibility(View.VISIBLE);
        } else if (fileUri == null) {
            mErrorView.setText(R.string.error_valid_id);
            mErrorView.setVisibility(View.VISIBLE);
        } else if (!Utils.getUtils().isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
        } else if (!Utils.getUtils().isPasswordValid(password) || !password.equals(confirmPassword)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
        } else {
            Utils.getUtils().hideKeyboard(this);
            signUp(name, email, password, number, address, fileUri);
        }
    }

    private void signUp(String name, String email, String password, String number, String address,Uri fileUri) {

        File filePath = new File(Utils.getUtils().getRealPathFromURI(this, fileUri));
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody numberBody = RequestBody.create(MediaType.parse("text/plain"), number);
        RequestBody addressBody = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody fileBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), filePath);

        final ProgressDialog pDialog = Utils.showProgressDialog(this, "Creating your account...");
        ApiServices api = Api.getInstance().getApiServices();
        Call<Result> call = api.register(nameBody, emailBody, passwordBody, numberBody, addressBody, fileBody);

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
                    Toast.makeText(getApplicationContext(), "Account has been created", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } catch (Exception ex) {
                    error = "Api Response Error: " + ex.getMessage();
                    mErrorView.setText(error);
                    mErrorView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@Nullable Call<Result> call, @NonNull Throwable t) {
                Utils.dismissProgressDialog(pDialog);
                error = "Api Failure: " + t.getMessage();
                mErrorView.setText(error);
                mErrorView.setVisibility(View.VISIBLE);
            }
        });
    }

}
