package com.bellantoni.chetta.lieme.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.bellantoni.chetta.lieme.R;

/**
 * Created by Domenico on 07/09/2015.
 */
public class BluetoothDialogError extends DialogFragment {


    private Button okButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_bluetooth_error, null);


        this.okButton = (Button) view.findViewById(R.id.okButtonReadQuestion);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getDialog().setTitle("No Heart Rate Monitor connected");
        return view;
    }

}
