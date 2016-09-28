package nursulaeman.catetduit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nur on 28/09/16.
 */
public class IncomeTransactions {
    @SerializedName("income_transactions")
    public List<IncomeTransactionItem> income_transactions;
    public List<IncomeTransactionItem> getIncomeTransactions() { return income_transactions; }
    public void setIncomeTransactions(List<IncomeTransactionItem> income_transactions) { this.income_transactions = income_transactions; }
    public IncomeTransactions(List<IncomeTransactionItem> income_transactions) { this.income_transactions = income_transactions; }

    public class IncomeTransactionItem {
        private int id;
        private String description;
        private String amount;
        private String date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
