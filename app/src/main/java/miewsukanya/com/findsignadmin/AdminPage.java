package miewsukanya.com.findsignadmin;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdminPage extends AppCompatActivity {
    //Explicit
    ImageView imgInsert,imgEdit, imgDel;
    TextView AdIdTextView,AdNameTextView;
    String AdIdString,AdNameString;
    private MyConstant myConstant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        myConstant = new MyConstant();

        //test intent data from mainActivity 27/01/17
        TextView textView = (TextView) findViewById(R.id.textView5);
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        textView.setText(message);
        textView.setTextSize(20);
        Log.d("Name", "Name :" + message);

        //BindWidget
        imgInsert = (ImageView) findViewById(R.id.imgInsert);
       // imgEdit = (ImageView) findViewById(R.id.imgEdit);
        imgDel = (ImageView) findViewById(R.id.imgDel);
        AdIdTextView = (TextView) findViewById(R.id.textView4);
        AdNameTextView = (TextView) findViewById(R.id.textView5);


        SynUser synUser = new SynUser(AdminPage.this);
        synUser.execute(myConstant.getUrlGetJSON());


        //imgInsert controller
        imgInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ส่งค่า AdminID ไป InsertActivity 29.01.17
                String adminID = AdIdTextView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                intent.putExtra("adminID", adminID);
                Log.d("adminID","ID:"+ adminID);
                startActivity(intent);
                //finish();
               // startActivity(new Intent(AdminPage.this, InsertActivity.class));
            }
        });
        /*imgEdit controller
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminID = AdIdTextView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("adminID", adminID);
                Log.d("adminID","ID:"+ adminID);
                startActivity(intent);
                //startActivity(new Intent(AdminPage.this,EditActivity.class));
            }
        });*/
        //imgDel controller
        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ส่งค่า AdminID ไป InsertActivity 29.01.17
                String adminID = AdIdTextView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), DelActivity.class);
                intent.putExtra("adminID", adminID);
                Log.d("adminID","ID:"+ adminID);
                startActivity(intent);
                //startActivity(new Intent(AdminPage.this,DelActivity.class));
            }
        });
    }//Main Method
    private class SynUser extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;
        private String[] nameStrings;
        private String[] adIDStrings;
        private String trueAdID;
        private boolean aBoolean = true;

        public SynUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                Log.d("13novV2", "e doIn ==> " + e.toString());
                return null;
            }
        }   // doInBack
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("13novV2", "JSON ==> " + s);
            try {
                //29.01.17
                AdNameString = AdNameTextView.getText().toString();

                JSONArray jsonArray = new JSONArray(s);
                nameStrings = new String[jsonArray.length()];
                adIDStrings = new String[jsonArray.length()];

                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //nameStrings[i] = jsonObject.getString("AdName");
                    adIDStrings[i] = jsonObject.getString("AdID");
                    Log.d("28JanV1", "AdID(" + adIDStrings[i] + ")");

                    //เปรียบเทียบค่าที่ส่งมาจากหน้า Main ว่าตรงกับ AdName ID เท่าไหร่ 29.01.17
                    if (AdNameString.equals(jsonObject.getString("AdName"))) {
                        trueAdID = jsonObject.getString("AdID");
                    }
                    //set text show AdminID 29.01.17
                    AdIdTextView.setText(trueAdID);
                }// for
            } catch (Exception e) {
                e.printStackTrace();
            }
        }   //onPost
    }   // SynUser
}//Main Class
