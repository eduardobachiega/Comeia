package eduardobachiega.hackindebt.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eduardobachiega.hackindebt.R;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String TAG = LoginActivity.this.getClass().getSimpleName();
    Context context = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            SharedPreferences preferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
            boolean isPJ = preferences.getBoolean("IS_PJ", false);
            Log.e("VALUE", String.valueOf(isPJ));
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("newPJExtra", isPJ);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fabLogin)
    public void fabLoginClick() {
        smsRequest();
    }

    private void smsRequest() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                getPhoneText(),
                60,
                TimeUnit.SECONDS,
                this,
                smsCallback);
    }

    private String getPhoneText() {
        return etPhoneNumber.getText().toString();
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks smsCallback =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(context, "ERROR: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Intent i = new Intent(context, SelectPlayerActivity.class);
                            i.putExtra("PHONE", etPhoneNumber.getText());
                            startActivity(i);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

}
