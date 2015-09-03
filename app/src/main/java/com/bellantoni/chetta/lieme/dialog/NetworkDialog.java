package com.bellantoni.chetta.lieme.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.bellantoni.chetta.lieme.R;

/**
 * Created by Domenico on 29/05/2015.
 */
public class NetworkDialog extends DialogFragment {

    public interface NetworkInfoInteface{

       void okInfoInterface();

    }

    private NetworkInfoInteface mNetworkInfoInterface;
    private DialogInterface.OnClickListener mOnclickListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof NetworkInfoInteface) {
            mNetworkInfoInterface = (NetworkInfoInteface) activity;
            mOnclickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Dialog.BUTTON_NEUTRAL:
                            mNetworkInfoInterface.okInfoInterface();
                            dismiss();
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
        // Here we create and return the AlertDialog using a Builder
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_network)
                .setTitle(R.string.check_connection).setNeutralButton(R.string.retry,mOnclickListener);
        this.setCancelable(false);
        return builder.create();
    }
}
