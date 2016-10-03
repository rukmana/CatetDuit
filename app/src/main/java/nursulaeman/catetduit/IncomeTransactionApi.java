package nursulaeman.catetduit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by nur on 28/09/16.
 */
public interface IncomeTransactionApi {
    @GET("/income_transactions")
    Call<IncomeTransactions> getIncomeTransactions();

    @GET("/income_transaction/{id}")
    Call<IncomeTransaction> getIncomeTransaction(@Path("id") int incometransaction_id);


    @PUT("/income_transaction/{id}")
    Call<IncomeTransaction> updateIncomeTransaction(@Path("id") int incometransaction_id, @Body IncomeTransaction incometransaction);


    @POST("/income_transactions")
    Call<IncomeTransaction> saveIncomeTransaction(@Body IncomeTransaction incometransaction);

    @DELETE("/income_transactions/{id}")
    Call<IncomeTransaction> deleteIncomeTransaction(@Path("id") String incometransaction_id);



}
