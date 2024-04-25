package com.example.gestaofinanceiraapp;

public class FinancialData {
    private float value;
    private String month;
    private String bank;
    private String tipo; // Despesa ou Renda

    public FinancialData(float value, String month, String bank, String tipo) {
        this.value = value;
        this.month = month;
        this.bank = bank;
        this.tipo = tipo;
    }

    // Getters e Setters
    public float getValue() {
        return value;
    }

    public String getMonth() {
        return month;
    }

    public String getBank() {
        return bank;
    }

    public String getTipo() {
        return tipo;
    }
}
