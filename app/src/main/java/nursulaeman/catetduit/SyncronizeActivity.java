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
    Cursor incomes, tmp;
    ProgressDialog progressDialog;
    int status, idserver = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncronize);
        this.setTitle("Syncronize");

        DatabaseHelper myDB = new DatabaseHelper(this);
        incomes = myDB.listIncome();
        tmp = myDB.listTmp();

        tv_respond = (TextView) findViewById(R.id.tv_respond);

        //append data student to buffer
        StringBuffer buffer = new StringBuffer();
        while (tmp.moveToNext()) {
            buffer.append("tmp : " + tmp.getInt(1) + "\n");
        }
        //show data student
        alert_message("List tmp", buffer.toString());

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
                    incomes.moveToPosition(point());
                    do {
                        final Integer id = new Integer(incomes.getInt(0));
                        Call<IncomeTransaction> call = income_api.getIncomeTransaction(id);

                        // update to tmp
                        DatabaseHelper myDB1 = new DatabaseHelper(SyncronizeActivity.this);
                        int pos1 = incomes.getPosition();
                        Log.e("cek posisi ", String.valueOf(pos1));
                        if (tmp.getCount() == 0) {
                            myDB1.saveTmp(pos1);
                        } else if (tmp.getCount() > 0) {
                            tmp.moveToLast();
                            myDB1.updateTmp(String.valueOf(tmp.getInt(0)), pos1);
                        }

                        call.enqueue(new Callback<IncomeTransaction>() {

                            @Override
                            public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {

                                status = response.code();
                                idserver = response.body().getId(id);
                                tv_respond.setText(String.valueOf(status));
                                int pos = incomes.getPosition();

                                Log.e("cek id local", String.valueOf(id));
                                Log.e("cek status respons", String.valueOf(status));
                                Log.e("cek id server", String.valueOf(idserver));

                                if (id != idserver) {
                                    postApi(pos-1);
                                } else if (id == idserver) {
                                    putApi(pos-1);
                                }



                                progresStop();

                            }

                            @Override
                            public void onFailure(Call<IncomeTransaction> call, Throwable t) {
                                tv_respond.setText(String.valueOf(t));
                            }
                        });

                        incomes.moveToNext();
                    } while (!incomes.isAfterLast());
                } else {
                    Toast.makeText(SyncronizeActivity.this, "nothing to sync", Toast.LENGTH_SHORT).show();
                    progresStop();
                }
            }
        });

    } // on create

    public void alert_message(String title, String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void progresStart() {
        progressDialog = new ProgressDialog(SyncronizeActivity.this);
        progressDialog.setTitle("Syncronize on Process");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    private void progresStop() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
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
                    Toast.makeText(SyncronizeActivity.this, "Update successs", Toast.LENGTH_SHORT).show();
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

    // to cursor start from, the value got from last update or sync stop before finish
    private int point(){
        int x = 0;
        if (tmp.getCount()>0) {
            tmp.moveToLast();
            x = tmp.getInt(1);
        }
        return x;
    }

} // class






