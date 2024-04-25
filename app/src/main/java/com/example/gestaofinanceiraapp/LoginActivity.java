package com.example.gestaofinanceiraapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText emailET, senhaET;
    private TextView mensagemCadastro;
    private OnClickShowText onClickText;
    private SharedPreferences sp;

    private static final int DURACAO_TOAST = Toast.LENGTH_LONG;
    private static final String PREFS_USUARIO = "PREFS_USUARIO";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarViews();
        configurarSharedPreferences();
        configurarMostrarSenha();
        configurarOpcaoIrTelaCadastro();
    }

    private void inicializarViews() {
        emailET = findViewById(R.id.editTextEmailLogin);
        senhaET = findViewById(R.id.editTextSenhaLogin);
        mensagemCadastro = findViewById(R.id.textViewCadastroLogin);
        String emailUsuario = getIntent().getStringExtra("emailUsuario");
        emailET.setText(emailUsuario);
    }

    private void configurarSharedPreferences() {
        sp = getSharedPreferences(PREFS_USUARIO, MODE_PRIVATE);
    }

    private void configurarMostrarSenha() {
        onClickText = new OnClickShowText(senhaET);
        senhaET.setOnTouchListener(onClickText);
    }

    public void validarLogin(View v) {
        String emailUsuario = emailET.getText().toString().trim();
        String senhaUsuario = senhaET.getText().toString().trim();

        if (verificarCamposVazios(emailUsuario, senhaUsuario)) return;

        if (validarUsuario(emailUsuario, senhaUsuario)) {
            exibirMensagemToast("Login realizado com sucesso!");
            Intent intentTeste = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intentTeste);
            return;
        }
        exibirMensagemToast("Email ou senha incorretos!");
    }

    private boolean verificarCamposVazios(String emailUsuario, String senhaUsuario) {
        if (emailUsuario.isEmpty() || senhaUsuario.isEmpty()) {
            exibirMensagemToast("Os campos email e senha não podem estar vazios.");
            return true;
        }
        return false;
    }

    private boolean validarUsuario(String emailUsuario, String senhaUsuario) {
        String usuariosJson = sp.getString("usuarios", "[]");
        try {
            JSONArray usuarios = new JSONArray(usuariosJson);
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject usuario = usuarios.getJSONObject(i);
                if (emailUsuario.equals(usuario.getString("email")) && senhaUsuario.equals(usuario.getString("senha"))) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void configurarOpcaoIrTelaCadastro() {
        mensagemCadastro.setOnClickListener(v -> irParaTelaCadastro());
    }

    private void irParaTelaCadastro() {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void exibirMensagemToast(String mensagem) {
        Toast.makeText(this, mensagem, DURACAO_TOAST).show();
    }
}