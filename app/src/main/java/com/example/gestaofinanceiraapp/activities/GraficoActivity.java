package com.example.gestaofinanceiraapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestaofinanceiraapp.FinancialData;
import com.example.gestaofinanceiraapp.FluxoFinanceiro;
import com.example.gestaofinanceiraapp.LineChartHelper;
import com.example.gestaofinanceiraapp.Pessoa;
import com.example.gestaofinanceiraapp.PieChartHelper;
import com.example.gestaofinanceiraapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class GraficoActivity extends AppCompatActivity {
    private Pessoa p = new Pessoa();
    private String pessoaJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);
        pessoaJson = getIntent().getStringExtra("pessoaJson");
        deserializarJson(pessoaJson);
        inicializarGraficos();
    }

    private void deserializarJson(String json) {
        try {
            System.out.println("JSON: " + json);
            pessoaJson = json;
            this.p.fromJSON(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void inicializarGraficos() {
        LineChart lineChart = findViewById(R.id.lineChartDespesasRenda);
        LineChartHelper lineChartHelper = new LineChartHelper(lineChart);

        // Preparando os dados para despesas e renda
        ArrayList<FinancialData> financialDataList = prepararDadosFinanceiros();

        if (!financialDataList.isEmpty()) {
            // Atualizando o gráfico com os dados
            lineChartHelper.atualizarGrafico(financialDataList);
        }

        // Inicializando e configurando o gráfico de pizza
        PieChart pieChart = findViewById(R.id.pieChartBancos);
        PieChartHelper pieChartHelper = new PieChartHelper(pieChart);
        pieChartHelper.atualizarGrafico(financialDataList);
    }

    private ArrayList<FinancialData> prepararDadosFinanceiros() {
        ArrayList<FinancialData> financialDataList = new ArrayList<>();

        if (p.getListaFluxoFinanceiro() != null){
            for (FluxoFinanceiro fluxo : p.getListaFluxoFinanceiro()) {
                // Aqui você pode adicionar lógica para agrupar os dados por mês, se necessário
                financialDataList.add(new FinancialData(fluxo.getTipo(), fluxo.getDescricao(), fluxo.getNomeBanco(), fluxo.getValor(), fluxo.getData()));
            }
        }
        return financialDataList;
    }

    public void mudarTelaMain(View v){
        Intent intentMain = new Intent(GraficoActivity.this, MainActivity.class);
        intentMain.putExtra("pessoaJson", pessoaJson);
        startActivity(intentMain);
    }

    public void mudarTelaMetas(View v){
        Intent intentMetas = new Intent(GraficoActivity.this, MetasActivity.class);
        intentMetas.putExtra("pessoaJson", pessoaJson);
        startActivity(intentMetas);
    }

    public void mudarTelaConfiguracao(View v){
        Intent intentConfiguracao = new Intent(GraficoActivity.this, ConfiguracoesActivity.class);
        intentConfiguracao.putExtra("pessoaJson", pessoaJson);
        startActivity(intentConfiguracao);
    }
}