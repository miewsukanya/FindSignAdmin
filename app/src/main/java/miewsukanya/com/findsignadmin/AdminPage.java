package miewsukanya.com.findsignadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminPage extends AppCompatActivity {
//Explicit
    ImageView imgInsert,imgEdit, imgDel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        //BindWidget
        imgInsert = (ImageView) findViewById(R.id.imgInsert);
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        imgDel = (ImageView) findViewById(R.id.imgDel);

        //imgInsert controller
        imgInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPage.this, InsertActivity.class));
            }
        });
        //imgEdit controller
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPage.this,EditActivity.class));
            }
        });
        //imgDel controller
        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPage.this,DelActivity.class));
            }
        });
    }//Main Method
}//Main Class
