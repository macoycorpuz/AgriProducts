package thesis.agriproducts.view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import thesis.agriproducts.util.SharedPrefManager;
import thesis.agriproducts.util.Tags;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.view.adapter.ProductAdapter;

public class ProductDetailsFragment extends Fragment {

    //region Attributes
    int productId, userId;
    boolean isMyProduct;
    View mView, mDetails;
    TextView mProductName, mPrice, mQuantity, mLocation, mDescription, mErrorView;
    ImageView mImage;
    ProgressBar mProgress;
    ProgressDialog pDialog;

    Product product;
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
        Button mMakeOffer = mView.findViewById(R.id.btnMakeOffer);
        Button mDelete = mView.findViewById(R.id.btnDeleteProduct);
        mMakeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageAlert();
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });

        if(isMyProduct) {
            mMakeOffer.setVisibility(View.GONE);
            mDelete.setVisibility(View.VISIBLE);
        }

        userId = SharedPrefManager.getInstance().getUser(getActivity()).getUserId();
        showProduct();
        return mView;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setIsMyProduct(boolean isMyProduct) {this.isMyProduct = isMyProduct; }

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

    //TODO: (Message Alert) - Change specific content if possible.
    private void messageAlert() {
        final String content = "I want to buy your product.";
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        EditText mMessage = new EditText (getActivity());
        mMessage.setHint("message");
        mMessage.setText(content);
        alert.setTitle("Enter Message");
        alert.setView(mMessage);
        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                deal = new Deal(productId, userId, content);
                sendDeal();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { dialog.dismiss();
            }
        });
        alert.show();
    }

    //TODO: (Delete Product) - Alert dialog and close fragment
    private void deleteProduct() {
        pDialog = Utils.showProgressDialog(getActivity(), "Deleting product...");
        ApiServices api = Api.getInstance().getApiServices();
        Call<Result> call = api.deleteProduct(productId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull final Response<Result> response) {
                try {
                    Utils.getUtils().showProgress(false, mProgress, mDetails);
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    Toast.makeText(getActivity(), "Product has been deleted.", Toast.LENGTH_LONG).show();
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

    //TODO: (Send Deal) - Check if working
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
