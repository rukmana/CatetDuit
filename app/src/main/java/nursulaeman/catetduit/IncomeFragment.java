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

public class IncomeFragment extends Fragment {

    DatabaseHelper myDB;
    EditText et_des, et_amo, et_date;
    Button bt_save, bt_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_income, container, false);

        myDB = new DatabaseHelper(getActivity());
        et_des = (EditText)view.findViewById(R.id.et_des_income);
        et_amo = (EditText)view.findViewById(R.id.et_amo_income);
        et_date = (EditText)view.findViewById(R.id.et_date_income);
        bt_save = (Button)view.findViewById(R.id.bt_save_income);
        bt_cancel = (Button)view.findViewById(R.id.bt_cancel_income);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = et_des.getText().toString();
                String amo = et_amo.getText().toString();
                String dat = et_date.getText().toString();

                boolean result = myDB.saveIncome(des, amo, dat);
                if (result) {
                    Toast.makeText(getActivity(), "Add Income Success", Toast.LENGTH_SHORT).show();
                    et_des.setText("");
                    et_amo.setText("");
                    et_date.setText("");
                }else {
                    Toast.makeText(getActivity(), "Add Income Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}

