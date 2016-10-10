package nursulaeman.catetduit;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nursulaeman.catetduit.validation.DatePickIncome;
import nursulaeman.catetduit.validation.Validation;

public class IncomeFragment extends Fragment {

    DatabaseHelper myDB;
    EditText et_des, et_amo, et_date;
    Button bt_save, bt_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        myDB = new DatabaseHelper(getActivity());
        et_des = (EditText) view.findViewById(R.id.et_des_income);
        et_amo = (EditText) view.findViewById(R.id.et_amo_income);
        et_date = (EditText) view.findViewById(R.id.et_date_income);
        bt_save = (Button) view.findViewById(R.id.bt_save_income);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel_income);
        et_date.setShowSoftInputOnFocus(false);
        et_date.requestFocus();
        final Validation val = new Validation();

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = et_des.getText().toString();
                ;
                String amo = et_amo.getText().toString();
                String dat = et_date.getText().toString();

                if (!val.isValidText(des)) {
                    et_des.requestFocus();
                    et_des.setError("Required field");
                } else if (!val.isValidText(amo)) {
                    et_amo.requestFocus();
                    et_amo.setError("Required field");
                } else if (!val.isValidText(dat)) {
                    et_date.requestFocus();
                    et_date.setError("Required field");
                } else {
                    boolean result = myDB.saveIncome(des, amo, dat, getDateTime());
                    if (result) {
                        Toast.makeText(getActivity(), "Add Income Success", Toast.LENGTH_SHORT).show();
                        et_des.setText("");
                        et_amo.setText("");
                        et_date.setText("");
                    } else {
                        Toast.makeText(getActivity(), "Add Income Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lay_sync = new Intent(getActivity(), MainActivity.class);
                startActivity(lay_sync);
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickIncome();
                picker.setCancelable(false);
                picker.show(getActivity().getSupportFragmentManager(), "DatePicker");
            }
        });

        return view;
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


}

