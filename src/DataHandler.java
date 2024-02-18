import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class DataHandler {

    // Inicijuojam mūsų tiesinį sąrašą
    public static LinkedList<Card> cardsCollection = new LinkedList<>();
    public static boolean dataLoaded;

    public DataHandler() {
        cardsCollection = readDataFromJSON();
    }

    public static LinkedList<Card> readDataFromJSON() {
        if (!dataLoaded) {
            try {
                // Nuskaitom JSON duomenys iš failo
                String jsonContent = new String(Files.readAllBytes(Paths.get("data/board.json")));

                // Perkeliam viską į JSON objektą, kad jie būtų suprantami programai
                JSONObject jsonObject = new JSONObject(jsonContent);

                // Nuskaitome kiekvieną masyvą ir įkeliame į mūsų tiesinį sąrašą
                readJSONArray("Properties", cardsCollection, jsonObject);
                readJSONArray("Utilities", cardsCollection, jsonObject);
                readJSONArray("Taxes", cardsCollection, jsonObject);

                // Na ir nuskaitome paskutinį mūsų objektą
                JSONObject startingPoint = jsonObject.getJSONObject("Starting Point");
                JSONObject jailPoint = jsonObject.getJSONObject("Jail Point");
                readJSONObject(startingPoint, cardsCollection);
                readJSONObject(jailPoint, cardsCollection);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            dataLoaded = true;
            return cardsCollection;
        }
        return null;
    }

    // Kadangi turime masyvų ir paprastų objektų JSON faile, pasidarome pora metodų jų individualiam nuskaitymui
    private static void readJSONArray (String key, LinkedList<Card> linkedList, JSONObject jsonObject) {
        JSONArray array = jsonObject.getJSONArray(key);
        for (int i = 0; i < array.length(); i++) {
            JSONObject cardObject = array.getJSONObject(i);
            // Kadangi tame masyve yra tiesiog objektai, tai sukuriama atskiras metodas tu objektų sutvarkymui
            readJSONObject(cardObject, linkedList);
        }
    }

    private static void readJSONObject (JSONObject jsonObject, LinkedList<Card> linkedList) {
        String name = jsonObject.getString("name");
        int price = jsonObject.getInt("price");
        int points = jsonObject.getInt("points");
        String color = jsonObject.getString("color");
        String type = jsonObject.getString("type");
        Card card = new Card(name, price, points, color, type);
        linkedList.add(card);
    }
}
