package com.example.gestaofinanceiraapp.FluxoFinancas;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class FluxoFinanceiro {
    private int idFluxo;
    private String tipo;
    private String descricao;
    private String nomeBanco;
    private double valor;
    private String data;


    public JSONObject toJSON() throws Exception{
        JSONObject json = new JSONObject();
        json.put("idFluxo", idFluxo);
        json.put("tipo", tipo);
        json.put("descricao", descricao);
        json.put("nomeBanco", nomeBanco);
        json.put("valor", valor);
        json.put("data", data);
        return json;
    }


    public void fromJSON(String json) throws Exception{
        JSONObject obj = new JSONObject(json);
        idFluxo = obj.getInt("idFluxo");
        tipo = obj.getString("tipo");
        descricao = obj.getString("descricao");
        nomeBanco = obj.getString("nomeBanco");
        valor = obj.getDouble("valor");
        data = obj.getString("data");
    }
}
