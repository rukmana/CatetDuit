package nursulaeman.catetduit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TransactionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        this.setTitle("Transaction");
    }
}
