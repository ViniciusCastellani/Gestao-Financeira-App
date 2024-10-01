package com.example.gestaofinanceiraapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestaofinanceiraapp.ComunicacaoServer;
import com.example.gestaofinanceiraapp.OnClickShowText;
import com.example.gestaofinanceiraapp.Pessoa.Pessoa;
import com.example.gestaofinanceiraapp.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText emailET, senhaET;
    private TextView mensagemCadastro;
    private OnClickShowText onClickText;
    private ComunicacaoServer cs = new ComunicacaoServer();
    private Pessoa p = new Pessoa();
    private List<Pessoa> listaDePessoas = new ArrayList<>();
    private String pessoaJson, emailUsuario, senhaUsuario;
    private static final int DURACAO_TOAST = Toast.LENGTH_LONG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarViews();
        emailUsuario = getIntent().getStringExtra("emailUsuario");
        emailET.setText(emailUsuario);
        listarTodasPessoasJSON();
        configurarMostrarSenha();
        configurarOpcaoIrTelaCadastro();
    }

    public void validarLogin(View v) {
        listarTodasPessoasJSON();
        String emailUsuario = emailET.getText().toString().trim();
        String senhaUsuario = senhaET.getText().toString().trim();

        System.out.println(listaDePessoas);

        if (verificarCamposVazios(emailUsuario, senhaUsuario)) return;

        if (validarUsuario(emailUsuario, senhaUsuario)){
            exibirMensagemToast("Login realizado com sucesso!");
            irParaTelaPrincipal(pessoaJson);
        }
        else {
            exibirMensagemToast("Usuário ou senha inválidos.");
        }
    }

    private boolean verificarCamposVazios(String emailUsuario, String senhaUsuario) {
        if (emailUsuario.isEmpty() || senhaUsuario.isEmpty()) {
            exibirMensagemToast("Os campos id e senha não podem estar vazios.");
            return true;
        }
        return false;
    }


    private Boolean validarUsuario(String emailUsuario, String senhaUsuario) {
        for (Pessoa pessoa : listaDePessoas) {
            if (pessoa.getEmail().equals(emailUsuario) && pessoa.getSenha().equals(senhaUsuario)) {
                try {
                    pessoaJson = pessoa.toJSON();
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    public void listarTodasPessoasJSON() {
        String url = "http://192.168.0.16:8081/pessoa/listar";
        listaDePessoas.clear();
        cs.listarTodasPessoasJSON(url, this);
        if (cs.isRespostaGet()){
            for (Pessoa pessoa : cs.getListaDePessoas()){
                listaDePessoas.add(pessoa);
            }
            System.out.println(listaDePessoas);
    //      Toast.makeText(this, cs.getResposta(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inicializarViews() {
        emailET = findViewById(R.id.editTextEmailLogin);
        senhaET = findViewById(R.id.editTextSenhaLogin);
        mensagemCadastro = findViewById(R.id.textViewCadastroLogin);
        emailET.setText(emailUsuario);
    }

    private void configurarMostrarSenha() {
        onClickText = new OnClickShowText(senhaET);
        senhaET.setOnTouchListener(onClickText);
    }

    private void configurarOpcaoIrTelaCadastro() {
        mensagemCadastro.setOnClickListener(v -> irParaTelaCadastro());
    }

    private void irParaTelaCadastro() {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void irParaTelaPrincipal(String pessoaJson) {
        Intent intentMain = new Intent(LoginActivity.this,MainActivity.class);
        intentMain.putExtra("pessoaJson", pessoaJson);
        startActivity(intentMain);
    }


    private void exibirMensagemToast(String mensagem) {
        Toast.makeText(this, mensagem, DURACAO_TOAST).show();
    }
}