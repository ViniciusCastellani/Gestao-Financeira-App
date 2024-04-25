package com.example.gestaofinanceiraapp;

import android.graphics.Color;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PieChartHelper {

    private PieChart pieChart;

    public PieChartHelper(PieChart pieChart) {
        this.pieChart = pieChart;
        configurarGrafico();
    }

    private void configurarGrafico() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(14f); // Aumenta o tamanho do texto da legenda
        l.setTextColor(Color.WHITE); // Define a cor do texto da legenda para branco
    }

    public void atualizarGrafico(ArrayList<FinancialData> financialDataList) {
        Map<String, Float> totalPorBanco = new HashMap<>();

        // Calcular o total de renda e despesas por banco
        for (FinancialData data : financialDataList) {
            String banco = data.getBank();
            float valor = data.getTipo().equals("Renda") ? data.getValue() : -data.getValue();
            totalPorBanco.put(banco, totalPorBanco.getOrDefault(banco, 0f) + valor);
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        // Calcular a porcentagem total por banco
        float totalGeral = 0;
        for (Float valor : totalPorBanco.values()) {
            totalGeral += valor;
        }
        for (String banco : totalPorBanco.keySet()) {
            float totalBanco = totalPorBanco.get(banco);
            float porcentagemBanco = (totalBanco / totalGeral) * 100;

            // Adicionar entrada para a porcentagem total do banco, incluindo o nome do banco
            entries.add(new PieEntry(porcentagemBanco, banco));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // Configuração de cores para os segmentos do gráfico
        ArrayList<Integer> colors = new ArrayList<>();
        // Usando cores diferentes para cada banco
        Set<String> bancos = totalPorBanco.keySet();
        int[] colorTemplate = ColorTemplate.VORDIPLOM_COLORS;
        for (int i = 0; i < bancos.size(); i++) {
            colors.add(colorTemplate[i % colorTemplate.length]);
        }
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f); // Aumenta o tamanho do texto da porcentagem
        data.setValueTextColor(Color.BLACK); // Define a cor do texto da porcentagem para preto

        pieChart.setData(data);
        pieChart.invalidate(); // Atualiza o gráfico
    }
}