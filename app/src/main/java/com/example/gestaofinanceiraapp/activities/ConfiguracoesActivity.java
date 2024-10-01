package com.example.gestaofinanceiraapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestaofinanceiraapp.ComunicacaoServer;
import com.example.gestaofinanceiraapp.Pessoa.Pessoa;
import com.example.gestaofinanceiraapp.R;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracoesActivity extends AppCompatActivity {
    private Pessoa p = new Pessoa();
    private EditText nomeUsuarioET, emailUsuarioET, senhaUsuarioET;
    private List<Pessoa> listaDePessoas = new ArrayList<>();
    private ComunicacaoServer cs = new ComunicacaoServer();
    private String pessoaJson;
    private static final int DURACAO_TOAST = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        inicializarViews();
        pessoaJson = getIntent().getStringExtra("pessoaJson");
        deserializarJson(pessoaJson);
        listarTodasPessoasJSON();
    }

    public void atualizarUsuario(View v){
        String nome = nomeUsuarioET.getText().toString().trim();
        String email = emailUsuarioET.getText().toString().trim();
        String senha = senhaUsuarioET.getText().toString().trim();

        listarTodasPessoasJSON();

        try {
            String idPessoa = String.valueOf(p.getIdPessoa());
            p.fromJSON(pessoaJson);

            if (!nome.isEmpty()) {
                p.setNome(nome);
            }

            if (!email.isEmpty()) {
                if (!verificaEmailExistente(email)) {
                    p.setEmail(email);
                }
                else {
                    exibirMensagemToast("Email já cadastrado");
                    return;
                }
            }

            if (!senha.isEmpty()) {
                p.setSenha(senha);
            }

            String novoJson = p.toJSON();
            System.out.println(novoJson);

            String url = "http://192.168.0.16:8081/pessoa/atualizarTodasInfo/" + idPessoa;
            cs.atualizarVolleyJson(url, novoJson, this);

            if (!cs.isRespostaPut()) {
                exibirMensagemToast("Erro ao efetuar a alteração");

            }
            else {
                exibirMensagemToast("Alteração efetuada com sucesso");
                pessoaJson = novoJson;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void listarTodasPessoasJSON() {
        String url = "http://192.168.0.16:8081/pessoa/listar";
        cs.listarTodasPessoasJSON(url, this);

        if (cs.isRespostaGet()) {
            for (Pessoa pessoa : cs.getListaDePessoas()){
                listaDePessoas.add(pessoa);
            }
        }
    }

    private boolean verificaEmailExistente(String emailUsuario) {
        for(Pessoa pessoa : listaDePessoas) {
            if (pessoa.getEmail().equals(emailUsuario)) {
                return true;
            }
        }
        return false;
    }

    private void deserializarJson(String json) {
        try {
            pessoaJson = json;
            this.p.fromJSON(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void exibirMensagemToast(String mensagem) {
        Toast.makeText(this, mensagem, DURACAO_TOAST).show();
    }

    private boolean verificarCamposVazios(String nome, String email, String senha) {
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            exibirMensagemToast("Preencha todos os campos");
            return true;
        }
        return false;
    }

    private void inicializarViews() {
        nomeUsuarioET = findViewById(R.id.editTextNomeUsuarioConfiguracoes);
        emailUsuarioET = findViewById(R.id.editTextEmailUsuarioConfiguracao);
        senhaUsuarioET = findViewById(R.id.editTextSenhaUsuarioConfiguracao);
    }

    public void mudarTelaMain(View v){
        Intent intentMain = new Intent(ConfiguracoesActivity.this, MainActivity.class);
        intentMain.putExtra("pessoaJson", pessoaJson);
        startActivity(intentMain);
    }

    public void mudarTelaGrafico(View v){
        Intent intentGrafico = new Intent(ConfiguracoesActivity.this, GraficoActivity.class);
        intentGrafico.putExtra("pessoaJson", pessoaJson);
        startActivity(intentGrafico);
    }

    public void mudarTelaMetas(View v){
        Intent intentMetas = new Intent(ConfiguracoesActivity.this, MetasActivity.class);
        intentMetas.putExtra("pessoaJson", pessoaJson);
        startActivity(intentMetas);
    }
}