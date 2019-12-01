package com.shwetank.libraryassistant;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class AudioUtility {

    private static AudioUtility INSTANCE;
    private static boolean isPlaying = false;
    private Context context;
    private static TextToSpeech textToSpeech;

    private AudioUtility(Context context) {
        this.context = context;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                int i = textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
    }


    public static AudioUtility getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AudioUtility(context);
        }
        stopAudio();
        return INSTANCE;
    }

    public void startAudio(String value) {
        if (!isPlaying) {
            value = value.replaceAll("\\<.*?\\>", "");
            textToSpeech.speak(value, TextToSpeech.QUEUE_FLUSH, null);
            isPlaying = true;
        }
    }

    public static void stopAudio() {
        if (isPlaying) {
            textToSpeech.stop();
            isPlaying = false;
        }
    }

    public boolean isPlaying(){
        return isPlaying;
    }

}
