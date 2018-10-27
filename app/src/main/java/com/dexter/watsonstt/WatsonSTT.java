package com.dexter.watsonstt;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.dto.SpeechConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class WatsonSTT extends Service implements ISpeechDelegate {

    private String mRecognitionResults = "";
    public static String MY_ACTION  = "MY_ACTION";
    private static final String URL = "wss://stream.watsonplatform.net/speech-to-text/api";
    public WatsonSTT() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SpeechToText.sharedInstance().initWithContext(
                URI.create(URL),
                this.getApplicationContext(),
                new SpeechConfiguration());

        SpeechToText.sharedInstance().setCredentials("59df2522-6f82-4230-8832-7e6cecd4b5e6", "B8pMEzFhR82o");
        SpeechToText.sharedInstance().setDelegate(this);
        SpeechToText.sharedInstance().setModel("en-US_BroadbandModel");
        SpeechToText.sharedInstance().recognize();

        return START_STICKY;
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onMessage(String s) {
        try {
            JSONObject jObj = new JSONObject(s);
            if (jObj.has("state")) {
           //     Log.d(TAG, "Status message: " + jObj.getString("state"));
            } else if (jObj.has("results")) {
            //    Log.d(TAG, "Results message: ");
                JSONArray jArr = jObj.getJSONArray("results");
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject obj = jArr.getJSONObject(i);
                    JSONArray jArr1 = obj.getJSONArray("alternatives");
                    String str = jArr1.getJSONObject(0).getString("transcript");
                    str = str.replaceAll("\\s+", "");    //  日本語に限定しているのでスペース補填
                    String strFormatted = Character.toUpperCase(str.charAt(0)) + str.substring(1);
                    if (obj.getString("final").equals("true")) {
                        String stopMarker = "。";
                        mRecognitionResults += strFormatted + stopMarker;
                        Intent intent = new Intent(MY_ACTION);

                        intent.putExtra("Status", mRecognitionResults);
                        sendBroadcast(intent);
                        Log.e("result", "" + mRecognitionResults);
                    } else {
                      //  displayResult(mRecognitionResults + strFormatted);
                    }
                    break;
                }
            } else {

            }

        } catch (JSONException e) {
           // Log.e(TAG, "Error parsing JSON");
            e.printStackTrace();
        }
    }

    @Override
    public void onAmplitude(double v, double v1) {

    }
}
