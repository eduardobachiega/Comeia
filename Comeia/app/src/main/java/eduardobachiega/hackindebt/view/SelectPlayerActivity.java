package eduardobachiega.hackindebt.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import eduardobachiega.hackindebt.R;

public class SelectPlayerActivity extends AppCompatActivity {
    Context context = SelectPlayerActivity.this;

    private enum USER_TYPE {
        PF,
        PJ
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ibPF)
    public void ibPFCLick() {
        startActivity(USER_TYPE.PF);
    }

    @OnClick(R.id.ibPJ)
    public void ibPJClick() {
        startActivity(USER_TYPE.PJ);
    }

    private void startActivity(USER_TYPE user_type) {
        Intent i = new Intent(context, RegisterActivity.class);
        Intent receivedIntent = getIntent();
        if (receivedIntent.hasExtra("PHONE"))
            i.putExtra("PHONE", receivedIntent.getStringExtra("PHONE"));
        i.putExtra("USER_TYPE", user_type.toString());
        startActivity(i);
    }
}
