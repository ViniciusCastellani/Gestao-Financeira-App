package com.example.gestaofinanceiraapp.FluxoFinancas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinancialData {
    private String tipo;
    private String descricao;
    private String nomeBanco;
    private double valor;
    private String data; // Despesa ou Renda
}
