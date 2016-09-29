package nursulaeman.catetduit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    DatabaseHelper myDB;
    Cursor incomes;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncronize);
        this.setTitle("Syncronized");

        myDB = new DatabaseHelper(this);
        incomes = myDB.listIncome();

        tv_respond = (TextView) findViewById(R.id.tv_respond);

        Button btn_sync = (Button) findViewById(R.id.btn_Sync);
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void postApi() throws JSONException {

        int counter = 0;

        progressDialog = new ProgressDialog(SyncronizeActivity.this);
        progressDialog.setTitle("Syncronize on Process");
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);

        progressDialog.show();

        counter++;

        Integer current_status = (int) ((counter / (float) incomes.getColumnCount()) * 100);
        progressDialog.setProgress(current_status);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://private-fc7f8-cateduit.apiary-mock.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IncomeTransactionApi income_api = retrofit.create(IncomeTransactionApi.class);


        IncomeTransaction incometransaction = new IncomeTransaction();
        Call<IncomeTransaction> call = income_api.saveIncomeTransaction(incometransaction);
        call.enqueue(new Callback<IncomeTransaction>() {
            @Override
            public void onResponse(Call<IncomeTransaction> call, Response<IncomeTransaction> response) {
                int status = response.code();
                for (incomes.moveToFirst(); !incomes.isAfterLast(); incomes.moveToNext()) {
                    tv_respond.setText(String.valueOf(status) + " : last income sync : " + String.valueOf(incomes.getPosition()));
                }
                if (status == 201) {
                    Toast.makeText(SyncronizeActivity.this, "Sync Success", Toast.LENGTH_SHORT).show();
                } else if (status == 400) {
                    Toast.makeText(SyncronizeActivity.this, "Sync Failed", Toast.LENGTH_SHORT).show();
                }
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<IncomeTransaction> call, Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                AlertDialog.Builder alert = new AlertDialog.Builder(SyncronizeActivity.this);

                alert.setCancelable(false).setTitle("Syncronize").setMessage("fails synchronize")
                        .setPositiveButton("Skip", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SyncronizeActivity.this, "Skip.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    postApi();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                                Toast.makeText(SyncronizeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                alert.show();
                //Toast.makeText(SyncronizeActivity.this, String.valueOf(t), Toast.LENGTH_LONG).show();
            }
        });
    }
}






