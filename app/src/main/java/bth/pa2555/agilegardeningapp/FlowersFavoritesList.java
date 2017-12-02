package bth.pa2555.agilegardeningapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.canelmas.let.AskPermission;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bth.pa2555.adapters.FlowerAdapter;
import bth.pa2555.helpers.AppCompatActivityBase;
import bth.pa2555.helpers.DialogBuilder;
import bth.pa2555.helpers.PreferencesHelper;
import bth.pa2555.helpers.RecyclerItemClickListener;
import bth.pa2555.helpers.RestRequest;
import bth.pa2555.models.Flower;
import pl.aprilapps.easyphotopicker.EasyImage;


public class FlowersFavoritesList extends AppCompatActivityBase {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mButtonTakePicture;
    private FlowerAdapter mFlowerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_history_list);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupData();
    }

    private void initUI() {
        mRecyclerView = findViewById(R.id.rv_flower_history);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getCurrentContext(), LinearLayoutManager.VERTICAL, false));
        mButtonTakePicture = findViewById(R.id.fab_take_picture);
        mButtonTakePicture.setVisibility(View.GONE);
        setupToolbar();
        setToolbarTitle("Flowers");
    }

    private void setupData() {
        final AlertDialog alertDialogLoading = DialogBuilder.buildLoaingDialogWithSpinnerIcon(
                getCurrentContext(),
                false
        );

        RestRequest.makeGetRequest(
                getCurrentContext(),
                "GetFavoritesHistory",
                new RestRequest.IRequestResponse() {
                    @Override
                    public void onException() {
                        Toast.makeText(getCurrentContext(), "api calling threw an exception",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStatusOK(Response<String> result) {
                        Gson gson = new Gson();
                        final List<Flower> flowersList = new ArrayList<>();
                        flowersList.addAll(
                                (ArrayList<Flower>)(gson.fromJson(result.getResult(), new TypeToken<ArrayList<Flower>>() {
                                }.getType()))
                        );

                        mFlowerAdapter = new FlowerAdapter(flowersList, getCurrentContext());
                        mRecyclerView.setAdapter(mFlowerAdapter);
                        mFlowerAdapter.notifyDataSetChanged();

                        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getCurrentContext(), new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Flower flower = flowersList.get(position);
                                Intent intent = new Intent(getCurrentContext(), FlowerInfoActivity.class);
                                intent.putExtra("flower_info", flower);
                                getCurrentContext().startActivity(intent);
                            }
                        }));
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
