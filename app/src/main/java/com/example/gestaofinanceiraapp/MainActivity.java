package com.example.gestaofinanceiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mudarTelaMain(View v){
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }

    public void mudarTelaGrafico(View v){
        Intent intentMain = new Intent(this, GraficoActivity.class);
        startActivity(intentMain);
    }

    public void mudarTelaMetas(View v){
        Intent intentMain = new Intent(this, MetasActivity.class);
        startActivity(intentMain);
    }

    public void mudarTelaConfiguracao(View v){
        Intent intentMain = new Intent(this, ConfiguracoesActivity.class);
        startActivity(intentMain);
    }
}