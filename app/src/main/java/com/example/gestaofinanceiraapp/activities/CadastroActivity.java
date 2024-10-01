package com.example.gestaofinanceiraapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestaofinanceiraapp.ComunicacaoServer;
import com.example.gestaofinanceiraapp.OnClickShowText;
import com.example.gestaofinanceiraapp.Pessoa.Pessoa;
import com.example.gestaofinanceiraapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CadastroActivity extends AppCompatActivity {
    private EditText nomeUsuarioET, emailUsuarioET, senhaUsuarioET, confirmarSenhaUsuarioET;
    private String nomeUsuario, emailUsuario, senhaUsuario, confirmarSenhaUsuario;
    private List<Pessoa> listaDePessoas = new ArrayList<>();
    private ComunicacaoServer cs = new ComunicacaoServer();
    private String jsonPessoa;
    private int idPessoa;
    private OnClickShowText onClickSenha;
    private OnClickShowText onClickConfirmarSenha;
    private static final int DURACAO_TOAST = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        listarTodasPessoasJSON();
        inicializarViews();
    }

    public void cadastrarUsuario(View v) {
        nomeUsuario = nomeUsuarioET.getText().toString().trim();
        emailUsuario = emailUsuarioET.getText().toString().trim();
        senhaUsuario = senhaUsuarioET.getText().toString().trim();
        confirmarSenhaUsuario = confirmarSenhaUsuarioET.getText().toString().trim();

        listarTodasPessoasJSON();

        if (!validarSenha(senhaUsuario, confirmarSenhaUsuario)) return;
        if (verificarCamposVazios(nomeUsuario, emailUsuario, senhaUsuario, confirmarSenhaUsuario)) return;
        if (verificaEmailExistente(emailUsuario)) {
            exibirMensagemToast("Email já está em uso");
            return;
        }
        try {
            jsonPessoa = gerarJson(nomeUsuario, emailUsuario, senhaUsuario);
            System.out.println(jsonPessoa);
            String url = "http://192.168.0.16:8081/pessoa";
            cs.enviarJSON(jsonPessoa, url, this);

            if (!cs.isRespostaPost()) {
                exibirMensagemToast("Erro ao cadastrar no servidor");
                return;
            } else {
                exibirMensagemToast("Cadastro efetuado com sucesso");
                voltarParaLoginActivity(emailUsuario);
            }

        } catch (Exception e) {
            e.printStackTrace();
            exibirMensagemToast("Erro ao enviar dados: " + e.getMessage());
        }
    }

    public void listarTodasPessoasJSON() {
        listaDePessoas.clear();
        String url = "http://192.168.0.16:8081/pessoa/listar";
        cs.listarTodasPessoasJSON(url, this);
        if (cs.isRespostaGet()) {
            for (Pessoa pessoa : cs.getListaDePessoas()){
                listaDePessoas.add(pessoa);
            }
       }
    }

    private String gerarJson(String nomeUsuario, String emailUsuario, String senhaUsuario) {
        try {
            idPessoa = listaDePessoas.size() + 1;
            JSONObject json = new JSONObject();
            JSONArray jsonArrayFluxo = new JSONArray();
            JSONArray jsonArrayMeta = new JSONArray();
            json.put("idPessoa", idPessoa);
            json.put("nome", nomeUsuario);
            json.put("email", emailUsuario);
            json.put("senha", senhaUsuario);
            json.put("balancoGeral", null);
            json.put("todasFaturas", null);
            json.put("listaFluxoFinanceiro", jsonArrayFluxo);
            json.put("listaMetas", jsonArrayMeta);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonPessoa.toString();
    }

    private boolean verificarCamposVazios(String ...campos) {
        for (String campo : campos) {
            if (campo.isEmpty()) {
                exibirMensagemToast("Preencha todos os campos");
                return true;
            }
        }
        return false;
    }

    private boolean verificaEmailExistente(String emailUsuario) {
        for(Pessoa pessoa : listaDePessoas) {
            if (pessoa.getEmail().equals(emailUsuario)) {
                return true;
            }
        }
        return false;
    }

    private boolean validarSenha(String senhaUsuario, String confirmarSenhaUsuario) {
        if (!senhaUsuario.equals(confirmarSenhaUsuario)) {
            exibirMensagemToast("As senhas não coincidem!");
            return false;
        }
        return true;
    }

    private void voltarParaLoginActivity(String emailUsuario) {
        Intent intentLogin= new Intent(CadastroActivity.this, LoginActivity.class);
        intentLogin.putExtra("emailUsuario", emailUsuario);
        startActivity(intentLogin);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void inicializarViews() {
        nomeUsuarioET = findViewById(R.id.editTextNomeUsuarioCadastro);
        emailUsuarioET = findViewById(R.id.editTextEmailUsuarioCadastro);
        senhaUsuarioET = findViewById(R.id.editTextSenhaUsuarioCadastro);
        confirmarSenhaUsuarioET = findViewById(R.id.editTextConfirmarSenhaUsuarioCadastro);
        configurarOnTouchListeners();
    }

    private void configurarOnTouchListeners() {
        onClickSenha = new OnClickShowText(senhaUsuarioET);
        senhaUsuarioET.setOnTouchListener(onClickSenha);

        onClickConfirmarSenha = new OnClickShowText(confirmarSenhaUsuarioET);
        confirmarSenhaUsuarioET.setOnTouchListener(onClickConfirmarSenha);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
    }

    private void exibirMensagemToast(String mensagem) {
        Toast.makeText(this, mensagem, DURACAO_TOAST).show();
    }
}