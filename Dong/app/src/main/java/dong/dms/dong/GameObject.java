package dong.dms.dong;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MI on 20/05/16.
 */
public class GameObject {
    double velocityX = 0;
    double velocityY = 0;
    double x = 0;
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

    public static GameObject parseJSON(String s) {

        GameObject go = new GameObject();

        try {
            JSONObject o = new JSONObject(s);
            go.velocityX = o.getDouble("velX");
            go.velocityY = o.getDouble("velY");
            go.x = o.getDouble("x");
            go.isWonRound = o.getBoolean("roundWon");
            go.isWonMatch = o.getBoolean("wonMatch");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return go;
    }

}
