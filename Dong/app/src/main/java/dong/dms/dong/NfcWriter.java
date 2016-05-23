package dong.dms.dong;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

/**
 * Created by Adam on 18/05/16.
 */
public class NfcWriter {

    public boolean WriteTag(Tag tag, Object object) {

        // record that contains our custom data from textfield, using custom MIME_TYPE
        byte[] payload = serialize(object);
        byte[] mimeBytes = "application/aut.dong".getBytes(Charset.forName("US-ASCII"));
        NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes,
                new byte[0], payload);
        NdefMessage message = new NdefMessage(new NdefRecord[] { record });

        try {
            // see if tag is already NDEF formatted
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable() || ndef.getMaxSize() < message.toByteArray().length) {
                    ndef.close();
                    return false;
                }
                else {
                    ndef.writeNdefMessage(message);
                    ndef.close();
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignore
        }

        return false;
    }

    public static byte[] serialize(Object obj) {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object deserialize(byte[] bytes) {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
