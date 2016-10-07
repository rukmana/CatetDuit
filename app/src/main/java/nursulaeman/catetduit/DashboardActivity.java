package nursulaeman.catetduit;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends BaseActivity {

    RecyclerView rv_income;
    RecyclerView rv_expenses;
    RecyclerView.Adapter rv_adapter, rv_adapter2;
    RecyclerView.LayoutManager rv_layout_manager, rv_layout_manager2;
    Cursor incomes, inc, expenses, exp, tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.setTitle("Dashboard");

        final DatabaseHelper myDB = new DatabaseHelper(this);
        incomes = myDB.listIncome();
        inc = myDB.listIncome();
        expenses = myDB.listExpense();
        exp = myDB.listExpense();
        tmp = myDB.listTmp();

        // Code Recycleview

        rv_income = (RecyclerView) findViewById(R.id.rv_income);
        rv_income.setHasFixedSize(true);

        rv_expenses = (RecyclerView) findViewById(R.id.rv_expenses);
        rv_expenses.setHasFixedSize(true);

        rv_layout_manager = new LinearLayoutManager(this);
        rv_layout_manager2 = new LinearLayoutManager(this);
        rv_income.setLayoutManager(rv_layout_manager);
        rv_expenses.setLayoutManager(rv_layout_manager2);

        rv_adapter = new MyAdapter(loadincome());
        rv_income.setAdapter(rv_adapter);

        rv_adapter2 = new MyAdapter2(loadexpenses());
        rv_expenses.setAdapter(rv_adapter2);

        // End Recycleview

        int sumI = 0;
        TextView totalI = (TextView) findViewById(R.id.tv_total_I);
        while (inc.moveToNext()) {
            sumI += inc.getInt(inc.getColumnIndex("AMOUNT"));
        }
        totalI.setText("Rp. " + String.valueOf(sumI));

        int sumE = 0;
        TextView totalE = (TextView) findViewById(R.id.tv_total_E);
        while (exp.moveToNext()) {
            sumE += exp.getInt(exp.getColumnIndex("AMOUNT"));
        }
        totalE.setText("Rp. " + String.valueOf(sumE));

        TextView balance = (TextView) findViewById(R.id.tv_balance);
        balance.setText("Rp. " + String.valueOf(sumI-sumE));

    }

    private String[] loadincome() {
        String[] incomesA = new String[incomes.getCount()];
        while (incomes.moveToNext()) {
            incomesA[incomes.getPosition()]=incomes.getInt(0)+" "+incomes.getString(1)+" "+incomes.getString(2)+" "+incomes.getString(4)+" "+incomes.getString(5);
        }
    return incomesA;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] ds_data1;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CardView cv_income;
            public TextView tv_income;

            public ViewHolder(View v) {
                super(v);
                cv_income = (CardView) v.findViewById(R.id.cv_income);
                tv_income = (TextView) v.findViewById(R.id.tv_income);
            }
        }

        public MyAdapter(String[] dataset) {
            ds_data1 = dataset;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_income, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv_income.setText(ds_data1[position]);

            // dialog

            holder.tv_income.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // costum dialog
                    final Dialog dialog = new Dialog(DashboardActivity.this);
                    dialog.setContentView(R.layout.dialog_dashboard);
                    dialog.setTitle("Update or Delete");

                    incomes.moveToPosition(position);

                    final int Idi = incomes.getInt(incomes.getColumnIndexOrThrow("ID"));

                    final EditText des_in = (EditText) dialog.findViewById(R.id. et_dialog_des);
                    String des_ins = incomes.getString(incomes.getColumnIndex("DESCRIPTION"));
                    des_in.setText(des_ins);

                    final EditText amo_in = (EditText) dialog.findViewById(R.id. et_dialog_amo);
                    String amo_ins = incomes.getString(incomes.getColumnIndex("AMOUNT"));
                    amo_in.setText(amo_ins);

                    Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonU);
                    dialogButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseHelper myDB1 = new DatabaseHelper(DashboardActivity.this);
                            myDB1.updateIncome(String.valueOf(Idi), des_in.getText().toString(), amo_in.getText().toString(), getDateTime());
                            tmp.moveToLast();
                            myDB1.updateTmp(String.valueOf(tmp.getInt(0)), incomes.getPosition());
                            Toast.makeText(DashboardActivity.this, "updated", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                            dialog.dismiss();
                        }
                    });

                    Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonD);
                    dialogButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseHelper myDB1 = new DatabaseHelper(DashboardActivity.this);
                            myDB1.deleteIncome(String.valueOf(Idi));
                            Toast.makeText(DashboardActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                            dialog.dismiss();
                        }
                    });

                    Button dialogButton3 = (Button) dialog.findViewById(R.id.dialogButtonC);
                    dialogButton3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return ds_data1.length;
        }
    }

    private String[] loadexpenses() {
        String[] expensesA = new String[expenses.getCount()];
        while (expenses.moveToNext()) {
            expensesA[expenses.getPosition()]=expenses.getString(1)+" "+expenses.getString(2)+" "+expenses.getString(4)+" "+expenses.getString(5);
        }
        return expensesA;
    }

    public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
        private String[] ds_data2;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public CardView cv_expenses;
            public TextView tv_expenses;

            public ViewHolder(View v) {
                super(v);
                cv_expenses = (CardView) v.findViewById(R.id.cv_expenses);
                tv_expenses = (TextView) v.findViewById(R.id.tv_expenses);
            }
        }

        public MyAdapter2(String[] dataset2) {
            ds_data2 = dataset2;
        }

        @Override
        public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_expenses, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv_expenses.setText(ds_data2[position]);

            holder.tv_expenses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // costum dialog
                    final Dialog dialog = new Dialog(DashboardActivity.this);
                    dialog.setContentView(R.layout.dialog_dashboard);
                    dialog.setTitle("Update or Delete");

                    expenses.moveToPosition(position);

                    final int Ide = expenses.getInt(expenses.getColumnIndexOrThrow("ID"));

                    final EditText des_ex = (EditText) dialog.findViewById(R.id. et_dialog_des);
                    String des_exs = expenses.getString(expenses.getColumnIndex("DESCRIPTION"));
                    des_ex.setText(des_exs);

                    final EditText amo_ex = (EditText) dialog.findViewById(R.id. et_dialog_amo);
                    String amo_exs = expenses.getString(expenses.getColumnIndex("AMOUNT"));
                    amo_ex.setText(amo_exs);

                    Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonU);
                    dialogButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseHelper myDB1 = new DatabaseHelper(DashboardActivity.this);
                            myDB1.updateExpense(String.valueOf(Ide), des_ex.getText().toString(), amo_ex.getText().toString(),getDateTime());
                            Toast.makeText(DashboardActivity.this, "updated", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                            dialog.dismiss();
                        }
                    });

                    Button dialogButton2 = (Button) dialog.findViewById(R.id.dialogButtonD);
                    dialogButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseHelper myDB1 = new DatabaseHelper(DashboardActivity.this);
                            myDB1.deleteExpense(String.valueOf(Ide));
                            Toast.makeText(DashboardActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                            dialog.dismiss();
                        }
                    });

                    Button dialogButton3 = (Button) dialog.findViewById(R.id.dialogButtonC);
                    dialogButton3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return ds_data2.length;
        }
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}





