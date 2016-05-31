package dong.dms.dong;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by MI on 11/05/16.
 */
public interface ComNode extends Runnable, Serializable {

    public static final UUID SERVICE_UUID
            = UUID.fromString("aa7e561f-591f-4767-bf26-e4bff3f0895f");
    public static final String SERVICE_NAME = "DongGame";
    public void setGameLogic(GameLogic gl);
    public void forward(GameObject go);
    public void stop();
    public void registerActivity(GameActivity activity);
    public void sendConfirm(boolean confirm);
    public void setConfirm(boolean set);

}
