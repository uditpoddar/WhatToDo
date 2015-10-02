package com.uditpoddar.android.whattodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class EditTaskActivity extends ActionBarActivity {

    private EditText etEditTask;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        etEditTask = (EditText)findViewById(R.id.etEditTask);
        String editTask = getIntent().getStringExtra(Constants.TASK);
        position = getIntent().getIntExtra(Constants.TASK_POS, 0);
        etEditTask.setText(editTask);
    }

    public void sumbitChanges(View view) {
        Intent data = new Intent();
        String editedTask = etEditTask.getText().toString();
        data.putExtra(Constants.TASK, editedTask);
        data.putExtra(Constants.TASK_POS, position);
        setResult(RESULT_OK, data);
        finish();
    }
}
