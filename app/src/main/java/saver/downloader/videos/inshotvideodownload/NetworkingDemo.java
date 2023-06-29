package saver.downloader.videos.inshotvideodownload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.input.ReaderInputStream;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpConnection;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ConnectTimeoutException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ConnectionPoolTimeoutException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicHeader;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicHttpEntityEnclosingRequest;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.params.BasicHttpParams;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.params.HttpConnectionParams;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.params.HttpParams;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class NetworkingDemo extends AppCompatActivity implements View.OnClickListener{
    private Button btn_getData;
    List<foodModel> list = new ArrayList();
    InputStream inputStream = null;
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    InputStream is;
    Document dom;
   // public  Handler networkingHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking_demo);
        btn_getData = findViewById(R.id.btn_getData);
        btn_getData.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_getData) {
            Ion.with(NetworkingDemo.this)
                    .load("https://www.w3schools.com/xml/simple.xml")
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            // do stuff with the result or error
                            String resXML = result;
                            DocumentBuilderFactory factory;
                            DocumentBuilder builder;
                            InputStream is;
                            Document dom;
                            String res = "";
                            try {
                                factory = DocumentBuilderFactory.newInstance();
                                builder = factory.newDocumentBuilder();
                                is = new ByteArrayInputStream(resXML.getBytes("UTF-8"));
                                dom = builder.parse(is);
                                Element element= dom.getDocumentElement();
                                element.normalize();

                                NodeList nodeList = dom.getElementsByTagName("food");
                                for(int i = 0; i < nodeList.getLength(); i++)
                                {
                                    Node node = nodeList.item(i);
                                    if(node.getNodeType() == Node.ELEMENT_NODE)
                                    {
                                        Element element1 = (Element) node;
                                        res += getValue("name", element1);
                                        res += "\n";
                                    }
                                }
                                Log.e("Result", res);
                            }catch (Exception exception){

                            }
                           // getDocument(result);

                            StringReader stringReader = new StringReader(result);
                            InputStream inputStream = new ReaderInputStream(stringReader, StandardCharsets.UTF_8);

                        }
                    });

        }
    }
//    Handler networkingHandler = new Handler()
//    {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            //String result = msg.obj.toString();
//            //Toast.makeText(NetworkingDemo.this, result, Toast.LENGTH_LONG).show();
//        }
//    };
    // Không thế gọi result trong phương thức onClick đc nên phải chạy luồng mới
private static String getValue(String tag, Element element) {
    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
    Node node = (Node) nodeList.item(0);
    return node.getNodeValue();
}
    private String convertStreamtoString(InputStream inputStream) {
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
        }catch (Exception e)
        {

        }
        return  stringBuilder.toString();
    }
    public List parse(InputStream in) throws IOException, XmlPullParserException{
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(in , null);
            xmlPullParser.nextTag();
            return readMenu(xmlPullParser);
        }finally {
            in.close();
        }
    }
    public List readMenu(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException{
        List<foodModel> menu = new ArrayList();
        xmlPullParser.require(XmlPullParser.START_TAG, null, "breakfast_menu");
        while (xmlPullParser.next() != XmlPullParser.END_TAG)
        {
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String x = xmlPullParser.getName();
            if(x == "food")
            {
                menu.add(readfoodModel(xmlPullParser));
            }
        }
        return menu;
    }
    private foodModel readfoodModel(XmlPullParser parser) throws XmlPullParserException, IOException{
        String price = null, description = null, name = null, calories = null;
        while(parser.next() != XmlPullParser.END_TAG)
        {
            if(parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String temp = parser.getName();
            if(temp == "name")
            {
                name = readName(parser);
            }
            else if(temp == "price")
            {
                price = readPrice(parser);
            }
            else if(temp == "description")
            {
                description = readDescription(parser);
            }
            else if(temp == "calories")
            {
                calories = readCalories(parser);
            }
            else skip(parser);
        }
        return new foodModel(name, price, description, calories);
    }
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, null, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "name");
        return  name;
    }
    private String readPrice(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, null, "price");
        String price = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "price");
        return price;
    }
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, null, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return description;
    }
    private String readCalories(XmlPullParser parser) throws IOException, XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, null, "calories");
        String calories = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "calories");
        return calories;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    private InputStream downloadURL(String urlString) throws IOException
    {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(5000);
        conn.connect();
        return conn.getInputStream();
    }
    private void getDocument(String result)
    {
//        String resXML = result;
//        DocumentBuilderFactory factory;
//        DocumentBuilder builder;
//        InputStream is;
//        Document dom;
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            dom = builder.parse(is);
        }catch (Exception exception){

        }
    }
}