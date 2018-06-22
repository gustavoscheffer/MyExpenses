package br.com.fadergs.myexpenses;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CadastraDespesaActivity extends AppCompatActivity {

    //Referencia do firebaseDb
    DatabaseReference mDatabase;

    String userId;
    String nomeDespesa;
    double valorDespesa;
    String categoriaGasto;
    long dataDespesa;

    //Firebase user
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_despesa);

        //Pega a referecia do Base de dados
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Coloca a data atual no campo data
        setDefaultDate();

        Spinner spinner = (Spinner) findViewById(R.id.listaCategorias);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoria_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);








        // Implementa o botão para salvar os gastos
        Button btnSalvar = findViewById(R.id.btnCadastraDespesa);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Pega os valores do botoes
                EditText editTextNomeDespesa = findViewById(R.id.edtNomeDespesa);
                nomeDespesa =  editTextNomeDespesa.getText().toString();
                EditText editTextValor = findViewById(R.id.edtValorDespesa);
                String strValorDespesa = editTextValor.getText().toString();
                TextView textViewData = findViewById(R.id.edtDataDespesa);
                dataDespesa = convertDataToTimeStamp(textViewData.getText().toString());
                Spinner spinnerCategoria = findViewById(R.id.listaCategorias);
                categoriaGasto = spinnerCategoria.getSelectedItem().toString();

                //Pega o id do user
                userId = firebaseUser.getUid();

                //valida campos preenchidos
                if (validaCampos(nomeDespesa, strValorDespesa, dataDespesa, categoriaGasto).size() == 4){
                    //Cadastra Despesa
                    valorDespesa = Double.parseDouble(strValorDespesa);
                    cadastraNovaDespesa(userId, nomeDespesa,valorDespesa,dataDespesa,categoriaGasto);
                }else{
                    Toast.makeText(CadastraDespesaActivity.this, "Favor verifique se todos os campos foram \n" +
                            "prenchidos!", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    // valida campos
    private List validaCampos(String nomeDespesa, String valorDespesa, long dataDespesa, String categoriaGasto) {

        List<String> todosOk = new ArrayList<String>();

        if (!nomeDespesa.isEmpty()){
            todosOk.add("ok");
        }

        if (!valorDespesa.isEmpty()){
            todosOk.add("ok");
        }

        if (dataDespesa != 0){
            todosOk.add("ok");
        }

        if(!categoriaGasto.isEmpty()){
            todosOk.add("ok");
        }

        return todosOk;
    }

    // implmenta a view que mostra o calendario para escolher a data
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Implementa o metodo de colocar a data atual no campo data
    public void setDefaultDate() {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //pega a referencia do campo para colocar a data
        TextView txtDate = findViewById(R.id.edtDataDespesa);

        // Adicionar 1, pois o indice do mes começa do 0
        int mesAtual = month + 1;

        //Coloca a data selecionada no campo data
        txtDate.setText(day + "/" + mesAtual + "/" + year);

    }

    // Implementa Cadastra Despesa
    private void cadastraNovaDespesa(String id, String nome, double valor, long data, String categoria) {

        String key = mDatabase.child("gastos").push().getKey();

        Despesa despesa = new Despesa(id,nome, valor, data, categoria);

        Map<String, Object> gastoValues = despesa.toMap();

        mDatabase.child("gastos").child(id).child(key).updateChildren(gastoValues).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(CadastraDespesaActivity.this, "Despesa salva com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CadastraDespesaActivity.this, "ERRO AO SALVAR A DESPESA!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* Metodo que converte a data de string para timestamp
Sera usado para inserir a data no banco.*/
    private long convertDataToTimeStamp(String strData) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*Metodo que converte data (Timestamp) para data String(dd/MM/yyyy)
      Este sera usado para converter o dado quando vem do banco.*/
    private String convertTimeStampToDataBr(long dataTimeStamp){
        DateFormat formatter =  new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(dataTimeStamp);
    }

}

