package com.bellantoni.chetta.lieme.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created by Domenico on 22/07/2015.
 */
public class QuestionDialog extends DialogFragment /*implements View.OnClickListener*/ {



    public interface QuestionInterface{
        void yesQuestionPressed(int idQuestion);
        void noQuestionPressed(int idQuestion);
    }

    private int idQuestion;

    private QuestionInterface mQuestionInterface;
    private DialogInterface.OnClickListener mOnclickListener;
    private Button yesButton, noButton;
    private ImageView profileImage;
    private TextView question;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_dialog_question, null);
        noButton = (Button)view.findViewById(R.id.noButtonAnswer);
        yesButton = (Button)view.findViewById(R.id.yesButtonAnswer);
        profileImage = (ImageView)view.findViewById(R.id.imgProfileQuestionSender);
        question = (TextView)view.findViewById(R.id.questionToAnswer);
        setCancelable(false);
        question.setText("here there should be the text of the question retrived using the id of the question, also the image on the left should be the image of" +
                "the friend that sent the question, the id of the question is " + String.valueOf(getArguments().getInt("questionId")));
        Picasso.with(getActivity().getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").placeholder(R.mipmap.iconuseranonymous).transform(new CircleTransform()).fit().centerCrop().into(profileImage);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mQuestionInterface.noQuestionPressed(getArguments().getInt("questionId"));


            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mQuestionInterface.yesQuestionPressed(getArguments().getInt("questionId"));
            }
        });

        getDialog().setTitle("Name and Surname asks you");
        return view;



    }



    /*@Override
    public void onClick(View v) {
        if(v.getId()==R.id.yesButtonAnswer){
            System.out.println("BELLAAAAAAA");
            dismiss();
            mQuestionInterface.yesQuestionPressed(getArguments().getInt("questionId"));

        }
        if(v.getId()==R.id.noButtonAnswer){

            dismiss();
            mQuestionInterface.noQuestionPressed(getArguments().getInt("questionId"));

        }
    }*/





    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof QuestionInterface) {
            mQuestionInterface = (QuestionInterface) activity;
            /*mOnclickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case R.id.yesButtonAnswer:
                            System.out.println("BELLAAAAAAA");
                            mQuestionInterface.yesQuestionPressed(getArguments().getInt("questionId"));
                            break;
                        case R.id.noButtonAnswer:
                            dismiss();
                            mQuestionInterface.noQuestionPressed(getArguments().getInt("questionId"));
                            break;
                        default:
                            break;
                    }

                }

            };*/

        }
    }

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Here we create and return the AlertDialog using a Builder
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.logo_mini_dialog)
                .setTitle("here there should be the text of the question retrived using the id of the question, also the image on the left should be the image of" +
                        "the friend that sent the question, the id of the question is " + String.valueOf(this.idQuestion))
                .setPositiveButton(R.string.yes,mOnclickListener)
                .setNegativeButton(R.string.no, mOnclickListener);

        return builder.create();
    }*/


}
