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
import thesis.agriproducts.R;
import thesis.agriproducts.model.entities.Product;
import thesis.agriproducts.model.entities.User;
import thesis.agriproducts.util.Tags;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.view.adapter.ProductAdapter;

public class AdminFragment extends Fragment {

    //region Attributes
    String CURRENT_TAG = "";
    View mView;
    TextView mTitle, mErrorView;
    RecyclerView mRecyclerView;
    ProgressBar mProgress;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ProductAdapter mProductAdapter;
//    UsersAdapter mUserAdapter;
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
                break;
            case Tags.PRODUCTS_FRAGMENT:
                mTitle.setText(Tags.PRODUCTS_FRAGMENT);
                break;
        }
    }

    private void showProducts() {

    }

//    private void fillProducts() {
//        mProductAdapter = new ProductAdapter(getActivity(), productList);
//        mProductAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Utils.setProductId(productList.get(position).getProductId());
//                Utils.switchContent(getActivity(), R.id.adminContainer, Tags.PRODUCT_DETAILS_FRAGMENT);
//            }
//        });
//        mRecyclerView.setAdapter(mProductAdapter);
//    }
//
//    private void fillUsers() {
//        mUserAdapter = new ProductAdapter(getActivity(), productList);
//        mUserAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Utils.setProductId(productList.get(position).getProductId());
//                Utils.switchContent(getActivity(), R.id.adminContainer, Tags.PRODUCT_DETAILS_FRAGMENT);
//            }
//        });
//        mRecyclerView.setAdapter(mUserAdapter);
//    }

    private void handleError(String error) {
        Utils.getUtils().showProgress(false, mProgress, mRecyclerView);
        mErrorView.setText(error);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
