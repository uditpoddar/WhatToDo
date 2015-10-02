package com.uditpoddar.android.whattodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WhatToDoList extends ActionBarActivity {

    private static final String tasksStorageFileName = "tasks.txt";

    private static final int REQUEST_CODE = 20;

    private ArrayList<String> tasks;

    private ArrayAdapter<String> tasksAdapter;

    private ListView lvToDoList;

    private EditText etNewTask;

    private Button btnAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_to_do_list);
        lvToDoList = (ListView)findViewById(R.id.lvToDoList);
        etNewTask = (EditText)findViewById(R.id.etNewTask);
        btnAddTask = (Button)findViewById(R.id.btnAddTask);
        tasks = getTasksList(tasksStorageFileName);
        tasksAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        lvToDoList.setAdapter(tasksAdapter);
        setupClickListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_what_to_do_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTasks(){
        tasksAdapter.notifyDataSetChanged();
        saveTasksList(tasksStorageFileName, tasks);
    }

    private File getFile(String fileName) {
        File filesDir = getFilesDir();
        return new File(filesDir, fileName);
    }

    private ArrayList<String> getTasksList(String fileName) {
        File todoFile = getFile(fileName);
        try {
            return new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            return new ArrayList<String>();
        }
    }

    private boolean saveTasksList(String fileName, ArrayList<String> tasks) {
        File todoFile = getFile(fileName);
        try {
            FileUtils.writeLines(todoFile, tasks);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void setupClickListeners() {
        lvToDoList.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String task = tasks.get(position);
                        tasks.remove(position);
                        Toast.makeText(WhatToDoList.this, String.format("'%s' task removed", task), Toast.LENGTH_SHORT).show();
                        updateTasks();
                        return true;
                    }
                }
        );

        lvToDoList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent editTask = new Intent(WhatToDoList.this, EditTaskActivity.class);
                        editTask.putExtra(Constants.TASK, tasks.get(position));
                        editTask.putExtra(Constants.TASK_POS, position);
                        startActivityForResult(editTask, REQUEST_CODE);
                    }
                }
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String task = data.getStringExtra(Constants.TASK);
            int pos = data.getIntExtra(Constants.TASK_POS, 0);
            tasks.remove(pos);
            tasks.add(pos, task);
            updateTasks();
        }
    }

    public void addTask(View view) {
        tasks.add(etNewTask.getText().toString());
        etNewTask.setText("");
        updateTasks();
    }
}
