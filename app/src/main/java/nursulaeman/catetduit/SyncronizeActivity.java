package nursulaeman.catetduit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncronizeActivity extends BaseActivity {

    TextView tv_respond;
    Cursor incomes;
    ProgressDialog progressDialog;
    int status, idserver = 0;

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

                progresStart();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://private-fc7f8-cateduit.apiary-mock.com/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                IncomeTransactionApi income_api = retrofit.create(IncomeTransactionApi.class);

                if (incomes.getCount() > 0) {
                    incomes.moveToFirst();
                    do {
                        final Integer id = new Integer(incomes.getInt(0));
                        Call<IncomeTransaction> call = income_api.getIncomeTransaction(id);
                        call.enqueue(new Callback<IncomeTransaction>() {
                            @Override
                            public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {

                                status = response.code();
                                idserver = response.body().getId(id);
                                tv_respond.setText(String.valueOf(status));

                                Log.e("cek id local", String.valueOf(id));
                                Log.e("cek status respons", String.valueOf(status));
                                Log.e("cek id server", String.valueOf(idserver));

                            }
                            @Override
                            public void onFailure(Call<IncomeTransaction> call, Throwable t) {
                                tv_respond.setText(String.valueOf(t));
                            }
                        });
                    incomes.moveToNext();
                    } while (!incomes.isAfterLast());
                    progresStop();
                } else {
                    Toast.makeText(SyncronizeActivity.this, "nothing to sync", Toast.LENGTH_SHORT).show();
                    progresStop();
                }
            }
        });

    } // on create

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

} // class






