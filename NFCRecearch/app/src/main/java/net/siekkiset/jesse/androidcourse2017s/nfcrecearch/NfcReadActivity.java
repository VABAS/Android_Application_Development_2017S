package net.siekkiset.jesse.androidcourse2017s.nfcrecearch;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class NfcReadActivity extends AppCompatActivity {
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_read);

        mTextView = (TextView) findViewById(R.id.textView);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, R.string.nfc_not_supported_message, Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!nfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled.");
        } else {
            mTextView.setText("NFC is enabled");
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, nfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Toast.makeText(getBaseContext(), "NDEF-tag discovered", Toast.LENGTH_LONG).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            readNfc(tag);
        }
    }

    private void readNfc (Tag tag) {
        Ndef ndef = Ndef.get(tag);

        // NDEF not supported.
        if (ndef == null) {
            return;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            String payload = new String(ndefRecord.getPayload());
            byte[] id = tag.getId();

            // Converting tag id to hex-string
            String sId = new String();
            for (int i = 0; i < id.length; i++) {
                String x = Integer.toHexString(((int) id[i] & 0xff));
                if (x.length() == 1) {
                    x = '0' + x;
                }
                sId += x;
                if (i < id.length -1) {
                    sId += ':';
                }
            }

            mTextView.setText(
                    "Tag id: " + sId    + "\n"
                    + "Content: " + payload
            );
        }
    }
}
