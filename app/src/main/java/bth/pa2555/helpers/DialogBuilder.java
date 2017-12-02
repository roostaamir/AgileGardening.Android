package bth.pa2555.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import bth.pa2555.agilegardeningapp.R;

public class DialogBuilder {

    public static AlertDialog buildLoaingDialogWithSpinnerIcon(Context context, boolean isCancelable) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_spinner_loading, null);
        ImageView spinnerIcon = dialogView.findViewById(R.id.imageView_spinner);
        Animation rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        rotateAnimation.setFillAfter(true);
        spinnerIcon.startAnimation(rotateAnimation);

        dialog.setView(dialogView);
        dialog.setCancelable(isCancelable);
        return dialog.create();
    }

    public static AlertDialog buildLoadingDialog(Context context, String message, boolean isCancelable) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        ((TextView) dialogView.findViewById(R.id.dialog_text)).setText(message);
        dialog.setView(dialogView);
        dialog.setCancelable(isCancelable);
        return dialog.create();
    }

    public static AlertDialog buildCustomLayoutDialogWithoutListener(Context context, boolean isCancelable,
                                                                     View layoutView) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(isCancelable);
        return dialogBuilder.create();
    }

    public static AlertDialog buildCustomLayoutDialogWithoutListener(Context context, boolean isCancelable,
                                                                     String positiveText, String negativeText, View layoutView) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(isCancelable);
        dialogBuilder.setPositiveButton(positiveText, null);
        dialogBuilder.setNegativeButton(negativeText, null);
        return dialogBuilder.create();
    }

    public static AlertDialog buildCustomLayoutDialogWithoutListener(Context context, boolean isCancelable,
                                                                     String positiveText, String negativeText, String neutralText,
                                                                     View layoutView) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(isCancelable);
        dialogBuilder.setPositiveButton(positiveText, null);
        dialogBuilder.setNegativeButton(negativeText, null);
        dialogBuilder.setNeutralButton(neutralText, null);
        return dialogBuilder.create();
    }
}
