package com.example.gestaofinanceiraapp.Meta;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Meta {
    private int idMeta;
    private String tituloMeta;
    private String descricaoMeta;
    private double valorMeta;
    private String dataPrazo;

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("idMeta", idMeta);
        json.put("tituloMeta", tituloMeta);
        json.put("descricaoMeta", descricaoMeta);
        json.put("valorMeta", valorMeta);
        json.put("dataPrazo", dataPrazo);
        return json;
    }

    public void fromJSON(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        idMeta = jsonObject.getInt("idMeta");
        tituloMeta = jsonObject.getString("tituloMeta");
        descricaoMeta = jsonObject.getString("descricaoMeta");
        valorMeta = jsonObject.getDouble("valorMeta");
        dataPrazo = jsonObject.getString("dataPrazo");
    }
}
