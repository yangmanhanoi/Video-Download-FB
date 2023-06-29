package saver.downloader.videos.inshotvideodownload;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class foodModel {
    String name;
    String price;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    String calories;
    public foodModel(String name, String price, String description, String calories)
    {
        this.calories = calories;
        this.price = price;
        this.name = name;
        this.description = description;
    }
//    private foodModel readfoodModel(XmlPullParser parser) throws XmlPullParserException, IOException{
//        String price = null, description = null, name = null, calories = null;
//        while(parser.next() != XmlPullParser.END_TAG)
//        {
//            if(parser.getEventType() != XmlPullParser.START_TAG)
//            {
//                continue;
//            }
//            String temp = parser.getName();
//            if(temp == "name")
//            {
//                name = readName(parser);
//            }
//            else if(temp == "price")
//            {
//                price = readPrice(parser);
//            }
//            else if(temp == "description")
//            {
//                description = readDescription(parser);
//            }
//            else if(temp == "calories")
//            {
//                calories = readCalories(parser);
//            }
//            else skip(parser);
//        }
//        return new foodModel(name, price, description, calories);
//    }
//    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
//        String result = "";
//        if (parser.next() == XmlPullParser.TEXT) {
//            result = parser.getText();
//            parser.nextTag();
//        }
//        return result;
//    }
//    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException{
//        parser.require(XmlPullParser.START_TAG, null, "name");
//        String name = readText(parser);
//        parser.require(XmlPullParser.END_TAG, null, "name");
//        return  name;
//    }
//    private String readPrice(XmlPullParser parser) throws IOException, XmlPullParserException{
//        parser.require(XmlPullParser.START_TAG, null, "price");
//        String price = readText(parser);
//        parser.require(XmlPullParser.END_TAG, null, "price");
//        return price;
//    }
//    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException{
//        parser.require(XmlPullParser.START_TAG, null, "description");
//        String description = readText(parser);
//        parser.require(XmlPullParser.END_TAG, null, "description");
//        return description;
//    }
//    private String readCalories(XmlPullParser parser) throws IOException, XmlPullParserException{
//        parser.require(XmlPullParser.START_TAG, null, "calories");
//        String calories = readText(parser);
//        parser.require(XmlPullParser.END_TAG, null, "calories");
//        return calories;
//    }
//    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
//        if (parser.getEventType() != XmlPullParser.START_TAG) {
//            throw new IllegalStateException();
//        }
//        int depth = 1;
//        while (depth != 0) {
//            switch (parser.next()) {
//                case XmlPullParser.END_TAG:
//                    depth--;
//                    break;
//                case XmlPullParser.START_TAG:
//                    depth++;
//                    break;
//            }
//        }
//    }
}
