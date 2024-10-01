package com.example.gestaofinanceiraapp.Pessoa;

import com.example.gestaofinanceiraapp.FluxoFinancas.FluxoFinanceiro;
import com.example.gestaofinanceiraapp.Meta.Meta;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pessoa {
    private int idPessoa;
    private String nome;
    private String email;
    private String senha;
    private double balancoGeral;
    private double todasFaturas;
    private List<FluxoFinanceiro> listaFluxoFinanceiro = new ArrayList<>();
    private List<Meta> listaMetas = new ArrayList<>();


    public String toJSON() throws Exception{
        JSONObject json = new JSONObject();
        JSONArray jsonArrayFluxo = new JSONArray();
        JSONArray jsonArrayMeta = new JSONArray();
        json.put("idPessoa", idPessoa);
        json.put("nome", nome);
        json.put("email", email);
        json.put("senha", senha);
        json.put("balancoGeral", balancoGeral);
        json.put("todasFaturas", todasFaturas);

        for (FluxoFinanceiro fluxo : listaFluxoFinanceiro) {
            jsonArrayFluxo.put(fluxo.toJSON());
        }

        for (Meta meta : listaMetas){
            jsonArrayMeta.put(meta.toJSON());
        }

        json.put("listaFluxoFinanceiro",jsonArrayFluxo);
        json.put("listaMetas",jsonArrayMeta);

        // Convert the JSONObject to a UTF-8 encoded string
        String jsonString = json.toString();
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        String utf8JsonString = new String(bytes, StandardCharsets.UTF_8);

        return utf8JsonString;
    }


    public void fromJSON(String json) throws Exception {
        // Ensure the input string is handled as UTF-8
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String utf8Json = new String(bytes, StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(utf8Json);
        this.idPessoa = obj.getInt("idPessoa");
        this.nome = obj.getString("nome");
        this.email = obj.getString("email");
        this.senha = obj.getString("senha");
        this.balancoGeral = obj.getDouble("balancoGeral");
        this.todasFaturas = obj.getDouble("todasFaturas");

        JSONArray listaFluxoFinanceiroJsonArray = obj.getJSONArray("listaFluxoFinanceiro");
        for (int i = 0; i < listaFluxoFinanceiroJsonArray.length(); i++) {
            JSONObject fluxoJsonObj = listaFluxoFinanceiroJsonArray.getJSONObject(i);
            FluxoFinanceiro fluxo = new FluxoFinanceiro();
            fluxo.fromJSON(fluxoJsonObj.toString());

            // Verificando se o ID do fluxo já existe na lista
            boolean idExists = false;
            for (FluxoFinanceiro existingFluxo : this.listaFluxoFinanceiro) {
                if (existingFluxo.getIdFluxo() == fluxo.getIdFluxo()) { // Assumindo que tem um método getId()
                    idExists = true;
                    break;
                }
            }

            // Se o ID não existir, adiciona o fluxo à lista
            if (!idExists) {
                this.listaFluxoFinanceiro.add(fluxo);
            }
        }

        // Deserializando a lista de Metas
        JSONArray listaMetasJsonArray = obj.getJSONArray("listaMetas");
        for (int i = 0; i < listaMetasJsonArray.length(); i++) {
            JSONObject metaObj = listaMetasJsonArray.getJSONObject(i);
            Meta meta = new Meta();
            meta.fromJSON(metaObj.toString());

            boolean metaExists = false;
            for (Meta existingMeta : this.listaMetas) {
                if (existingMeta.getIdMeta() == meta.getIdMeta()) {
                    metaExists = true;
                    break;
                }
            }

            // Se a meta não existir, adiciona-a à lista
            if (!metaExists) {
                this.listaMetas.add(meta);
            }
        }
    }
}