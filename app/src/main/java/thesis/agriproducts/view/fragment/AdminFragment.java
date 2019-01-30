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
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.R;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;
import thesis.agriproducts.model.entities.Product;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.model.entities.User;
import thesis.agriproducts.util.Tags;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.view.adapter.ProductAdapter;
import thesis.agriproducts.view.adapter.UserAdapter;

public class AdminFragment extends Fragment {

    //region Attributes
    ApiServices api = Api.getInstance().getApiServices();
    Call<Result> call;

    String CURRENT_TAG = "";
    View mView;
    TextView mTitle, mErrorView;
    RecyclerView mRecyclerView;
    ProgressBar mProgress;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ProductAdapter mProductAdapter;
    UserAdapter mUserAdapter;
    List<Product> productList;
    List<User> userList;
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_admin, container, false);
        mTitle = mView.findViewById(R.id.txtTitleAdmin);
        mProgress = mView.findViewById(R.id.progAdmin);
        mErrorView = mView.findViewById(R.id.txtAdminError);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeViewAdmin);
        mRecyclerView = mView.findViewById(R.id.recyclerViewAdmin);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                show();
            }
        });
        show();
        return mView;
    }

    public void setTag(String CURRENT_TAG) {
        this.CURRENT_TAG = CURRENT_TAG;
    }

    public void show() {
        switch (CURRENT_TAG) {
            case Tags.USERS_FRAGMENT:
                mTitle.setText(Tags.USERS_FRAGMENT);
                showUsers();
                break;
            case Tags.PRODUCTS_FRAGMENT:
                mTitle.setText(Tags.PRODUCTS_FRAGMENT);
                showProducts();
                break;
        }
    }

    private void showProducts() {
        clearAll();
        Utils.getUtils().showProgress(true, mProgress, mRecyclerView);
        call = api.getAllProducts();
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                try {
                    Utils.getUtils().showProgress(false, mProgress, mRecyclerView);
                    if(response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if(response.body().getError())
                        throw new Exception(response.body().getMessage());
                    productList = response.body().getProducts();
                    fillProducts();
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

    private void showUsers() {
        clearAll();
        Utils.getUtils().showProgress(true, mProgress, mRecyclerView);
        call = api.getAllUsers();
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                try {
                    Utils.getUtils().showProgress(false, mProgress, mRecyclerView);
                    if(response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if(response.body().getError())
                        throw new Exception(response.body().getMessage());
                    userList = response.body().getUsers();
                    fillUsers();
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

    private void fillProducts() {
        mProductAdapter = new ProductAdapter(getActivity(), productList);
        mProductAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Utils.setProductId(productList.get(position).getProductId());
                Utils.switchContent(getActivity(), R.id.adminContainer, Tags.PRODUCT_DETAILS_FRAGMENT);
            }
        });
        mRecyclerView.setAdapter(mProductAdapter);
    }

    private void fillUsers() {
        mUserAdapter = new UserAdapter(getActivity(), userList);
        mUserAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Utils.setUserId(userList.get(position).getUserId());
                Utils.switchContent(getActivity(), R.id.adminContainer, Tags.USER_DETAILS_FRAGMENT);
            }
        });
        mRecyclerView.setAdapter(mUserAdapter);
    }

    private void handleError(String error) {
        Utils.getUtils().showProgress(false, mProgress, mRecyclerView);
        mErrorView.setText(error);
        mErrorView.setVisibility(View.VISIBLE);
    }

    private void clearAll(){
        mRecyclerView.setAdapter(null);
        mErrorView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
