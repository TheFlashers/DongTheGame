package dong.dms.dong;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by MI on 11/05/16.
 */
public interface ComNode extends Runnable, Serializable {

    public static final UUID SERVICE_UUID
            = UUID.fromString("aa7e561f-591f-4767-bf26-e4bff3f0895f");
    public static final String SERVICE_NAME = "Android Bluetooth Demo";
    public void forward(String message);
    public void stop();
    public void registerActivity(ClientActivity clientActivity);

}
