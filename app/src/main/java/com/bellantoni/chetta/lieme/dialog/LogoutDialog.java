package com.bellantoni.chetta.lieme.dialog;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.bellantoni.chetta.lieme.R;


public class LogoutDialog extends DialogFragment {

    public interface LogoutInterface{

        void yesPressed();
        void noPressed();

    }
    private LogoutInterface mlogoutInterface;
    private DialogInterface.OnClickListener mOnclickListener;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof LogoutInterface) {
            mlogoutInterface = (LogoutInterface) activity;
            mOnclickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Dialog.BUTTON_POSITIVE:
                            mlogoutInterface.yesPressed();
                            break;
                        case Dialog.BUTTON_NEGATIVE:
                            mlogoutInterface.noPressed();
                            break;
                        default:
                            break;
                    }

                }

            };

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.logo_mini_dialog)
                .setTitle(R.string.dialog_logout)
                .setPositiveButton(R.string.yes,mOnclickListener)
                .setNegativeButton(R.string.no, mOnclickListener);

        return builder.create();
    }


}