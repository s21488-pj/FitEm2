package com.example.fitem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Training> trainings;
    private TrainingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Inicjalizacja danych

        TrainingDBHelper dbHelper = new TrainingDBHelper(this);
        this.trainings = dbHelper.getAllTrainings();

        ListView listView = (ListView) findViewById(R.id.listView);
        // Inicjalizacja i ustawienie adaptera
        this.adapter = new TrainingAdapter(this, trainings,dbHelper);
        listView.setAdapter(adapter);

        // Obsługa kliknięcia elementu listy
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
                intent.putExtra("training", trainings.get(position));
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
        Button addTrainingButton = findViewById(R.id.addTrainingButton);

        addTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Nowy trening");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String trainingName = input.getText().toString();
                        Training newTraining = new Training(trainingName);

                        long newTrainingId = dbHelper.insertTraining(trainingName);
                        newTraining.setId((int) newTrainingId);

                        trainings.clear(); // Wyczyść listę treningów
                        trainings.addAll(dbHelper.getAllTrainings());

                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        // Obsługa przycisku edycji
        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Przełączanie trybu edycji
                adapter.setEditMode(!adapter.isInEditMode());
            }
        });
    }

}