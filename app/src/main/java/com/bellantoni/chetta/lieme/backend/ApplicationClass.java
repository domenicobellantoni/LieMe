package com.bellantoni.chetta.lieme.backend;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Domenico on 19/05/2015.
 */
public class ApplicationClass extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        printHash();
    }

    private void printHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.bellantoni.chetta.lieme",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("!!!!!!!IL MIO HASH E':", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
