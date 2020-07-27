package cookmap.cookandroid.hw.newcalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(),
                android.R.style.Theme_DeviceDefault_Dialog, this, year, month, day);
       /* DatePickerDialog pickerDialog1 = new DatePickerDialog(getActivity(), R.style.CustomDialog
                , this, year, month, day);*/
        //android.R.style.Theme_Material_Dialog_NoActionBar

        return pickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
