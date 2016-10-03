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

        if (incomes.getCount() > 0) {
            incomes.moveToFirst();
            do {
                final Integer id = new Integer(incomes.getInt(0));
                Call<IncomeTransaction> call = income_api.getIncomeTransaction(id);
                call.enqueue(new Callback<IncomeTransaction>() {
                    @Override
                    public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {
                        int status = response.code();
                        int ids = response.body().getId(id); // api dummy can't dynamic
                        int pos = incomes.getPosition();

                        Log.e("cek id local", String.valueOf(id));
                        Log.e("cek status respons", String.valueOf(status));
                        Log.e("cek id server", String.valueOf(ids));

                        if (id != ids) {
                            postApi(pos);
                        } else if (id == ids) {
                            putApi(pos);
                        }
                        progresStop();
                    }

                    @Override
                    public void onFailure(Call<IncomeTransaction> call, Throwable t) {
                        Log.e("cek5", String.valueOf(t));
                        tv_respond.setText(String.valueOf(t));
                    }
                });
                incomes.moveToNext();
            } while (!incomes.isLast());
        } else {
            Toast.makeText(SyncronizeActivity.this, "nothing to sync", Toast.LENGTH_SHORT).show();
            progresStop();
        }
    }

    private void postApi(int pos) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://private-fc7f8-cateduit.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final IncomeTransactionApi income_api = retrofit.create(IncomeTransactionApi.class);
        // POST
        incomes.moveToPosition(pos);
        IncomeTransaction incometransaction = new IncomeTransaction(incomes.getInt(0), incomes.getString(1), incomes.getString(2));
        Call<IncomeTransaction> call = income_api.saveIncomeTransaction(incometransaction);
        call.enqueue(new Callback<IncomeTransaction>() {
            @Override
            public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {
                int status = response.code();
                tv_respond.setText(String.valueOf(status));
                if (String.valueOf(status).equals("201")) {
                    Toast.makeText(SyncronizeActivity.this, "Add success", Toast.LENGTH_SHORT).show();
                } else if (String.valueOf(status).equals("400")) {
                    Toast.makeText(SyncronizeActivity.this, "Add failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<IncomeTransaction> call, Throwable t) {
                tv_respond.setText(String.valueOf(t));
            }

        });
    }


    private void putApi(int pos) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://private-fc7f8-cateduit.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final IncomeTransactionApi income_api = retrofit.create(IncomeTransactionApi.class);
        // PUT
        incomes.moveToPosition(pos);
        Call<IncomeTransaction> call = income_api.updateIncomeTransaction(incomes.getInt(0), new IncomeTransaction(incomes.getInt(0), incomes.getString(1), incomes.getString(2)));
        call.enqueue(new Callback<IncomeTransaction>() {
            @Override
            public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {
                int status = response.code();
                tv_respond.setText(String.valueOf(status));
                if (String.valueOf(status).equals("201")) {
                    Toast.makeText(SyncronizeActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                } else if (String.valueOf(status).equals("400")) {
                    Toast.makeText(SyncronizeActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<IncomeTransaction> call, Throwable t) {
                tv_respond.setText(String.valueOf(t));
            }
        });
    }

    private void progresStart() {
        progressDialog = new ProgressDialog(SyncronizeActivity.this);
        progressDialog.setTitle("Syncronize on Process");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    private void progresStop() {
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}






