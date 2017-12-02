package bth.pa2555.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import bth.pa2555.agilegardeningapp.R;

public class RestRequest {

    public static void makeGetRequest(final Context context, String relativeUrl, final IRequestResponse response, final IBeforeAfterRequest beforeAfter) {
        beforeAfter.before();
        Ion.with(context)
                .load(context.getString(R.string.request_endpoint) + relativeUrl)
                .setHeader("Content-Type", "application/json")
                .addHeader("auth_token", PreferencesHelper.getToken(context))
                .setTimeout(20000)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        beforeAfter.after();
                        if (e != null)
                            response.onException();
                        else {
                            HeadersResponse header = result.getHeaders();
                            if (header.code() == 200) {
                                response.onStatusOK(result);
                            }
                            else {
                                response.onStatusNotOK(header);
                            }
                        }
                    }
                });
    }

    public static void makeDeleteRequest(final Context context, String relativeUrl, final IRequestResponse response, final IBeforeAfterRequest beforeAfter) {
        beforeAfter.before();
        Ion.with(context)
                .load("DELETE", context.getString(R.string.request_endpoint) + relativeUrl)
                .setHeader("Content-Type", "application/json")
                .addHeader("auth_token", PreferencesHelper.getToken(context))
                .setTimeout(20000)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        beforeAfter.after();
                        if (e != null)
                            response.onException();
                        else {
                            HeadersResponse header = result.getHeaders();
                            if (header.code() == 200) {
                                response.onStatusOK(result);
                            }
                            else {
                                response.onStatusNotOK(header);
                            }
                        }
                    }
                });
    }

    public static void makePostRequest(final Context context, String relativeUrl,
                                       String body, final IRequestResponse response, final IBeforeAfterRequest beforeAfter) {
        beforeAfter.before();
        Ion.with(context)
                .load(context.getString(R.string.request_endpoint) + relativeUrl)
                .setHeader("Content-Type", "application/json")
                .addHeader("auth_token", PreferencesHelper.getToken(context))
                .setTimeout(20000)
                .setStringBody(body)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        beforeAfter.after();
                        if (e != null)
                            response.onException();
                        else {
                            HeadersResponse header = result.getHeaders();
                            if (header.code() == 200) {
                                response.onStatusOK(result);
                            }
                            else {
                                response.onStatusNotOK(header);
                            }
                        }
                    }
                });
    }

    public static void makePostRequest(final Context context, String relativeUrl,
                                       JsonObject body, final IRequestResponse response, final IBeforeAfterRequest beforeAfter) {
        beforeAfter.before();
        Ion.with(context)
                .load(context.getString(R.string.request_endpoint) + relativeUrl)
                .setHeader("Content-Type", "application/json")
                .addHeader("auth_token", PreferencesHelper.getToken(context))
                .setTimeout(20000)
                .setJsonObjectBody(body)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        beforeAfter.after();
                        if (e != null)
                            response.onException();
                        else {
                            HeadersResponse header = result.getHeaders();
                            if (header.code() == 200) {
                                response.onStatusOK(result);
                            }
                            else {
                                response.onStatusNotOK(header);
                            }
                        }
                    }
                });
    }

    public static void makePostRequest(final Context context, String relativeUrl,
                                       JsonArray body, final IRequestResponse response, final IBeforeAfterRequest beforeAfter) {
        beforeAfter.before();
        Ion.with(context)
                .load(context.getString(R.string.request_endpoint) + relativeUrl)
                .setHeader("Content-Type", "application/json")
                .addHeader("auth_token", PreferencesHelper.getToken(context))
                .setTimeout(20000)
                .setJsonArrayBody(body)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        beforeAfter.after();
                        if (e != null)
                            response.onException();
                        else {
                            HeadersResponse header = result.getHeaders();
                            if (header.code() == 200) {
                                response.onStatusOK(result);
                            }
                            else {
                                response.onStatusNotOK(header);
                            }
                        }
                    }
                });
    }

    public interface IBeforeAfterRequest {
        void before();
        void after();
    }

    public interface IRequestResponse {
        void onException();
        void onStatusOK(Response<String> result);
        void onStatusNotOK(HeadersResponse header);
    }
}
