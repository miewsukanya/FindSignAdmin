package miewsukanya.com.findsignadmin;

/**
 * Created by Sukanya Boonpun on 26/01/2560.
 */
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sukanya Boonpun on 21/09/2559.
 */
public class InsertBackground extends AsyncTask<String, Void, String> {
    Context context;
    //AlertDialog alertDialog;
    InsertBackground (Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        //url จากของ server
        String insert_url = "http://202.28.94.32/2559/563020232-9/add2.php";
        if (type.equals("insert")) {
            try {
                String str_signname = params[1];
                String str_latitude = params[2];
                String str_longitude = params[3];
                String str_adId = params[4];
                URL url = new URL(insert_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("SignName", "UTF-8") + "=" + URLEncoder.encode(str_signname, "UTF-8") + "&"
                        + URLEncoder.encode("Latitude", "UTF-8") + "=" + URLEncoder.encode(str_latitude, "UTF-8") + "&"
                        + URLEncoder.encode("Longitude", "UTF-8") + "=" + URLEncoder.encode(str_longitude, "UTF-8") + "&"
                        + URLEncoder.encode("AdID", "UTF-8") + "=" + URLEncoder.encode(str_adId, "UTF-8");
                //SignName,Latitude,Longitude,AdID ชื่อคอลัมในฐานข้อมูล
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//if
        else if (type.equals("edit")){
            try {
                String str_signname = params[1];
                String str_latitude = params[2];
                String str_longitude = params[3];
                String str_adId = params[4];
                URL url = new URL(insert_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("SignName", "UTF-8") + "=" + URLEncoder.encode(str_signname, "UTF-8") + "&"
                        + URLEncoder.encode("Latitude", "UTF-8") + "=" + URLEncoder.encode(str_latitude, "UTF-8") + "&"
                        + URLEncoder.encode("Longitude", "UTF-8") + "=" + URLEncoder.encode(str_longitude, "UTF-8") + "&"
                        + URLEncoder.encode("AdID", "UTF-8") + "=" + URLEncoder.encode(str_adId, "UTF-8");
                //SignName,Latitude,Longitude,AdID ชื่อคอลัมในฐานข้อมูล
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//else if

        return null;
    }//doInBackground

    @Override
    protected void onPreExecute() {
       // alertDialog = new AlertDialog.Builder(context).create();
       // alertDialog.setTitle("เพิ่มข้อมูลป้าย");
       // alertDialog.setMessage("เพิ่มข้อมูลสำเร็จ");
    }

    @Override
    protected void onPostExecute(String result) {
       // alertDialog.setMessage(result);
        //alertDialog.show();
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}//main class