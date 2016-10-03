package nursulaeman.catetduit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncronizeActivity extends BaseActivity {

    TextView tv_respond;
    Cursor incomes;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncronize);
        this.setTitle("Syncronize");

        DatabaseHelper myDB = new DatabaseHelper(this);
        incomes = myDB.listIncome();

        tv_respond = (TextView) findViewById(R.id.tv_respond);

        Button btn_sync = (Button) findViewById(R.id.btn_Sync);
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getApi() throws JSONException {

        progresStart();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://private-fc7f8-cateduit.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final IncomeTransactionApi income_api = retrofit.create(IncomeTransactionApi.class);

        while (incomes.moveToNext()) {
            final Integer id = new Integer(incomes.getInt(0));
            Log.e("cek1", String.valueOf(incomes.getInt(0)));
            Call<IncomeTransaction> call = income_api.getIncomeTransaction(incomes.getInt(0));
            call.enqueue(new Callback<IncomeTransaction>() {
                @Override
                public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {
                    int status = response.code();
                    Log.e("cek2", String.valueOf(status));
                    Log.e("cek3", String.valueOf(response.body().getId()));
                    tv_respond.setText("Id = " + String.valueOf(response.body().getId()));
                    Log.e("ceks",String.valueOf(id));
                    if (id != response.body().getId()) {
                        postApi(id);
                    } else if (id == response.body().getId()) {
                        putApi();
                    }

                }

                @Override
                public void onFailure(Call<IncomeTransaction> call, Throwable t) {
                    Log.e("cek4", String.valueOf(t));
                    tv_respond.setText(String.valueOf(t));
                }
            });
        }
    }

    private void postApi(int id) {
        tv_respond.setText("post");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://private-fc7f8-cateduit.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final IncomeTransactionApi income_api = retrofit.create(IncomeTransactionApi.class);
        // POST
        incomes.moveToPosition(id);
        IncomeTransaction incometransaction = new IncomeTransaction(incomes.getInt(0), incomes.getString(1), incomes.getString(2));
        Call<IncomeTransaction> call = income_api.saveIncomeTransaction(incometransaction);
        call.enqueue(new Callback<IncomeTransaction>() {
            @Override
            public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {
            }

            @Override
            public void onFailure(Call<IncomeTransaction> call, Throwable t) {
            }

        });
    }


    private void putApi() {
        tv_respond.setText("put");
    }

    private void progresStart() {
        progressDialog = new ProgressDialog(SyncronizeActivity.this);
        progressDialog.setTitle("Syncronize on Process");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

}






