package thesis.agriproducts.view.fragment;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.R;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;
import thesis.agriproducts.model.entities.Deal;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.util.SharedPrefManager;
import thesis.agriproducts.util.Tags;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.view.adapter.InboxAdapter;
import thesis.agriproducts.view.adapter.ProductAdapter;

public class InboxFragment extends Fragment {

    //region Attributes
    int userId;
    int BUYING_FLAG = 1;
    int SELLING_FLAG = 2;
    int CURRENT_FLAG = 0;

    ApiServices api = Api.getInstance().getApiServices();
    Call<Result> call;

    View mView;
    RecyclerView mRecyclerView;
    InboxAdapter mAdapter;
    ProgressBar mProgress;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mErrorView;
    List<Deal> dealList;
    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_inbox, container, false);
        mProgress = mView.findViewById(R.id.progInbox);
        mErrorView = mView.findViewById(R.id.txtInboxError);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeViewInbox);
        mRecyclerView = mView.findViewById(R.id.recyclerViewInbox);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { showDeals(CURRENT_FLAG); }
        });
        userId = SharedPrefManager.getInstance().getUser(getActivity()).getUserId();
        Button mSellingBtn = mView.findViewById(R.id.btnSelling);
        Button mBuyingBtn= mView.findViewById(R.id.btnBuying);
        mSellingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeals(SELLING_FLAG);
            }
        });
        mBuyingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeals(BUYING_FLAG);
            }
        });
        return mView;
    }

    private void showDeals(int dealFlag) {
        CURRENT_FLAG = dealFlag;
        mRecyclerView.setAdapter(null);
        mErrorView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        Utils.getUtils().showProgress(true, mProgress, mRecyclerView);
        call = (dealFlag == BUYING_FLAG) ? api.getBuying(userId) : api.getSelling(userId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull Response<Result> response) {
                try {
                    Utils.getUtils().showProgress(false, mProgress, mRecyclerView);
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    dealList = response.body().getDeals();
                    fillDeals();
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

    private void fillDeals() {
        mAdapter = new InboxAdapter(getActivity(), dealList);
        mAdapter.setOnItemClickListener(new InboxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO: Go to Messages
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    private void handleError(String error) {
        Utils.getUtils().showProgress(false, mProgress, mRecyclerView);
        mErrorView.setText(error);
        mErrorView.setVisibility(View.VISIBLE);
    }

}
