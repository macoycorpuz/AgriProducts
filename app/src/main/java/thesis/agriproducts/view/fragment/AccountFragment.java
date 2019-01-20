package thesis.agriproducts.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import thesis.agriproducts.R;
import thesis.agriproducts.util.SharedPrefManager;

public class AccountFragment extends Fragment {

    //region Attributes
    View mView;
    TextView mName;
    TextView mEmail;
    TextView mNumber;
    TextView mAddress;
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
                changePassword();

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
        mName.setText(SharedPrefManager.getInstance().getUser(getActivity()).getName());
        mEmail.setText(SharedPrefManager.getInstance().getUser(getActivity()).getEmail());
        mNumber.setText(SharedPrefManager.getInstance().getUser(getActivity()).getNumber());
        mAddress.setText(SharedPrefManager.getInstance().getUser(getActivity()).getAddress());
    }

    private void changePassword() {

    }

    private void logOut() {
        SharedPrefManager.getInstance().logout(getActivity());
        getActivity().finish();
    }
}
