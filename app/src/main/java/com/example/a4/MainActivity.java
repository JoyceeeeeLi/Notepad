package com.example.a4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener{

    private MyAdapter myAdapter;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ArrayList<String>> myDataSet;
    private int counter=0;
    private String currentFileName;
    private boolean resume = false;
    private boolean delete = false;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDataSet = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.notes_list);
        recyclerView.setHasFixedSize(true);

        // layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(this, myDataSet);
        myAdapter.setClickListener(this);
        recyclerView.setAdapter(myAdapter);

        // add new note
        FloatingActionButton myButton = findViewById(R.id.fab);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // file name
                String filename = "file" + Integer.toString(counter);
                ++counter;
                currentFileName = filename;

                // add notes
                ArrayList<String> newNote = new ArrayList<>();
                newNote.add("My Note");
                newNote.add("");
                newNote.add(filename);
                myDataSet.add(0, newNote);
                myAdapter.notifyItemInserted(0);

                // go to activity 2
                resume = true;
                goToEditPage("My Note", "", filename);
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();

        if(resume) {

            // update title/contents
            ArrayList<String> editNote = new ArrayList<>();

            // read from file
            try {
                String titleFileName = "title" + currentFileName + ".txt";

                FileInputStream titleFileInputStream = openFileInput(titleFileName);
                InputStreamReader titleInputStreamReader = new InputStreamReader(titleFileInputStream);

                BufferedReader titleBufferedReader = new BufferedReader(titleInputStreamReader);
                StringBuffer titleStringBuffer = new StringBuffer();

                String lines;
                while ((lines = titleBufferedReader.readLine()) != null) {
                    titleStringBuffer.append(lines + "\n");
                }

                String contentFileName = "content" + currentFileName + ".txt";

                FileInputStream contentFileInputStream = openFileInput(contentFileName);
                InputStreamReader contentInputStreamReader = new InputStreamReader(contentFileInputStream);

                BufferedReader contentBufferedReader = new BufferedReader(contentInputStreamReader);
                StringBuffer contentStringBuffer = new StringBuffer();

                String lines2;
                while ((lines2 = contentBufferedReader.readLine()) != null) {
                    contentStringBuffer.append(lines2 + "\n");
                }

                if(titleStringBuffer.toString().isEmpty() && contentStringBuffer.toString().isEmpty()){
                    myDataSet.remove(currentPosition);
                    myAdapter.notifyItemRemoved(currentPosition);
                    resume = false;
                    currentPosition=0;
                    return;
                }

                editNote.add(titleStringBuffer.toString());
                editNote.add(contentStringBuffer.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // test
            myDataSet.set(currentPosition, editNote);
            myAdapter.notifyItemChanged(currentPosition);
            resume = false;
            currentPosition=0;
        }

    }

    @Override
    public void onItemClick(View view, int position) {

            String title = myAdapter.getItem(position).get(0);
            String content = myAdapter.getItem(position).get(1);

            // new file name
            String filename = "file" + Integer.toString(counter);
            ++counter;
            currentFileName = filename;
            currentPosition = position;

            // got to second activity
            resume = true;
            goToEditPage(title, content, filename);

    }

    public void goToEditPage(String title, String content, String filename){
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("filename", filename);
        startActivity(intent);
    }

}