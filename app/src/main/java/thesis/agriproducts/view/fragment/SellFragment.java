package thesis.agriproducts.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import java.io.File;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thesis.agriproducts.R;
import thesis.agriproducts.domain.Api;
import thesis.agriproducts.domain.ApiServices;
import thesis.agriproducts.model.entities.Result;
import thesis.agriproducts.util.SharedPrefManager;
import thesis.agriproducts.util.Utils;
import thesis.agriproducts.view.adapter.ProductAdapter;

public class SellFragment extends Fragment {

    //region Attributes
    int PICK_IMAGE_REQUEST = 1;
    int PLACE_PICKER_REQUEST = 2;
    View mView;
    EditText mProductName, mLocation, mPrice, mQuantity, mDescription;
    ImageView mProductImage;
    TextView mErrorView;
    Uri fileUri;
    int sellerId;
    String productName, description, quantity, price, location, lat, lng;
    ProgressDialog pDialog;
    RequestBody sellerIdBody, productNameBody, descriptionBody, quantityBody, priceBody, locationBody, latBody, lngBody, fileBody;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product_sell, container, false);

        // Initialize Views
        mProductName = mView.findViewById(R.id.txtSellProductName);
        mLocation = mView.findViewById(R.id.txtSellLocation);
        mPrice = mView.findViewById(R.id.txtSellPrice);
        mQuantity = mView.findViewById(R.id.txtSellQuantity);
        mDescription = mView.findViewById(R.id.txtSellDescription);
        mProductImage = mView.findViewById(R.id.imgSellProduct);
        mErrorView = mView.findViewById(R.id.txtSellError);
        Button mSellProduct = mView.findViewById(R.id.btnSell);

        mLocation.setInputType(InputType.TYPE_NULL);
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDestination();
            }
        });
        mLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) { selectDestination(); }
        });
        mProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        mSellProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptPostProduct();
            }
        });

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                try {
                    fileUri = data.getData();
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(fileUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mProductImage.setImageBitmap(selectedImage);
                    mProductImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(data, getActivity());
                location = place.getAddress().toString();
                lat = Double.toString((place.getLatLng().latitude));
                lng = Double.toString((place.getLatLng().longitude));
                mLocation.setText(location);
            }
        }

    }


    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
    }

    public void selectDestination() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = builder.build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }

    private void attemptPostProduct() {
        mErrorView.setVisibility(View.GONE);
        sellerId = SharedPrefManager.getInstance().getUser(getActivity()).getUserId();
        productName = mProductName.getText().toString();
        description = mDescription.getText().toString();
        quantity = mQuantity.getText().toString();
        price = mPrice.getText().toString();

        if (!Utils.getUtils().isEmptyFields(productName, description, quantity, price, location, lat, lng)) {
            mErrorView.setText(R.string.error_sell_product);
            mErrorView.setVisibility(View.VISIBLE);
        } else if (fileUri == null) {
            mErrorView.setText(R.string.error_product_image);
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            Utils.getUtils().hideKeyboard(getActivity());
            postProduct();
        }
    }

    private void postProduct() {
        pDialog = Utils.showProgressDialog(getActivity(), "Posting your product...");
        ApiServices api = Api.getInstance().getApiServices();
        parseRequestBody();
        Call<Result> call = api.postProduct(sellerIdBody, productNameBody, descriptionBody, quantityBody, priceBody, locationBody, latBody, lngBody, fileBody);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@Nullable Call<Result> call, @NonNull Response<Result> response) {
                try {
                    Utils.dismissProgressDialog(pDialog);
                    if (response.errorBody() != null)
                        throw new Exception(response.errorBody().string());
                    if (response.body().getError())
                        throw new Exception(response.body().getMessage());
                    Toast.makeText(getActivity(), "Your product has been posted.", Toast.LENGTH_LONG).show();
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

    private void parseRequestBody() {
        File filePath = new File(Utils.getUtils().getRealPathFromURI(getActivity(), fileUri));
        sellerIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sellerId));
        productNameBody = RequestBody.create(MediaType.parse("text/plain"), productName);
        descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        quantityBody = RequestBody.create(MediaType.parse("text/plain"), quantity);
        priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
        locationBody = RequestBody.create(MediaType.parse("text/plain"), location);
        latBody = RequestBody.create(MediaType.parse("text/plain"), lat);
        lngBody = RequestBody.create(MediaType.parse("text/plain"), lng);
        fileBody = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), filePath);
    }

    private void handleError(String error) {
        Utils.dismissProgressDialog(pDialog);
        mErrorView.setText(error);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
