package saver.downloader.videos.inshotvideodownload;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class JsonParser extends AppCompatActivity {
    private Button btn_getData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_example);
        //  btn_getData.setOnClickListener(this);
        String result = "{ \"users\" :[" +
                "{\"name\":\"Suresh Dasari\",\"designation\":\"Team Leader\",\"location\":\"Hyderabad\"}" +
                ",{\"name\":\"Rohini Alavala\",\"designation\":\"Agricultural Officer\",\"location\":\"Guntur\"}" +
                ",{\"name\":\"Trishika Dasari\",\"designation\":\"Charted Accountant\",\"location\":\"Guntur\"}] }";
        Log.e("Kiemtra", result);
        try {
            ArrayList<HashMap<String, String>> userList = new ArrayList<>();
            ListView listView = findViewById(R.id.user_list);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("users");
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> user = new HashMap<>();
                JSONObject obj = jsonArray.getJSONObject(i);
                user.put("name", obj.getString("name"));
                user.put("designation", obj.getString("designation"));
                user.put("location", obj.getString("location"));
                userList.add(user);
            }
            ListAdapter adapter = new SimpleAdapter(JsonParser.this, userList, R.layout.custom_listview, new String[]{"name", "designation", "location"}, new int[]{R.id.name, R.id.designation, R.id.location});
            listView.setAdapter(adapter);
        } catch (JSONException exception) {
        }
    }
}

