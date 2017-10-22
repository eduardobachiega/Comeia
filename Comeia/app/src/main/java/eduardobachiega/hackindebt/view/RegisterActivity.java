package eduardobachiega.hackindebt.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import br.com.edsb.dutils.UDialogs;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eduardobachiega.hackindebt.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.clPf)
    ConstraintLayout clPF;

    @BindView(R.id.clPj)
    ConstraintLayout clPJ;

    @BindView(R.id.etCNPJ)
    EditText etCNPJ;

    @BindView(R.id.etCPF)
    EditText etCPF;

    @BindView(R.id.etStoreName)
    EditText etStoreName;

    @BindView(R.id.etUsername)
    EditText etUsername;

    @BindView(R.id.etStoreAddress)
    EditText etStoreAddress;

    HashMap<String, String> registerData = new HashMap<>();
    boolean isPJ;

    Context context = RegisterActivity.this;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getExtras();
        setupSharedPreferences();
    }

    private void setupSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void getExtras() {
        Intent receivedIntent = getIntent();
        registerData.put("PHONE", receivedIntent.getStringExtra("PHONE"));
        if (receivedIntent.getStringExtra("USER_TYPE").equals("PJ")) {
            clPJ.setVisibility(View.VISIBLE);
            isPJ = true;
        } else {
            clPF.setVisibility(View.VISIBLE);
            isPJ = false;
        }
    }

    @OnClick(R.id.fabRegister)
    public void fabRegisterClick() {
        sendData();
    }

    private void sendData() {
        if (isPJ) {
            setPJData();
        } else {
            setPFData();
            reference.child("PF").child(etCPF.getText().toString()).setValue(registerData);
            editor.putBoolean("IS_PJ", false).apply();
            editor.putString("ID", etCPF.getText().toString()).apply();
            startActivity();
        }
    }

    private void setPJData() {
        registerData.put("storeName", etStoreName.getText().toString());
        getLatLng();
        registerData.put("storeAddress", etStoreAddress.getText().toString());
    }

    private void getLatLng() {
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s" +
                        "&key=%s", etStoreAddress.getText().toString(),
                getString(R.string.GOOGLE_MAPS_API_KEY));
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "ERROR: " + e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONObject location = obj.getJSONArray("results").getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location");
                    String lat = location.getString("lat");
                    String lng = location.getString("lng");
                    registerData.put("LAT", lat);
                    registerData.put("LNG", lng);
                    reference.child("PJ").child(etCNPJ.getText().toString()).setValue(registerData);
                    editor.putString("ID", etCNPJ.getText().toString()).apply();
                    editor.putBoolean("IS_PJ", true).apply();
                    startActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UDialogs.showPopUpMessage(context, getString(R.string.error),
                                    getString(R.string.location_error), getString(R.string.ok),
                                    false, 0, null);
                        }
                    });
                }
            }
        });
    }

    private void setPFData() {
        registerData.put("username", etUsername.getText().toString());
    }

    private void startActivity() {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("IS_PJ", isPJ);
        Log.e("IS_PJ", String.valueOf(isPJ));
        startActivity(i);
    }

}
