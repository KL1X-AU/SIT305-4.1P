package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {
    EditText addedit_taskName, addedit_taskDesc;
    CalendarView addedit_calendar;
    Button addedit_button;
    TextView addedit_textheader;
    long dateReturn;
    boolean dateEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        boolean edit = intent.getBooleanExtra("edit",false);
        int index = intent.getIntExtra("index",0);
        int id = intent.getIntExtra("id",0);
        String editName = intent.getStringExtra("taskName");
        String editDesc = intent.getStringExtra("taskDesc");
        long editDate = intent.getLongExtra("taskDate",0L);

        addedit_textheader = findViewById(R.id.addedit_textheader);
        addedit_button = findViewById(R.id.addedit_button);
        addedit_taskName = findViewById(R.id.addedit_taskName);
        addedit_taskDesc = findViewById(R.id.addedit_taskDesc);
        addedit_calendar = findViewById(R.id.addedit_calendar);

        if (edit) {
            addedit_textheader.setText("EDIT TASK");
            addedit_taskName.setText(editName);
            addedit_taskDesc.setText(editDesc);
            addedit_button.setText("SAVE");
            addedit_calendar.setDate(editDate);
        }

        addedit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addedit_taskName.getText().toString().isEmpty() || addedit_taskDesc.getText().toString().isEmpty()) {
                    Toast.makeText(AddTask.this, "Please fill in both task name and description.", Toast.LENGTH_SHORT).show();
                }
                else if(!edit) {
                    if (!dateEdited) {
                        returnToMain(addedit_calendar.getDate());
                    }
                    else {
                        returnToMain(dateReturn);
                    }

                }
                else {
                    if (!dateEdited) {
                        editToMain(index,id,editDate);
                    }
                    else {
                        editToMain(index,id,dateReturn);
                    }

                }
            }
        });

        addedit_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(year,month,day);
                dateEdited = true;
                dateReturn = c.getTimeInMillis();
            }
        });
    }
    public void returnToMain(long date) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("taskName",addedit_taskName.getText().toString());
        intent.putExtra("taskDesc",addedit_taskDesc.getText().toString());
        intent.putExtra("taskDate",date);
        intent.putExtra("execute",1);
        startActivity(intent);
    }
    public void editToMain(int index,int id,long date) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("index",index);
        intent.putExtra("id",id);
        intent.putExtra("taskName",addedit_taskName.getText().toString());
        intent.putExtra("taskDesc",addedit_taskDesc.getText().toString());
        intent.putExtra("taskDate",date);
        intent.putExtra("execute",2);
        startActivity(intent);
    }
}