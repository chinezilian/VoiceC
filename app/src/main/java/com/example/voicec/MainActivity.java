package com.example.voicec;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_COD_INPUT =1000;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
         editText= findViewById(R.id.edit_text);
        final Button button;
         button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();
            }
        });
    }
    private void inicio() {
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pode dizer:");
        try {
            startActivityForResult(mSpeechRecognizerIntent, REQ_COD_INPUT);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "O celular não suporta voz",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int id, int resultCode, Intent dados){
        super.onActivityResult(id,resultCode,dados);
        switch (id)
        {
            case REQ_COD_INPUT:
                if(resultCode==RESULT_OK&&null != dados) {
                    ArrayList<String> result = dados.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String EntradaVoz,saida;
                    EntradaVoz = result.get(0);
                    saida=comparator(EntradaVoz);
                    editText.setText(saida);

                }
                    break;

        }

    }
    String comparator(String fala) {
        String listCommand[] = {"frente", "vira direita", "vira esquerda", "atrás"};
        for (int i = 0; i <= listCommand.length - 1; i++) {
            if (listCommand[i].equalsIgnoreCase(fala)) {
                Toast.makeText(getApplicationContext(),fala,Toast.LENGTH_SHORT).show();
                break;
            } else if (listCommand.length - 1 == i) {
                Toast.makeText(getApplicationContext(),"Comando invalido,tente novamente",Toast.LENGTH_SHORT).show();
                fala=null;
            }
        }
        return fala;

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

}
