package thesis.agriproducts.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import thesis.agriproducts.util.SharedPrefManager;
import thesis.agriproducts.util.Tags;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.view.adapter.ProductAdapter;

public class HomeFragment extends Fragment {

    //region Attributes
    View mView;
    EditText mSearchProduct;
    TextView mErrorView;
    RecyclerView mRecyclerView;
    ProductAdapter mAdapter;
    ProgressBar mProgress;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Product> productList;
    int userId;
    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_products, container, false);
        mProgress = mView.findViewById(R.id.progProducts);
        mErrorView = mView.findViewById(R.id.txtHomeError);
        mSearchProduct = mView.findViewById(R.id.txtSearchProducts);
        mSwipeRefreshLayout = mView.findViewById(R.id.swipeView);
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchProduct.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN && keyCode ==KeyEvent.KEYCODE_ENTER))
                {
                    showProducts(mSearchProduct.getText().toString());
                    return true;
                }
                return false;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProducts(null);
            }
        });

        userId = SharedPrefManager.getInstance().getUser(getActivity()).getUserId();
        showProducts(null);
        return mView;
    }

    private void showProducts(String productName) {
        mRecyclerView.setAdapter(null);
        mErrorView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        Utils.getUtils().showProgress(true, mProgress, mRecyclerView);
        ApiServices api = Api.getInstance().getApiServices();
        Call<Result> call;
        if(productName == null) call = api.getProducts(userId);
        else call = api.getProductByName(productName);
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

    private void fillProducts() {
        mAdapter = new ProductAdapter(getActivity(), productList);
        mAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Utils.setProductId(productList.get(position).getProductId());
                Utils.switchContent(getActivity(), R.id.fragContainer, Tags.PRODUCT_DETAILS_FRAGMENT);
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
