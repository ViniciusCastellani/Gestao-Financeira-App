package com.example.gestaofinanceiraapp;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestaofinanceiraapp.Pessoa.Pessoa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComunicacaoServer {
    private boolean respostaPost = true;
    private boolean respostaPut = true;
    private boolean respostaGet = true;
    private List<Pessoa> listaDePessoas = new ArrayList<>();
    private String novoJsonPessoa = "";

    public Boolean enviarJSON(String jsonPessoa, String url, Context context) {
        if (jsonPessoa.isEmpty()) {
            respostaPost = false;
            return respostaPost;
        }

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            enviarVolleyJson(url, jsonPessoa, requestQueue);
        } catch (Exception ex) {
            respostaPost = false;
            return respostaPost;
        }
        return respostaPost;
    }

    private void enviarVolleyJson(String url, String jsonPessoa, RequestQueue requestQueue) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, s -> tratarOKPOST(s),
                s -> tratarErroPOST(s)) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonPessoa.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private void tratarErroPOST(VolleyError s) {
        NetworkResponse networkResponse = s.networkResponse;
        if (networkResponse!= null) {
            respostaPost = false;
        } else {
            respostaPost = false;
        }
    }

    private void tratarOKPOST(String json) {
        respostaPost = true;
    }

    public void listarTodasPessoasJSON(String url, Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> tratarOKListarPessoas(response),
                error -> tratarErroListarPessoas(error));

        requestQueue.add(stringRequest);
    }

    private void tratarErroListarPessoas(VolleyError error) {
        respostaGet = false;
    }

    private void tratarOKListarPessoas(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            this.listaDePessoas.clear(); // Limpa a lista anterior
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Pessoa pessoa = new Pessoa();
                try {
                    pessoa.fromJSON(jsonObject.toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                listaDePessoas.add(pessoa);
            }
            respostaGet = true;
        } catch (JSONException e) {
            respostaGet = false;
        }
    }

    public void atualizarVolleyJson(String url, String jsonPessoa, Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, s -> tratarOKPUT(s),
                s -> tratarErroPUT(s)) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return jsonPessoa.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        requestQueue.add(stringRequest);
    }

    private void tratarErroPUT(VolleyError s) {
        NetworkResponse networkResponse = s.networkResponse;
        if (networkResponse!= null) {
            String responseBody = new String(networkResponse.data, Charset.forName("UTF-8"));
            respostaPut = false;
        } else {
            respostaPut = false;
        }
    }

    private void tratarOKPUT(String json) {
        respostaPut = true;
        novoJsonPessoa = json;
    }
}
