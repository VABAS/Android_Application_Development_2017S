package net.siekkiset.jesse.androidcourse2017s.nfcrecearch;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class NfcWriteActivity extends AppCompatActivity {
    protected NfcAdapter nfcAdapter;
    protected String textToWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_write);
        textToWrite = getIntent().getStringExtra("text").toString();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, R.string.nfc_not_supported_message, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        IntentFilter[] intentFiltersArray = new IntentFilter[]{};
        String[][] techList = new String[][]{
                {android.nfc.tech.Ndef.class.getName()},
                {android.nfc.tech.NdefFormatable.class.getName()}
        };
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
    }

    private void handleIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefMessage msg;
        try {
            msg = new NdefMessage(new NdefRecord[] {
                    new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, textToWrite.getBytes())
            });
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error writing tag: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        writeTag(tag, msg);
        Toast.makeText(getBaseContext(), "Tag written", Toast.LENGTH_LONG).show();
        finish();
    }

    public void writeTag(Tag tag, NdefMessage message) {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null)  {
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                    }
                }
                else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
