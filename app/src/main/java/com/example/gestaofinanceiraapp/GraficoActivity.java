package com.example.gestaofinanceiraapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class GraficoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        inicializarGraficos();
    }

    private void inicializarGraficos() {
        LineChart lineChart = findViewById(R.id.lineChart);
        LineChartHelper lineChartHelper = new LineChartHelper(lineChart);

        // Preparando os dados para despesas e renda
        ArrayList<FinancialData> financialDataList = prepararDadosFinanceiros();

        // Atualizando o gráfico com os dados
        lineChartHelper.atualizarGrafico(financialDataList);

        // Inicializando e configurando o gráfico de pizza
        PieChart pieChart = findViewById(R.id.pieChart);
        PieChartHelper pieChartHelper = new PieChartHelper(pieChart);
        pieChartHelper.atualizarGrafico(financialDataList);
    }

    private ArrayList<FinancialData> prepararDadosFinanceiros() {
        ArrayList<FinancialData> financialDataList = new ArrayList<>();
        financialDataList.add(new FinancialData(1000, "Jan", "Banco XYZ", "Renda"));
        financialDataList.add(new FinancialData(1500, "Fev", "Banco XZ", "Renda"));
        financialDataList.add(new FinancialData(2000, "Mar", "Banco XYZ", "Renda"));
        // Adicionando dados de despesas
        financialDataList.add(new FinancialData(500, "Jan", "Banco XYZ", "Despesa"));
        financialDataList.add(new FinancialData(750, "Fev", "Banco XZ", "Despesa"));
        financialDataList.add(new FinancialData(1000, "Mar", "Banco XYZ", "Despesa"));
        return financialDataList;
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