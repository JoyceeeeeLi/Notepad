package com.example.a4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {
    private EditText title;
    private EditText content;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        this.title = findViewById(R.id.edit_title);
        this.content = findViewById(R.id.edit_content);

        Intent intent = getIntent();
        String newTitle = intent.getStringExtra("title");
        String newContent = intent.getStringExtra("content");
        String filename = intent.getStringExtra("filename");
        this.filename = filename;

        title.setText(newTitle);
        content.setText(newContent);

        FloatingActionButton myButton = findViewById(R.id.save);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        saveNote();
        super.onBackPressed();
    }

    private void saveNote(){
        String titleString = title.getText().toString();
        String contentString = content.getText().toString();

        if(titleString.trim().isEmpty()){
            try{
                // first 10 char of content
                String firstTenCharOfContentString;
                if(contentString.length() > 10){
                    firstTenCharOfContentString = contentString.substring(0, 10);
                } else {
                    firstTenCharOfContentString = contentString;
                }

                String titleFileName = "title" + filename + ".txt";

                FileOutputStream titleFileOutputStream = openFileOutput(titleFileName, MODE_PRIVATE);
                titleFileOutputStream.write(firstTenCharOfContentString.getBytes());
                titleFileOutputStream.close();

                String contentFileName = "content" + filename + ".txt";

                FileOutputStream contentFileOutputStream = openFileOutput(contentFileName, MODE_PRIVATE);
                contentFileOutputStream.write(contentString.getBytes());
                contentFileOutputStream.close();

            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

        } else {
            try{
                String titleFileName = "title" + filename + ".txt";
                FileOutputStream titleFileOutputStream = openFileOutput(titleFileName, MODE_PRIVATE);
                titleFileOutputStream.write(titleString.getBytes());
                titleFileOutputStream.close();

                String contentFileName = "content" + filename + ".txt";

                FileOutputStream contentFileOutputStream = openFileOutput(contentFileName, MODE_PRIVATE);

                contentFileOutputStream.write(contentString.getBytes());
                contentFileOutputStream.close();

            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}