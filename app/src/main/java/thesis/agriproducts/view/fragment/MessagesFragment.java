package thesis.agriproducts.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.R;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;
import thesis.agriproducts.model.entities.Message;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.util.SharedPrefManager;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.view.adapter.InboxAdapter;
import thesis.agriproducts.view.adapter.MessagesAdapter;
import thesis.agriproducts.view.adapter.ProductAdapter;

//TODO: (Add Function) - Bottom RecyclerView
//TODO: (Send Message) - Refresh or update adapter for new message. Check simplifiedcoding.com
public class MessagesFragment extends Fragment {

    //region Attributes
    int userId, dealId;
    ApiServices api = Api.getInstance().getApiServices();
    Call<Result> call;

    View mView;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    MessagesAdapter mMessagesAdapter;
    ProductAdapter mProductAdapter;
    ProgressBar mProgress;
    ProgressDialog pDialog;
    TextView mErrorView;
    TextView mMessage;
    List<Message> messageList;
    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutManager = new LinearLayoutManager(getActivity());
        userId = SharedPrefManager.getInstance().getUser(getActivity()).getUserId();

        mView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        mProgress = mView.findViewById(R.id.progMessages);
        mErrorView = mView.findViewById(R.id.txtMessagesError);
        mMessage = mView.findViewById(R.id.txtMessage);
        mRecyclerView = mView.findViewById(R.id.recyclerViewMessages);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        Button mSend = mView.findViewById(R.id.btnSend);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        showMessages();
        return mView;
    }

    public void setDealId(int dealId) { this.dealId = dealId; }

    private void showMessages() {
        Utils.getUtils().showProgress(true, mProgress, mRecyclerView);
        mRecyclerView.setAdapter(null);
        mErrorView.setVisibility(View.GONE);
        call = api.getMessages(dealId, userId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull Response<Result> response) {
                try {
                    Utils.getUtils().showProgress(false, mProgress, mRecyclerView);
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    messageList = response.body().getMessages();
                    fillMessages();
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

    private void sendMessage() {
        pDialog = Utils.showProgressDialog(getActivity(), "Sending message...");
        String content = mMessage.getText().toString();
        mErrorView.setVisibility(View.GONE);
        call = api.sendMessage(dealId, userId, content);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull Response<Result> response) {
                try {
                    Utils.dismissProgressDialog(pDialog);
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    messageList = response.body().getMessages();
                    Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_LONG).show();
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

    private void fillMessages() {
        mMessagesAdapter = new MessagesAdapter(getActivity(), messageList, userId);
        mRecyclerView.setAdapter(mMessagesAdapter);
    }

    private void handleError(String error) {
        Utils.dismissProgressDialog(pDialog);
        Utils.getUtils().showProgress(false, mProgress, mRecyclerView);
        mErrorView.setText(error);
        mErrorView.setVisibility(View.VISIBLE);
    }
}

