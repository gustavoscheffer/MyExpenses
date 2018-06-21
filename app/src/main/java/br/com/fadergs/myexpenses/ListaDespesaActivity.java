package br.com.fadergs.myexpenses;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class ListaDespesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_despesa);

        // Coloca a data atual no campo data
        setDefaultDate(findViewById(R.id.edtDataIni));
        setDefaultDate(findViewById(R.id.edtDataFim));
    }

    // implmenta a view que mostra o calendario para escolher a data
    public void showDatePickerDialogLista(View view) {
        DialogFragment new2Fragment = new DatePickerFragment();
        new2Fragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialogLista2(View view) {
        DialogFragment new3Fragment = new DatePickerFragment();
        new3Fragment.show(getSupportFragmentManager(), "datePicker");
    }
    // Implementa o metodo de colocar a data atual no campo data
    public void setDefaultDate(View viewbyid) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //pega a referencia do campo para colocar a data
        TextView txtDate =(TextView) viewbyid;

        // Adicionar 1, pois o indice do mes começa do 0
        int mesAtual = month + 1;

        //Coloca a data selecionada no campo data
        txtDate.setText(day + "/" + mesAtual + "/" + year);

    }

}


