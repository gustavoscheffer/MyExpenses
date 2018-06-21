package br.com.fadergs.myexpenses;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class CadastraDespesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_despesa);

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
        TextView txtDate = findViewById(R.id.edtData);

        // Adicionar 1, pois o indice do mes começa do 0
        int mesAtual = month + 1;

        //Coloca a data selecionada no campo data
        txtDate.setText(day + "/" + mesAtual + "/" + year);

    }

}
