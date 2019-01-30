package thesis.agriproducts.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.R;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.model.entities.User;
import thesis.agriproducts.util.Utils;

public class UserDetailsFragment extends Fragment {

    //region Attributes
    ApiServices api = Api.getInstance().getApiServices();
    Call<Result> call;
    int userId;
    User user;

    View mView;
    TextView mName, mEmail, mNumber, mAddress, mActivate, mErrorView;
    ImageView mImage;
    ProgressDialog pDialog;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_details, container, false);
        mErrorView = mView.findViewById(R.id.txtUserError);
        mName = mView.findViewById(R.id.txtUserDetailName);
        mEmail = mView.findViewById(R.id.txtUserDetailEmail);
        mNumber = mView.findViewById(R.id.txtUserDetailNumber);
        mAddress = mView.findViewById(R.id.txtUserDetailAddress);
        mActivate = mView.findViewById(R.id.txtUserDetailActivate);
        mImage = mView.findViewById(R.id.imgUser);

        Button mActivate = mView.findViewById(R.id.btnActivateUser);
        Button mDelete = mView.findViewById(R.id.btnDeleteUser);
        mActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateUser();
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

        showUser();
        return mView;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private void showUser() {
        call = api.getUser(userId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                try {
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    user = response.body().getUser();
                    fillDetails();
                } catch (Exception ex) {
                    handleError(ex.getMessage());
                }
            }

            @Override
            public void onFailure(@Nullable Call<Result> call, @NonNull Throwable t) {
                handleError("Api Failure: " + t.getMessage());
            }
        });
    }

    private void activateUser() {
        call = api.activate();
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                try {
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    mActivate.setText("Activated");
                    Toast.makeText(getActivity(), "User has been activated", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    handleError(ex.getMessage());
                }
            }

            @Override
            public void onFailure(@Nullable Call<Result> call, @NonNull Throwable t) {
                handleError("Api Failure: " + t.getMessage());
            }
        });
    }

    private void deleteUser() {
        pDialog = Utils.showProgressDialog(getActivity(), "Deleting product...");
        ApiServices api = Api.getInstance().getApiServices();
        Call<Result> call = api.deleteUser(userId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                try {
                    Utils.dismissProgressDialog(pDialog);
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    //TODO: Go to admin fragment
                    Toast.makeText(getActivity(), "User has been deleted.", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    handleError(ex.getMessage());
                }
            }

            @Override
            public void onFailure(@Nullable Call<Result> call, @NonNull Throwable t) {
                handleError("Api Failure: " + t.getMessage());
            }
        });
    }

    private void fillDetails() {
        mName.setText(user.getName());
        mEmail.setText(String.valueOf(user.getEmail()));
//        mNumber.setText(String.valueOf(user.getNumber()));
//        mAddress.setText(user.getAddress());
//        mActivate.setText(user.getIsActivated() ? "Activated" : "Inactive");
//        Picasso.get()
//                .load(user.getUrl())
//                .placeholder(R.drawable.ic_photo_light_blue_24dp)
//                .error(R.drawable.ic_error_outline_red_24dp)
//                .into(mImage);
    }

    private void handleError(String error) {
        Utils.dismissProgressDialog(pDialog);
        mErrorView.setText(error);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
