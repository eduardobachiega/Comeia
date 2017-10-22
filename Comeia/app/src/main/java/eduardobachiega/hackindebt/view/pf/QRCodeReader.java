package eduardobachiega.hackindebt.view.pf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.edsb.dutils.UDialogs;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eduardobachiega.hackindebt.R;

public class QRCodeReader extends Fragment {
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    public QRCodeReader() {
        // Required empty public constructor
    }

    @OnClick(R.id.ibReadQRCode)
    public void readQRCodeClick() {
        IntentIntegrator.forSupportFragment(this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).initiateScan();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
                            resultCode, data);
                    Log.e("Result", result.toString());
                    Double value = Double.parseDouble(result.getContents());
                    final Double moneySaved = value * 0.05;
                    String message = getString(R.string.paid, value, moneySaved);
                    UDialogs.showPopUpMessage(getContext(), getString(R.string.paymentSuccess),
                            message, getString(R.string.ok), true, 0, null);
                    SharedPreferences preferences = getActivity()
                            .getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
                    final String id = preferences.getString("ID", null);
                    if (id != null) {
                        reference.child("PF").child(id).child("moneySaved").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    Double oldValue = Double.parseDouble(dataSnapshot.getValue().toString());
                                    reference.child("PF").child(id).child("moneySaved").setValue(oldValue + moneySaved);
                                }catch (Exception e){
                                    reference.child("PF").child(id).child("moneySaved").setValue(moneySaved);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                databaseError.toException().printStackTrace();
                            }
                        });
                    }
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qrcode_reader, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
