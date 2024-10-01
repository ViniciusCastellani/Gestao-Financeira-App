package com.example.gestaofinanceiraapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gestaofinanceiraapp.ComunicacaoServer;
import com.example.gestaofinanceiraapp.Meta.Meta;
import com.example.gestaofinanceiraapp.Meta.MetaAdapter;
import com.example.gestaofinanceiraapp.Pessoa.Pessoa;
import com.example.gestaofinanceiraapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MetasActivity extends AppCompatActivity {
    private Pessoa p = new Pessoa();
    private String pessoaJson;
    private RecyclerView recyclerViewMetas;
    private MetaAdapter metaAdapter;
    private ArrayList<Meta> metaList = new ArrayList<>();
    private Button buttonNovaMeta;
    private ComunicacaoServer cs = new ComunicacaoServer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);
        pessoaJson = getIntent().getStringExtra("pessoaJson");
        deserializarJson(pessoaJson);

        recyclerViewMetas = findViewById(R.id.recyclerView);
        recyclerViewMetas.setLayoutManager(new LinearLayoutManager(this));

        if (p.getListaMetas() != null) {
            for (Meta meta : p.getListaMetas()) {
                metaList.add(new Meta(meta.getIdMeta(), meta.getTituloMeta(), meta.getDescricaoMeta(), meta.getValorMeta(),
                        meta.getDataPrazo()));
            }
        }

        metaAdapter = new MetaAdapter(metaList, this);
        recyclerViewMetas.setAdapter(metaAdapter);

        buttonNovaMeta = findViewById(R.id.btnNewMeta);
        buttonNovaMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarNovaMetaDialog();
            }
        });
    }

    private void deserializarJson(String json) {
        try {
            pessoaJson = json;
            this.p.fromJSON(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarNovaMetaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_nova_meta, null);
        builder.setView(dialogView);

        final EditText etTituloMeta = dialogView.findViewById(R.id.etMetaTitle);
        final EditText etMetaDescription = dialogView.findViewById(R.id.etMetaDescription);
        final EditText etMetaValue = dialogView.findViewById(R.id.etMetaValue);
        final EditText etPrazo = dialogView.findViewById(R.id.etPrazo);
        Button btnSaveMeta = dialogView.findViewById(R.id.btnSaveMeta);

        final int[] selectedDay = new int[1];
        final int[] selectedMonth = new int[1];
        final int[] selectedYear = new int[1];

        etPrazo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MetasActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                selectedDay[0] = dayOfMonth;
                                selectedMonth[0] = monthOfYear;
                                selectedYear[0] = year;
                                etPrazo.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });

        final AlertDialog dialog = builder.create();

        btnSaveMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String title = etTituloMeta.getText().toString();
                    String description = etMetaDescription.getText().toString();
                    double value = Double.parseDouble(etMetaValue.getText().toString());
                    String prazo = etPrazo.getText().toString();

                    Meta newMeta = new Meta(0, title, description, value, prazo);
                    metaList.add(newMeta);
                    metaAdapter.notifyItemInserted(metaList.size() - 1);
                    dialog.dismiss();
                    enviarMetaServidor(description, title, value, prazo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private void enviarMetaServidor(String description, String title, double value, String prazo) {
        if (verificarCamposVazios(description, value, prazo)) return;

        try {
            p.fromJSON(pessoaJson);

            List<Meta> listaMetas = p.getListaMetas();

            Meta newMeta = new Meta(listaMetas.size() + 1, title, description, value, prazo);
            listaMetas.add(newMeta);
            p.setListaMetas(listaMetas);
            String novoJson = p.toJSON();

            System.out.println(novoJson);

            String url = "http://192.168.0.16:8081/pessoa/meta/" + p.getIdPessoa();
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

    private boolean verificarCamposVazios(String description, double value, String prazo) {
        String valueST = String.valueOf(value);

        if (description.isEmpty() || valueST.isEmpty() || prazo.isEmpty()){
            exibirMensagemToast("Preencha todos os campos");
            return true;
        }
        else {
            return false;
        }
    }

    private void exibirMensagemToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void mudarTelaMain(View v){
        Intent intentMain = new Intent(MetasActivity.this, MainActivity.class);
        intentMain.putExtra("pessoaJson", pessoaJson);
        startActivity(intentMain);
    }

    public void mudarTelaGrafico(View v){
        Intent intentGrafico = new Intent(MetasActivity.this, GraficoActivity.class);
        intentGrafico.putExtra("pessoaJson", pessoaJson);
        startActivity(intentGrafico);
    }

    public void mudarTelaConfiguracao(View v){
        Intent intentConfiguracao = new Intent(MetasActivity.this, ConfiguracoesActivity.class);
        intentConfiguracao.putExtra("pessoaJson", pessoaJson);
        startActivity(intentConfiguracao);
    }

}