package thesis.agriproducts.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.R;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.util.SharedPrefManager;
import thesis.agriproducts.util.Utils;

public class AccountFragment extends Fragment {

    //region Attributes
    View mView;
    TextView mName;
    TextView mEmail;
    TextView mNumber;
    TextView mAddress;
    TextView mErrorView;
    int userId;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account, container, false);
        mName = mView.findViewById(R.id.txtUserName);
        mEmail = mView.findViewById(R.id.txtUserEmail);
        mNumber = mView.findViewById(R.id.txtUserNumber);
        mAddress = mView.findViewById(R.id.txtUserAddress);
        Button mChangePassword = mView.findViewById(R.id.btnChangePassowrd);
        Button mLogout = mView.findViewById(R.id.btnLogout);
        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePassword();

            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        showUser();
        return mView;
    }

    private void showUser() {
        userId = SharedPrefManager.getInstance().getUser(getActivity()).getUserId();
        mName.setText(SharedPrefManager.getInstance().getUser(getActivity()).getName());
        mEmail.setText(SharedPrefManager.getInstance().getUser(getActivity()).getEmail());
        mNumber.setText(SharedPrefManager.getInstance().getUser(getActivity()).getNumber());
        mAddress.setText(SharedPrefManager.getInstance().getUser(getActivity()).getAddress());
    }

    private void showChangePassword() {
        //Initialize
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final LinearLayout layout = new LinearLayout(getActivity());
        final EditText mOldPassword = new EditText (getActivity());
        final EditText mNewPassword = new EditText (getActivity());

        //Layout
        mOldPassword.setHint("Current Password");
        mNewPassword.setHint("New Password");
        mOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(mOldPassword);
        layout.addView(mNewPassword);
        alert.setTitle("Change Password");
        alert.setView(layout);

        //Handler
        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String oldPassword = mOldPassword.getText().toString();
                String newPassword = mNewPassword.getText().toString();
                changePassword(oldPassword, newPassword);
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { dialog.dismiss();
            }
        });
        alert.show();
    }

    private void changePassword(String oldP, String newP) {
        if (!Utils.getUtils().isEmptyFields(oldP, newP)) {
            Toast.makeText(getActivity(), "Invalid Fields", Toast.LENGTH_LONG).show();
        } else if (!Utils.getUtils().isPasswordValid(oldP)) {
            Toast.makeText(getActivity(), "Invalid Old Password", Toast.LENGTH_LONG).show();
        } else if (!Utils.getUtils().isPasswordValid(newP)) {
            Toast.makeText(getActivity(), "Invalid New Password", Toast.LENGTH_LONG).show();
        }

        try {
            ApiServices api = Api.getInstance().getApiServices();
            Call<Result> call = api.changePassoword(userId, oldP, newP);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                    try {
                        if (response.errorBody() != null)
                            throw new Exception(response.errorBody().string());
                        if (response.body().getError())
                            throw new Exception(response.body().getMessage());
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        logOut();
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@Nullable Call<Result> call, @NonNull Throwable t) {
                    Toast.makeText(getActivity(), "Api Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void logOut() {
        SharedPrefManager.getInstance().logout(getActivity());
        getActivity().finish();
    }
}
