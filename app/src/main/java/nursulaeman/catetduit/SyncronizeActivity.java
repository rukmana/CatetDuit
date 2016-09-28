package nursulaeman.catetduit;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncronizeActivity extends BaseActivity {

    TextView tv_respond;
    DatabaseHelper myDB;
    Cursor incomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncronize);
        this.setTitle("Syncronize");

        myDB = new DatabaseHelper(this);
        incomes = myDB.listIncome();

        tv_respond = (TextView) findViewById(R.id.tv_respond);

        Button btn_sync = (Button) findViewById(R.id.btn_Sync);
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://private-fc7f8-cateduit.apiary-mock.com/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();


                IncomeTransactionApi income_api = retrofit.create(IncomeTransactionApi.class);

                while (incomes.moveToNext()) {
                    IncomeTransaction income_save = new IncomeTransaction(incomes.getInt(0),incomes.getString(1),incomes.getString(2),incomes.getString(3));
                    Call<IncomeTransaction> call = income_api.saveIncomeTransaction(income_save);
                    call.enqueue(new Callback<IncomeTransaction>() {
                        @Override
                        public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {
                            int status = response.code();
                            Toast.makeText(SyncronizeActivity.this, String.valueOf(status)+" - "+String.valueOf(response.body()), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<IncomeTransaction> call, Throwable t) {
                            Toast.makeText(SyncronizeActivity.this, String.valueOf(t), Toast.LENGTH_LONG).show();
                        }
                    });

                }
/*
                Call<IncomeTransactions> call = income_api.getIncomeTransactions();
                call.enqueue(new Callback<IncomeTransactions>() {

                    @Override
                    public void onResponse(Call<IncomeTransactions> call, Response<IncomeTransactions> response) {
                        int status = response.code();
                        tv_respond.setText(String.valueOf(status));
                        for (IncomeTransactions.IncomeTransactionItem ic : response.body().getIncomeTransactions()) {
                            Toast.makeText(SyncronizeActivity.this, "cek", Toast.LENGTH_LONG).show();
                            tv_respond.append(
                                    "Id = " + String.valueOf(ic.getId()) +
                                            System.getProperty("line.separator") +
                                            "Description = " + ic.getDescription() +
                                            System.getProperty("line.separator") +
                                            "Amount = " + ic.getAmount() +
                                            System.getProperty("line.separator")
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<IncomeTransactions> call, Throwable t) {
                        tv_respond.setText(String.valueOf(t));
                    }
                });
                */

            }

        });

    }

}
