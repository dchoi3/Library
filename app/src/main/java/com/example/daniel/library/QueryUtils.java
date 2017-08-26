package com.example.daniel.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 6/22/2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils(){}

    public static List<Books> fetchBookData(String requestUrl){

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }


        List<Books> books = extractFeaturesFromJson(jsonResponse);

        return books;
    }


    private static List<Books> extractFeaturesFromJson(String jsonResponse){
        if(TextUtils.isEmpty(jsonResponse)){return null;}

        List<Books> books = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            String title, author, date, description, previewURL;
            Bitmap imgURL;

            for(int i = 0; i < itemsArray.length(); i++){
                JSONObject bookInfo = new JSONObject(itemsArray.getJSONObject(i).getString("volumeInfo"));

                if(bookInfo.has("title")) {title = bookInfo.getString("title");
                Log.i("TITLE: ", title);}
                else title = "NO TITLE FOUND";

                if(bookInfo.has("authors")) author = formatAuthors(bookInfo.getJSONArray("authors").toString());
                else author = "";

                if(bookInfo.has("publishedDate")) date = bookInfo.getString("publishedDate");
                else date = "";

                if(bookInfo.has("description")) description = bookInfo.getString("description");
                else description = "No descriptions found.";

                if(bookInfo.has("imageLinks")) imgURL = getBitmap(bookInfo.getJSONObject("imageLinks").getString("thumbnail"));
                else imgURL = null;

                if(bookInfo.has("previewLink")) previewURL = bookInfo.getString("previewLink");
                else previewURL = null;

                books.add(new Books(title, author, date, description, imgURL, previewURL));
            }


        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return books;
    }

    private static URL createUrl(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){return jsonResponse;}

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }



        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while(line!=null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static String formatAuthors(String authors){

        //Handle Possible string formatting issues.
        String formatedAuthors;
        formatedAuthors = authors.replace("[", "").replace("]", "").replace("\"", "").replace(",", ", ");

        return formatedAuthors;
    }

    private static Bitmap getBitmap(String imgURL){

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(imgURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
