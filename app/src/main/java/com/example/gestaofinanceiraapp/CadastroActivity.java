package com.example.gestaofinanceiraapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CadastroActivity extends AppCompatActivity {
    private EditText nomeUsuarioET, emailUsuarioET, senhaUsuarioET, confirmarSenhaUsuarioET;
    private OnClickShowText onClickSenha;
    private OnClickShowText onClickConfirmarSenha;
    private SharedPreferences sp;

    private static final int DURACAO_TOAST = Toast.LENGTH_LONG;
    private static final String PREFS_USUARIO = "PREFS_USUARIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        inicializarViews();
        configurarSharedPreferences();
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

    private void configurarSharedPreferences() {
        sp = getSharedPreferences(PREFS_USUARIO, MODE_PRIVATE);
    }

    public void cadastrarUsuario(View v) {
        String nomeUsuario = nomeUsuarioET.getText().toString().trim();
        String emailUsuario = emailUsuarioET.getText().toString().trim();
        String senhaUsuario = senhaUsuarioET.getText().toString().trim();
        String confirmarSenhaUsuario = confirmarSenhaUsuarioET.getText().toString().trim();

        if (!validarSenha(senhaUsuario, confirmarSenhaUsuario)) return;
        if (verificarCamposVazios(nomeUsuario, emailUsuario, senhaUsuario, confirmarSenhaUsuario)) return;
        if (verificaEmailExistente(emailUsuario)) {
            exibirMensagemToast("Email já está em uso");
            return;
        }

        guardarDadosSharedPreferences(nomeUsuario, emailUsuario, senhaUsuario);
        voltarParaLoginActivity(emailUsuario);
    }

    private void guardarDadosSharedPreferences(String nomeUsuario, String emailUsuario, String senhaUsuario) {
        try {
            JSONObject novoUsuario = new JSONObject();
            novoUsuario.put("nome", nomeUsuario);
            novoUsuario.put("email", emailUsuario);
            novoUsuario.put("senha", senhaUsuario);

            JSONArray usuarios = recuperarUsuariosExistentes();
            usuarios.put(novoUsuario);

            salvarUsuariosNoSharedPreferences(usuarios);
            exibirMensagemToast("Cadastro realizado com sucesso");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray recuperarUsuariosExistentes() throws JSONException {
        String usuariosJson = sp.getString("usuarios", "[]");
        return new JSONArray(usuariosJson);
    }

    private void salvarUsuariosNoSharedPreferences(JSONArray usuarios) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("usuarios", usuarios.toString());
        editor.apply();
    }

    private boolean verificarCamposVazios(String ...campos) {
        StringBuilder mensagemErro = new StringBuilder();
        int contadorCamposVazios = 0;

        for (String campo : campos) {
            if (campo.isEmpty()) {
                mensagemErro.append("campo, ");
                contadorCamposVazios++;
            }
        }

        if (contadorCamposVazios > 0) {
            mensagemErro.setLength(mensagemErro.length() - 2);
            mensagemErro.insert(0, contadorCamposVazios == 1 ? "O campo " : "Os campos ");
            mensagemErro.append(contadorCamposVazios == 1 ? " está vazio" : " estão vazios.");
            exibirMensagemToast(mensagemErro.toString());
            return true;
        }
        return false;
    }

    private boolean verificaEmailExistente(String emailUsuario) {
        try {
            JSONArray usuarios = recuperarUsuariosExistentes();
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject usuario = usuarios.getJSONObject(i);
                if (emailUsuario.equals(usuario.getString("email"))) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
    }

    private void exibirMensagemToast(String mensagem) {
        Toast.makeText(this, mensagem, DURACAO_TOAST).show();
    }
}