package com.bellantoni.chetta.lieme.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.NotificationFragment;
import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
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

        //immagine la ricavo dall'id della domanda, l'id della domanda lo ricavo come getArguments().getInt("questionId");
        Picasso.with(getActivity().getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").placeholder(R.mipmap.iconuseranonymous).transform(new CircleTransform()).fit().centerCrop().into(imageViewProdileFriend);
        //il riultato della domanda lo ricavo dall'id perchè faccio la query al db, per ora lo simulo con il numero casuale 0,1

        Random rand = new Random();
        int n = rand.nextInt(2);
        if(n==0){
            this.resultAnswer.setImageResource(R.drawable.big_heart_red);

        }else{
            this.resultAnswer.setImageResource(R.drawable.big_heart_green);
        }

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.question.setText("Here there should be the question made by me. The text is retrieved using the question id.");


        return view;
    }


}
