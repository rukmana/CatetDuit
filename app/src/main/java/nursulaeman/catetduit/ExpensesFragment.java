package nursulaeman.catetduit;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import nursulaeman.catetduit.validation.Validation;

public class ExpensesFragment extends Fragment {

    DatabaseHelper myDB;
    EditText et_des, et_amo, et_date;
    Button bt_save, bt_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_expenses, container, false);

        myDB = new DatabaseHelper(getActivity());
        et_des = (EditText)view.findViewById(R.id.et_des_expenses);
        et_amo = (EditText)view.findViewById(R.id.et_amo_expenses);
        et_date = (EditText)view.findViewById(R.id.et_date_expenses);
        bt_save = (Button)view.findViewById(R.id.bt_save_expenses);
        bt_cancel = (Button)view.findViewById(R.id.bt_cancel_expenses);
        final Validation val = new Validation();

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = et_des.getText().toString();
                String amo = et_amo.getText().toString();
                String dat = et_date.getText().toString();

                if (!val.isValidText(des)) {
                    et_des.requestFocus();
                    et_des.setError("Required field");
                } else if (!val.isValidText(amo)) {
                    et_amo.requestFocus();
                    et_amo.setError("Required field");
                } else {

                    boolean result = myDB.saveExpense(des, amo, dat, getDateTime());
                    if (result) {
                        Toast.makeText(getActivity(), "Add Expenses Success", Toast.LENGTH_SHORT).show();
                        et_des.setText("");
                        et_amo.setText("");
                        et_date.setText("");
                    } else {
                        Toast.makeText(getActivity(), "Add Expenses Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        return view;
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
