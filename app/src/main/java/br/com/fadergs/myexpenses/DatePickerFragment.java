package br.com.fadergs.myexpenses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        //pega a referencia do campo para colocar a data

        TextView edtDate = null;

        //Verifica qual campo deve ser alterado a data
        if (getActivity().findViewById(R.id.edtData) != null) {
            edtDate = getActivity().findViewById(R.id.edtData);
        } else if (getActivity().findViewById(R.id.edtDataIni) != null) {
            edtDate = getActivity().findViewById(R.id.edtDataIni);
        } else if (getActivity().findViewById(R.id.edtDataFim) != null) {
            edtDate = getActivity().findViewById(R.id.edtDataFim);
        }

        // Adicionar 1, pois o indice do mes começa do 0
        int mesAtual = month + 1;

        //Coloca a data selecionada no campo data
        edtDate.setText(day + "/" + mesAtual + "/" + year);


    }

}
