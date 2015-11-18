package jp.fccpc.taskmanager.Views.DateDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class SelectDateFragment extends DialogFragment implements DateTimePickerDialog.OnDateTimeSetListener {
    public interface setDateCallback { void callback(Date d, String dataText); }

    public static final String TAG = "SelectDateFragment";

    private EditText mDateTextField;
    private OnSelectDateListener mCallback;
    private setDateCallback mSetCallback;

    public interface OnSelectDateListener {
        void onSelectDate(Date date);
    }

    public void setSelectDateListener(OnSelectDateListener callback) {
        this.mCallback = callback;
    }

    public void setDateTextField(EditText textField) {
        mDateTextField = textField;
    }

    public void setCallback(setDateCallback callback){
        mSetCallback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        final Calendar calendar = Calendar.getInstance();
//        int yy = calendar.get(Calendar.YEAR);
//        int mm = calendar.get(Calendar.MONTH);
//        int dd = calendar.get(Calendar.DAY_OF_MONTH);
//        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        return new DateTimePickerDialog(getActivity(), this, new Date(), true);

    }

    @Override
    public void onDateTimeSet(DatePicker datePickerView, TimePicker timePickerView, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date d = mCalendar.getTime();
        String dateText = sdf.format(d);
        Log.d(TAG, dateText);

        mSetCallback.callback(d, dateText);
    }


}