package bth.pa2555.agilegardeningapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
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
import java.util.Arrays;
import java.util.List;

import bth.pa2555.adapters.FlowerAdapter;
import bth.pa2555.helpers.AppCompatActivityBase;
import bth.pa2555.helpers.DialogBuilder;
import bth.pa2555.helpers.PreferencesHelper;
import bth.pa2555.helpers.RecyclerItemClickListener;
import bth.pa2555.helpers.RestRequest;
import bth.pa2555.models.Flower;
import pl.aprilapps.easyphotopicker.EasyImage;

public class FlowerListActivity extends AppCompatActivityBase implements RuntimePermissionListener {

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
        mButtonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
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
                "GetPlantsHistory",
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

    @AskPermission({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    private void openCamera() {
        EasyImage.openCamera(getCurrentContext(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_flower_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_favorite:
                startActivity(new Intent(getCurrentContext(), FlowersFavoritesList.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShowPermissionRationale(List<String> permissionList, RuntimePermissionRequest permissionRequest) {
        Toast.makeText(getCurrentContext(), "OnPermissionRational", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionDenied(List<DeniedPermission> deniedPermissionList) {
        Toast.makeText(getCurrentContext(), "OnPermissionDenied", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Let.handle(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(getCurrentContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                sendRequestToServer(imageFile);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                Toast.makeText(getCurrentContext(), "Image picking canceled", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendRequestToServer(File imageFile) {
        File compressedFile = createResizedImage(imageFile);
        if (compressedFile != null) {
            final AlertDialog alertDialogLoading = DialogBuilder.buildLoaingDialogWithSpinnerIcon(
                    getCurrentContext(),
                    false
            );
            alertDialogLoading.show();
            Ion.with(getCurrentContext())
                    .load(getString(R.string.request_endpoint) + "GetPlantByImage")
                    .setTimeout(120000)
                    .setHeader("auth_token", PreferencesHelper.getToken(getCurrentContext()))
                    .setMultipartFile("image", imageFile)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            alertDialogLoading.dismiss();
                            if (e != null)
                                Toast.makeText(getCurrentContext(), "api calling threw an exception : " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            else {
                                int resultCode = result.getHeaders().code();
                                if (resultCode == 200) {
                                    Gson gson = new Gson();
                                    Flower flower = gson.fromJson(result.getResult(), Flower.class);
                                    Intent intent = new Intent(getCurrentContext(), FlowerInfoActivity.class);
                                    intent.putExtra("flower_info", flower);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getCurrentContext(),
                                            "api failed with code : " + String.valueOf(resultCode),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
        else
            Toast.makeText(getCurrentContext(), "file is empty", Toast.LENGTH_LONG).show();
    }

    private File createResizedImage(File imageFile) {
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 1000, 1000, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapData = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(imageFile, false);
            fos.write(bitmapData);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return imageFile;
    }
}
