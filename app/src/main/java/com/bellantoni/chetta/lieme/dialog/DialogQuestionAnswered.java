package com.bellantoni.chetta.lieme.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.ContactListFragment;
import com.bellantoni.chetta.lieme.NotificationFragment;
import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.bellantoni.chetta.lieme.generalclasses.Question;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * Created by Domenico on 22/07/2015.
 */
public class DialogQuestionAnswered extends DialogFragment {

    private ImageView imageViewProdileFriend;
    private ImageView resultAnswer;
    private Button okButton;
    private TextView question;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_question_answered, null);

        this.imageViewProdileFriend = (ImageView) view.findViewById(R.id.imageFriendQuestionResult);
        this.resultAnswer  = (ImageView) view.findViewById(R.id.resultAnswer);
        this.okButton = (Button) view.findViewById(R.id.okButtonReadQuestion);
        this.question = (TextView)view.findViewById(R.id.questionReadAnswer);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Question questionObj = NotificationFragment.findQuestionById(String.valueOf(getArguments().getInt("questionId")));
        //System.out.println("XXX" + String.valueOf(getArguments().getInt("questionId")));
        //immagine la ricavo dall'id della domanda, l'id della domanda lo ricavo come getArguments().getInt("questionId");
        Picasso.with(getActivity().getApplicationContext()).load("https://graph.facebook.com/" + questionObj.getReceiver_id() + "/picture?height=115&width=115").placeholder(R.mipmap.iconuseranonymous).transform(new CircleTransform()).fit().centerCrop().into(imageViewProdileFriend);

        //il riultato della domanda lo ricavo dall'id perchè faccio la query al db, per ora lo simulo con il numero casuale 0,1

        if(questionObj.getAnswer().equals("no")){
            this.resultAnswer.setImageResource(R.drawable.big_heart_red);

        }else{
            this.resultAnswer.setImageResource(R.drawable.big_heart_green);
        }

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.question.setText(questionObj.getMessage());


        return view;
    }


}
