package com.example.taskmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements TaskViewAdapter.OnTaskListener {
    RecyclerView recyclerView;
    TaskViewAdapter taskViewAdapter;
    Button button_add_task;
    SharedPreferences sharedPreferences;
    String TASK_SAVED;
    static ArrayList<Task> taskList = new ArrayList<>();
    public void jumpToAdd (View view,boolean edit) {
        Intent intent = new Intent(this,AddTask.class);
        intent.putExtra("edit",false);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("com.example.taskmanager",MODE_PRIVATE);
        try {
            getTasks();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        button_add_task = findViewById(R.id.button_add_task);
        recyclerView = findViewById(R.id.recyclerView);
        taskViewAdapter = new TaskViewAdapter(taskList,this,this);
        recyclerView.setAdapter(taskViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToAdd(v,false);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Receive data from add edit
        Intent intent = getIntent();
        int intentIndex = intent.getIntExtra("index",0);
        String intentTaskName = intent.getStringExtra("taskName");
        String intentTaskDesc = intent.getStringExtra("taskDesc");
        long intentTaskDate = intent.getLongExtra("taskDate",0L);
        int intentExecute = intent.getIntExtra("execute",0);
        if (intentExecute == 1) {
            addTask(intentTaskName,intentTaskDesc,intentTaskDate);
            saveTasks(taskList);
        }
        if (intentExecute == 2) {
            editTask(intentIndex,intentTaskName,intentTaskDesc,intentTaskDate);
            saveTasks(taskList);
        }
        taskList.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Long.compare(o1.getDate(), o2.getDate());
            }
        });
        taskViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getIntent().replaceExtras(new Bundle());
        getIntent().setAction("");
        getIntent().setData(null);
        getIntent().setFlags(0);
        saveTasks(taskList);
    }

    public void addTask(String name, String desc, long date) {
        int index = taskList.size();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        formatter.setTimeZone(TimeZone.getDefault());
        String dateFormatted = formatter.format(date);

        Task export;
        export = new Task(
                index,
                name,
                desc,
                dateFormatted,
                date
        );
        taskList.add(export);
        taskViewAdapter.notifyItemInserted(index);
    }
    public void editTask(int index, String name, String desc, long date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        formatter.setTimeZone(TimeZone.getDefault());
        String dateFormatted = formatter.format(date);

        Task export;
        export = new Task(
                index,
                name,
                desc,
                dateFormatted,
                date
        );
        taskList.set(index,export);
        taskViewAdapter.notifyItemChanged(index);
    }

    public void saveTasks(ArrayList<Task> taskList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString(TASK_SAVED, json);
        editor.apply();
    }
    public void getTasks() throws JSONException {
        Gson gson = new Gson();
        String tasks = sharedPreferences.getString(TASK_SAVED,"");
        JSONArray jsonArray = new JSONArray(tasks);
        if (taskList.size() != jsonArray.length()) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                int id = obj.getInt("id");
                String name = obj.getString("name");
                String desc = obj.getString("desc");
                String due_date = obj.getString("due_date");
                long date = obj.getLong("date");
                Task task = new Task(id,name,desc,due_date,date);
                taskList.add(task);
            }
        }
    }

    @Override
    public void onTaskClick(int position) {
        Task input = taskList.get(position);
        Intent intent = new Intent(this, AddTask.class);
        intent.putExtra("index",position);
        intent.putExtra("id",input.getId());
        intent.putExtra("taskName",input.getName());
        intent.putExtra("taskDesc",input.getDesc());
        intent.putExtra("taskDate",input.getDate());
        intent.putExtra("edit",true);
        startActivity(intent);
    }
}