package bth.pa2555.agilegardeningapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Response;

import bth.pa2555.helpers.AppCompatActivityBase;
import bth.pa2555.helpers.DialogBuilder;
import bth.pa2555.helpers.RestRequest;
import bth.pa2555.models.Flower;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FlowerInfoActivity extends AppCompatActivityBase {

    private ImageView mImageViewFlowerImage;
    private WebView mWebViewFlowerInfo;
    private FloatingActionButton mFloatingActionButton;
    private Flower mFlower;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_detail);

        initFlowerInfo();
        initUI();
    }

    private void initFlowerInfo() {
        mFlower = getIntent().getExtras().getParcelable("flower_info");
    }

    private void initUI() {
        mImageViewFlowerImage = findViewById(R.id.imageView_flower_image);
        mWebViewFlowerInfo = findViewById(R.id.webView_flower_info);
        mFloatingActionButton = findViewById(R.id.fab_favorite_flower);
        setupToolbar();
        setToolbarTitle(mFlower.getName());

        Glide.with(getCurrentContext()).load(mFlower.getImageUrl())
                .transition(withCrossFade())
                .into(mImageViewFlowerImage);
        mWebViewFlowerInfo.loadData(mFlower.getInfo(), "text/html; charset=UTF-8;", "UTF-8");
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFlower.isFavorite())
                    makeUnFavorite();
                else
                    makeFavorite();
            }
        });
    }

    private void makeFavorite() {
        final AlertDialog alertDialogLoading = DialogBuilder.buildLoaingDialogWithSpinnerIcon(
                getCurrentContext(),
                false
        );
        RestRequest.makePostRequest(
                getCurrentContext(),
                "AddPlantToFavorite/" + mFlower.getId(),
                "",
                new RestRequest.IRequestResponse() {
                    @Override
                    public void onException() {
                        Toast.makeText(getCurrentContext(), "api calling threw an exception",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStatusOK(Response<String> result) {
                        Toast.makeText(getCurrentContext(), "Done", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStatusNotOK(HeadersResponse header) {
                        Toast.makeText(getCurrentContext(),
                                "api failed with code : " + String.valueOf(header.code()),
                                Toast.LENGTH_LONG).show();
                    }
                }, new RestRequest.IBeforeAfterRequest() {
                    @Override
                    public void before() {
                        alertDialogLoading.show();
                    }

                    @Override
                    public void after() {
                        alertDialogLoading.dismiss();
                    }
                }
        );
    }

    private void makeUnFavorite() {
        final AlertDialog alertDialogLoading = DialogBuilder.buildLoaingDialogWithSpinnerIcon(
                getCurrentContext(),
                false
        );
        RestRequest.makePostRequest(
                getCurrentContext(),
                    "RemovePlantFromFavorite/" + mFlower.getId(),
                "",
                new RestRequest.IRequestResponse() {
                    @Override
                    public void onException() {
                        Toast.makeText(getCurrentContext(), "api calling threw an exception",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStatusOK(Response<String> result) {
                        Toast.makeText(getCurrentContext(), "Done", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStatusNotOK(HeadersResponse header) {
                        Toast.makeText(getCurrentContext(),
                                "api failed with code : " + String.valueOf(header.code()),
                                Toast.LENGTH_LONG).show();
                    }
                }, new RestRequest.IBeforeAfterRequest() {
                    @Override
                    public void before() {
                        alertDialogLoading.show();
                    }

                    @Override
                    public void after() {
                        alertDialogLoading.dismiss();
                    }
                }
        );
    }
}
