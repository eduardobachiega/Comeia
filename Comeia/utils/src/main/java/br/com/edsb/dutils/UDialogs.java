package br.com.edsb.dutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by eduardo on 14/06/16.
 */
public class UDialogs {
    static ProgressDialog pDialog;
    static boolean isShowing = false;

    public static void showPopUpMessage(Context context, String title, String message, String buttonText, boolean cancelable, int dialogStyleResource, DialogInterface.OnClickListener buttonAction) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(context, dialogStyleResource);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setPositiveButton(buttonText, buttonAction);
        dialog.show();
    }

    public static void showOkCancelPopUp(Context context, String title, String message, String positiveButtonText, String negativeButtonText, boolean cancelable, int dialogStyleResource, DialogInterface.OnClickListener positiveButtonAction, DialogInterface.OnClickListener negativeButtonAction) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(context, dialogStyleResource);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setPositiveButton(positiveButtonText, positiveButtonAction);
        dialog.setNegativeButton(negativeButtonText, negativeButtonAction);
        dialog.show();
    }

    public static void showProgressDialog(Context context, String dialogMessage, boolean cancelable, int dialogStyleResource) {
        pDialog = new ProgressDialog(context, dialogStyleResource);
        pDialog.setIndeterminate(false);
        pDialog.setMessage(dialogMessage);
        pDialog.setCancelable(cancelable);
        pDialog.show();
        isShowing = true;
    }

    public static void dismissProgressDialog() {
        pDialog.dismiss();
        isShowing = false;
    }

    public boolean isShowingDialog() {
        return isShowing;
    }

    public static void showListPopUp(Context context, String[] optionsList, String dialogTitle, int dialogStyleResource, DialogInterface.OnClickListener itemClick) {
        final CharSequence op[] = optionsList;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, dialogStyleResource);
        builder.setTitle(dialogTitle);
        builder.setItems(op, itemClick);
        builder.show();
    }

    public static void showListPopUpWithCancel(Context context, String[] optionsList, String dialogTitle, int dialogStyleResource, String negativeButtonTitle, DialogInterface.OnClickListener negativeButtonClick, DialogInterface.OnClickListener itemClick) {
        final CharSequence op[] = optionsList;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, dialogStyleResource);
        builder.setTitle(dialogTitle);
        builder.setItems(op, itemClick);
        builder.setNegativeButton(negativeButtonTitle, negativeButtonClick);
        builder.show();
    }

    public static void showListPopUpMultiChoice(Context context, String[] optionsList, String dialogTitle, boolean cancelable, int dialogStyleResource, String positiveButtonTitle, DialogInterface.OnClickListener positiveButtonClick, DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener) {
        final CharSequence op[] = optionsList;
        boolean[] checked = new boolean[op.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(context, dialogStyleResource);
        builder.setTitle(dialogTitle);
        builder.setCancelable(cancelable);
        builder.setMultiChoiceItems(op, checked, multiChoiceClickListener);
        builder.setPositiveButton(positiveButtonTitle, positiveButtonClick);
        builder.show();
    }

    public static void showListPopUpMultiChoiceWithCancel(Context context, String[] optionsList, String dialogTitle, boolean cancelable, int dialogStyleResource, String positiveButtonTitle, String negativeButtonTitle, DialogInterface.OnClickListener positiveButtonClick, DialogInterface.OnClickListener negativeButtonClick, DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener) {
        final CharSequence op[] = optionsList;
        boolean[] checked = new boolean[op.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(context, dialogStyleResource);
        builder.setTitle(dialogTitle);
        builder.setCancelable(cancelable);
        builder.setMultiChoiceItems(op, checked, multiChoiceClickListener);
        builder.setPositiveButton(positiveButtonTitle, positiveButtonClick);
        builder.setNegativeButton(negativeButtonTitle, negativeButtonClick);
        builder.show();
    }

    public static void showSnack(String message, View parentLayout, int lenght) {
        Snackbar.make(parentLayout, message, lenght).show();
    }

    public static void showSnackWithAction(String message, View parentLayout, int lenght, String buttonTitle, int buttonTextColor, View.OnClickListener buttonClick) {
        Snackbar.make(parentLayout, message, lenght).setAction(buttonTitle, buttonClick).setActionTextColor(buttonTextColor).show();
    }

    public static void showSnackWithCallback(String message, View parentLayout, int lenght, BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        Snackbar.make(parentLayout, message, lenght).addCallback(callback).show();
    }

    public static void showSnackWithActionAndCallback(String message, View parentLayout, int lenght, String buttonTitle, int buttonTextColor, View.OnClickListener buttonClick, BaseTransientBottomBar.BaseCallback<Snackbar> callback) {
        Snackbar.make(parentLayout, message, lenght).setAction(buttonTitle, buttonClick).setActionTextColor(buttonTextColor).addCallback(callback).show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(String color, Activity a) {
        Window window = a.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#" + color));
    }

    @TargetApi(21)
    public static RippleDrawable setRipple(int normalColor, int pressedColor) {
        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), getColorDrawableFromColor(normalColor), null);
    }

    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_activated},
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor,
                                pressedColor,
                                pressedColor,
                                normalColor
                        }
        );
    }

    public static ColorDrawable getColorDrawableFromColor(int color) {
        return new ColorDrawable(color);
    }


}
