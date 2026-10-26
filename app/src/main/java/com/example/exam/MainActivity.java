package com.example.exam;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtStudentID;
    Spinner spinnerMajor;

    Button btnAdd, btnEdit, btnDelete, btnView;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtStudentID = findViewById(R.id.edtStudentID);

        spinnerMajor = findViewById(R.id.spinnerMajor);

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        databaseHelper = new DatabaseHelper(this);

        setupSpinner();

        btnAdd.setOnClickListener(v -> addStudent());

        btnEdit.setOnClickListener(v -> editStudent());

        btnDelete.setOnClickListener(v -> deleteStudent());

        btnView.setOnClickListener(v -> viewStudents());
    }

    private void setupSpinner() {

        String[] majors = {
                "Informatics",
                "Information Systems"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        majors
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerMajor.setAdapter(adapter);
    }

    private void addStudent() {

        String name = edtName.getText().toString().trim();
        String studentId = edtStudentID.getText().toString().trim();
        String major = spinnerMajor.getSelectedItem().toString();

        if (name.isEmpty() || studentId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please complete all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean inserted =
                databaseHelper.addStudent(
                        name,
                        studentId,
                        major
                );

        if (inserted) {

            Toast.makeText(
                    this,
                    "Student Added Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

        } else {

            Toast.makeText(
                    this,
                    "Failed to Add Student",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void editStudent() {

        String name = edtName.getText().toString().trim();
        String studentId = edtStudentID.getText().toString().trim();
        String major = spinnerMajor.getSelectedItem().toString();

        if (name.isEmpty() || studentId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter Student Name and Student ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean updated =
                databaseHelper.updateStudent(
                        name,
                        studentId,
                        major
                );

        if (updated) {

            Toast.makeText(
                    this,
                    "Student Updated Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

        } else {

            Toast.makeText(
                    this,
                    "Student ID Not Found",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void deleteStudent() {

        String studentId =
                edtStudentID.getText().toString().trim();

        if (studentId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter Student ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean deleted =
                databaseHelper.deleteStudent(studentId);

        if (deleted) {

            Toast.makeText(
                    this,
                    "Student Deleted Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

        } else {

            Toast.makeText(
                    this,
                    "Student ID Not Found",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void viewStudents() {

        Cursor cursor;
        cursor = databaseHelper.getAllStudents();

        if (cursor.getCount() == 0) {

            showMessage(
                    "No student data found."
            );

            cursor.close();

            return;
        }

        StringBuilder builder =
                new StringBuilder();

        while (cursor.moveToNext()) {

            builder.append("Student Name: ")
                    .append(cursor.getString(1))
                    .append("\n");

            builder.append("Student ID: ")
                    .append(cursor.getString(2))
                    .append("\n");

            builder.append("Major: ")
                    .append(cursor.getString(3))
                    .append("\n");

            builder.append("----------------------------")
                    .append("\n");
        }

        cursor.close();

        showMessage(
                builder.toString()
        );
    }

    private void showMessage(
            String message
    ) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Student Data");

        builder.setMessage(message);

        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                (dialog, which) -> dialog.dismiss()
        );

        builder.show();
    }

    private void clearFields() {

        edtName.setText("");
        edtStudentID.setText("");

        spinnerMajor.setSelection(0);
    }
}