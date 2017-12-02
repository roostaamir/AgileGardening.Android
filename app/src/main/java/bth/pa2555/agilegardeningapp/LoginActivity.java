package bth.pa2555.agilegardeningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Response;

import java.util.HashMap;

import bth.pa2555.helpers.AppCompatActivityBase;
import bth.pa2555.helpers.DialogBuilder;
import bth.pa2555.helpers.FastJsonCreator;
import bth.pa2555.helpers.PreferencesHelper;
import bth.pa2555.helpers.RestRequest;


public class LoginActivity extends AppCompatActivityBase {
    private AlertDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
    }

    private void initUI() {
        mLoadingDialog = DialogBuilder.buildLoaingDialogWithSpinnerIcon(
                getCurrentContext(),
                false
        );

        final EditText editTextEmail = findViewById(R.id.editText_email);
        final EditText editTextPassword = findViewById(R.id.editText_password);
        Button buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().isEmpty()
                        || editTextPassword.getText().toString().isEmpty())
                    Toast.makeText(getCurrentContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                else {
                    RestRequest.makePostRequest(
                            getCurrentContext(),
                            "Login/",
                            FastJsonCreator.create(new HashMap<String, String>() {{
                                put("email", editTextEmail.getText().toString());
                                put("password", editTextPassword.getText().toString());
                            }}),
                            new RestRequest.IRequestResponse() {
                                @Override
                                public void onException() {
                                    Toast.makeText(getCurrentContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onStatusOK(Response<String> result) {
                                    JsonParser parser = new JsonParser();
                                    JsonObject rootObject = parser.parse(result.getResult()).getAsJsonObject();
                                    String token = rootObject.get("token").getAsString();
                                    PreferencesHelper.setToken(getCurrentContext(), token);

                                    startActivity(new Intent(getCurrentContext(), FlowerListActivity.class));
                                    finish();
                                }

                                @Override
                                public void onStatusNotOK(HeadersResponse header) {
                                    Toast.makeText(getCurrentContext(),
                                            "Something went wrong from server with code " + header.code(),
                                            Toast.LENGTH_LONG).show();
                                }
                            },
                            new RestRequest.IBeforeAfterRequest() {
                                @Override
                                public void before() {
                                    mLoadingDialog.show();
                                }

                                @Override
                                public void after() {
                                    mLoadingDialog.dismiss();
                                }
                            }
                    );
                }
            }
        });
    }
}
