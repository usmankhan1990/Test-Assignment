package view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import activities.LoginSignUpScreen;
import co.sspp.library.SweetAlertDialog;

/**
 * Created by UsmanKhan on 9/16/18.
 */

public class UiView {

    private static final UiView ourInstance = new UiView();
    public static UiView getInstance() {
        return ourInstance;
    }

    public SweetAlertDialog showProgressBar(Context context){

        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Please wait");
        pDialog.setCancelable(true);
        pDialog.show();
        return pDialog;

    }

    public void dialogBox(final SweetAlertDialog dialogError, String message){
        dialogError.show();
        dialogError.setTitleText("Dear User").setContentText(message).show();
        dialogError.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                dialogError.dismiss();
            }
        });
    }

    public void showDialogConfirmationForLogout(final Activity activity){

        final  SweetAlertDialog pDialogConfirmation = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
        pDialogConfirmation.setTitleText("Dear User").setContentText("Do you want to logout from Assignment app?");
        pDialogConfirmation.setCancelable(false);
        pDialogConfirmation.showCancelButton(true);
        pDialogConfirmation.setConfirmText("Yes");
        pDialogConfirmation.setCancelText("No");
        pDialogConfirmation.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                sDialog.dismissWithAnimation();
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {

                        try {
                            Intent intent = new Intent(activity, LoginSignUpScreen.class);
                            activity.finish();
                            activity.startActivity(intent);

                        }catch (Exception exp){
                            exp.getMessage();
                        }
                    }
                });


            }
        });
        pDialogConfirmation.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialogConfirmation.dismiss();
            }
        });
//                    pDialogPopUp.setOnCancelListener(new );

        pDialogConfirmation.show();
    }
}
