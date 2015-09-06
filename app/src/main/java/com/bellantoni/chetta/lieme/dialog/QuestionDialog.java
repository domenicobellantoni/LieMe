package com.bellantoni.chetta.lieme.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.ContactListFragment;
import com.bellantoni.chetta.lieme.NotificationFragment;
import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.bellantoni.chetta.lieme.generalclasses.Question;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

/**
 * Created by Domenico on 22/07/2015.
 */
public class QuestionDialog extends DialogFragment /*implements View.OnClickListener*/ {



    public interface QuestionInterface{
        void yesQuestionPressed(int idQuestion,String senderId);
        void noQuestionPressed(int idQuestion,String senderId);
    }
    public interface updateNotificationInterface{
        public void updateNotifications();
    }
    private int idQuestion;

    private QuestionInterface mQuestionInterface;
    private DialogInterface.OnClickListener mOnclickListener;
    private Button yesButton, noButton;
    private ImageView profileImage;
    private TextView question;
    private Question questionObj;
    private Contact senderContact;

    /**
     * Tag used on log messages.
     */
    static final String TAG = "Question Dialog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_dialog_question, null);
        noButton = (Button)view.findViewById(R.id.noButtonAnswer);
        yesButton = (Button)view.findViewById(R.id.yesButtonAnswer);
        profileImage = (ImageView)view.findViewById(R.id.imgProfileQuestionSender);
        question = (TextView)view.findViewById(R.id.questionToAnswer);
        setCancelable(false);
        questionObj = NotificationFragment.findQuestionById(String.valueOf(getArguments().getInt("questionId")));
        if(questionObj==null)
        {
            Log.i(TAG, "Error: question id not found");
            return null;
        }



        question.setText(questionObj.getMessage());
        Picasso.with(getActivity().getApplicationContext()).load("https://graph.facebook.com/" + questionObj.getSender_id() + "/picture?height=115&width=115").placeholder(R.mipmap.iconuseranonymous).transform(new CircleTransform()).fit().centerCrop().into(profileImage);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mQuestionInterface.noQuestionPressed(getArguments().getInt("questionId"), questionObj.getSender_id());


            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mQuestionInterface.yesQuestionPressed(getArguments().getInt("questionId"), questionObj.getSender_id());
            }
        });


        senderContact = ContactListFragment.findContactById(questionObj.getSender_id());
        if(senderContact == null)
        {
            Log.i(TAG, "Error: sender id not found");
            getDialog().setTitle("Sender name not found");
        }else{
            getDialog().setTitle(senderContact.getName());
        }


        return view;

    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof QuestionInterface) {
            mQuestionInterface = (QuestionInterface) activity;

        }
    }




}
