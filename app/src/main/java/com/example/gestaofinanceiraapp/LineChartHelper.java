package com.example.gestaofinanceiraapp;

import android.graphics.Color;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;

public class LineChartHelper {

    private LineChart lineChart;

    public LineChartHelper(LineChart lineChart) {
        this.lineChart = lineChart;
        configurarGrafico();
    }

    private void configurarGrafico() {
        // Configuração do eixo X
        XAxis xAxis = lineChart.getXAxis();
        configurarEixoXAxis(xAxis, Color.WHITE, 14f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Array de abreviações dos nomes dos meses
                String[] monthNames = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
                int monthIndex = (int) value - 1; // Ajuste para o índice do array (0-11)
                if (monthIndex >= 0 && monthIndex < monthNames.length) {
                    return monthNames[monthIndex];
                } else {
                    return ""; // Retorna uma string vazia ou um valor padrão para mês não reconhecido
                }
            }
        });

        // Configuração do eixo Y
        YAxis leftAxis = lineChart.getAxisLeft();
        configurarEixoYAxis(leftAxis, Color.WHITE, 14f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = lineChart.getAxisRight();
        configurarEixoYAxis(rightAxis, Color.WHITE, 14f);
        rightAxis.setEnabled(false); // Desativa o eixo Y direito

        // Configuração da legenda
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE); // Texto da legenda branco
        legend.setTextSize(14f); // Aumenta o tamanho do texto da legenda
        legend.setForm(Legend.LegendForm.LINE);

        // Remover o "description label"
        lineChart.setDescription(null);
    }

    private void configurarEixoYAxis(YAxis eixo, int textColor, float textSize) {
        eixo.setTextColor(textColor);
        eixo.setTextSize(textSize);
    }

    private void configurarEixoXAxis(XAxis eixo, int textColor, float textSize) {
        eixo.setTextColor(textColor);
        eixo.setTextSize(textSize);
    }

    public void atualizarGrafico(ArrayList<FinancialData> financialDataList) {
        ArrayList<Entry> entriesRenda = new ArrayList<>();
        ArrayList<Entry> entriesDespesas = new ArrayList<>();

        for (FinancialData data : financialDataList) {
            float monthValue = getMonthValue(data.getMonth()); // Converte o nome do mês em um valor numérico
            if (data.getTipo().equals("Renda")) {
                entriesRenda.add(new Entry(monthValue, data.getValue()));
            } else if (data.getTipo().equals("Despesa")) {
                entriesDespesas.add(new Entry(monthValue, data.getValue()));
            }
        }

        LineDataSet dataSetRenda = criarDataSet("Renda", Color.GREEN, entriesRenda);
        LineDataSet dataSetDespesas = criarDataSet("Despesas", Color.RED, entriesDespesas);

        LineData lineData = new LineData(dataSetRenda, dataSetDespesas);
        lineChart.setData(lineData);
        lineChart.invalidate(); // Atualiza o gráfico
    }

    private float getMonthValue(String monthName) {
        // Array de abreviações dos nomes dos meses
        String[] monthNames = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        for (int i = 0; i < monthNames.length; i++) {
            if (monthNames[i].equalsIgnoreCase(monthName)) {
                return i + 1; // Retorna o valor numérico correspondente ao mês
            }
        }
        return 0; // Retorna 0 se o mês não for encontrado
    }

    private LineDataSet criarDataSet(String label, int color, ArrayList<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(color);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);
        return dataSet;
    }
}