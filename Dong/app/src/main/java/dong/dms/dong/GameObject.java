package dong.dms.dong;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MI on 20/05/16.
 */
public class GameObject {
    int velocityX = 0;
    int velocityY = 0;
    int x = 0;
    boolean isWonRound = false;
    boolean isWonMatch = false;

    public String createJsonString() {

        JSONObject GameObject = new JSONObject();
        try {
            GameObject.put("velX", velocityX);
            GameObject.put("velY", velocityY);
            GameObject.put("x", x);
            GameObject.put("roundWon", isWonRound);
            GameObject.put("wonMatch", isWonMatch);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return GameObject.toString();
    }

}
