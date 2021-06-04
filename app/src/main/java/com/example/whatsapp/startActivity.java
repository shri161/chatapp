package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startActivity extends AppCompatActivity {
  private Button RegisterBtn;
  private Button LoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_start );
      RegisterBtn=(Button) findViewById( R.id.button );
      LoginBtn=(Button) findViewById( R.id. login);
      RegisterBtn.setOnClickListener( new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent =new Intent(startActivity.this,registerActivity.class);
              startActivity( intent );
          }
      } );
      LoginBtn.setOnClickListener( new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent =new Intent(startActivity.this,loginActivity.class);
              startActivity( intent );
          }
      } );
    }
}