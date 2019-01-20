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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.R;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;
import thesis.agriproducts.model.entities.Deal;
import thesis.agriproducts.model.entities.Message;
import thesis.agriproducts.model.entities.Product;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.util.Tags;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.view.adapter.ProductAdapter;

public class ProductDetailsFragment extends Fragment {

    //region Attributes
    int productId;
    View mView;
    View mDetails;
    TextView mProductName;
    TextView mPrice;
    TextView mQuantity;
    TextView mLocation;
    TextView mDescription;
    ImageView mImage;
    ProgressBar mProgress;
    TextView mErrorView;
    Product product;
    ProgressDialog pDialog;
    Deal deal;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product_details, container, false);
        mDetails = mView.findViewById(R.id.viewDetails);
        mProgress = mView.findViewById(R.id.progDetails);
        mErrorView = mView.findViewById(R.id.txtProdDetailsError);
        mProductName = mView.findViewById(R.id.txtProdName);
        mPrice = mView.findViewById(R.id.txtProdPrice);
        mQuantity = mView.findViewById(R.id.txtProdQuantity);
        mLocation = mView.findViewById(R.id.txtProdLocation);
        mDescription = mView.findViewById(R.id.txtProdDescription);
        mImage = mView.findViewById(R.id.imgProduct);
        Button mMessage = mView.findViewById(R.id.btnMessage);
        Button mMakeOffer = mView.findViewById(R.id.btnMakeOffer);
        mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message();

            }
        });
        mMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOffer();
            }
        });
        showProduct();
        return mView;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    private void message() {

    }

    private void makeOffer() {
    }

    private void sendDeal() {
        pDialog = Utils.showProgressDialog(getActivity(), "Sending a deal...");
        ApiServices api = Api.getInstance().getApiServices();
        Call<Result> call = api.postDeal(deal);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                try {
                    Utils.getUtils().showProgress(false, mProgress, mDetails);
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
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

    private void showProduct() {
        Utils.getUtils().showProgress(true, mProgress, mDetails);
        ApiServices api = Api.getInstance().getApiServices();
        Call<Result> call = api.getProduct(productId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                try {
                    Utils.getUtils().showProgress(false, mProgress, mDetails);
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    product = response.body().getProduct();
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

    private void fillDetails() {
        mProductName.setText(product.getProductName());
        mPrice.setText(String.valueOf(product.getPrice()));
        mQuantity.setText(String.valueOf(product.getQuantity()));
        mLocation.setText(product.getLocation());
        mDescription.setText(product.getDescription());
        Picasso.get()
                .load(product.getProductUrl())
                .placeholder(R.drawable.ic_photo_light_blue_24dp)
                .error(R.drawable.ic_error_outline_red_24dp)
                .into(mImage);
    }

    private void handleError(String error) {
        Utils.dismissProgressDialog(pDialog);
        Utils.getUtils().showProgress(false, mProgress, mDetails);
        mErrorView.setText(error);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
