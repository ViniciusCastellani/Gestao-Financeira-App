package com.example.gestaofinanceiraapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestaofinanceiraapp.Pessoa.Pessoa;
import com.example.gestaofinanceiraapp.R;

public class MainActivity extends AppCompatActivity {
    private Pessoa p = new Pessoa();
    private String pessoaJson;
    private TextView nomeUsuario, valorSaldoGeral, valorTodasFaturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the JSON string from the Intent extras
        pessoaJson = getIntent().getStringExtra("pessoaJson");

        // Log the received JSON string to verify its content
        assert pessoaJson != null;
        Log.d("ReceivedJSON", pessoaJson);

        deserializarJson(pessoaJson);
        inicializarViews();
        atualizarTela();
    }

    private void inicializarViews() {
        nomeUsuario = findViewById(R.id.textViewUsuario);
        valorSaldoGeral = findViewById(R.id.textViewSaldoGeralValor);
        valorTodasFaturas = findViewById(R.id.textViewTodasFaturasValor);
    }

    private void atualizarTela() {
        nomeUsuario.setText(p.getNome());
        valorSaldoGeral.setText(String.format("R$ %.2f", p.getBalancoGeral()));
        valorTodasFaturas.setText(String.format("R$ %.2f", p.getTodasFaturas()));
    }

    private void deserializarJson(String json) {
        try {
            pessoaJson = json;
            this.p.fromJSON(json);
            Log.d("DeserializedName", p.getNome());  // Log deserialized name
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mudarTelaGrafico(View v){
        Intent intentGrafico = new Intent(MainActivity.this, GraficoActivity.class);
        intentGrafico.putExtra("pessoaJson", pessoaJson);
        startActivity(intentGrafico);
    }

    public void mudarTelaMetas(View v){
        Intent intentMetas = new Intent(MainActivity.this, MetasActivity.class);
        intentMetas.putExtra("pessoaJson", pessoaJson);
        startActivity(intentMetas);
    }

    public void mudarTelaConfiguracao(View v){
        Intent intentConfiguracao = new Intent(MainActivity.this, ConfiguracoesActivity.class);
        intentConfiguracao.putExtra("pessoaJson", pessoaJson);
        startActivity(intentConfiguracao);
    }
}