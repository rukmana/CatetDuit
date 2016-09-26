package nursulaeman.catetduit;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DashboardActivity extends BaseActivity {

    RecyclerView rv_income;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layout_manager;
    Cursor incomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.setTitle("Dashboard");

        final DatabaseHelper myDB = new DatabaseHelper(this);
        incomes = myDB.listIncome();

        // Code Recycleview

        rv_income = (RecyclerView) findViewById(R.id.rv_income);
        rv_income.setHasFixedSize(true);

        rv_layout_manager = new LinearLayoutManager(this);
        rv_income.setLayoutManager(rv_layout_manager);

        rv_adapter = new MyAdapter(loadincome());
        rv_income.setAdapter(rv_adapter);

        // End Recycleview
    }

    private String[] loadincome() {
        String[] incomesA = new String[incomes.getCount()];
        while (incomes.moveToNext()) {
            incomesA[incomes.getPosition()]=incomes.getString(1)+" "+incomes.getString(2);
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
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv_income.setText(ds_data1[position]);
        }

        @Override
        public int getItemCount() {
            return ds_data1.length;
        }
    }
}




