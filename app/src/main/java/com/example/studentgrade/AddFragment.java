package com.example.studentgrade;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    private Button buttonAdd;
    private EditText editTextName, editTextGrade, editTextInfo;
    private StudentDatabase studentDatabase;
    private StudentDao studentDao;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonAdd = requireActivity().findViewById(R.id.buttonAdd);
        editTextName = requireActivity().findViewById(R.id.editTextName);
        editTextGrade = requireActivity().findViewById(R.id.editTextGrade);
        editTextInfo = requireActivity().findViewById(R.id.editTextInfo);
        buttonAdd.setEnabled(false);
        editTextName.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editTextName, 0);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = editTextName.getText().toString().trim();
                String grade = editTextGrade.getText().toString().trim();
                buttonAdd.setEnabled(!name.isEmpty() && !grade.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editTextName.addTextChangedListener(textWatcher);
        editTextGrade.addTextChangedListener(textWatcher);
        buttonAdd.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String grade = editTextGrade.getText().toString().trim();
            String info = editTextInfo.getText().toString().trim();
            Student student = new Student(name, grade, info);

            studentDatabase = Room.databaseBuilder(requireActivity().getApplicationContext(), StudentDatabase.class, "student_database")
                    .allowMainThreadQueries()
                    .build();
            studentDao = studentDatabase.getStudentDao();
            studentDao.insertStudents(student);

            NavController navController = Navigation.findNavController(v);  //返回界面
            navController.navigateUp();
        });
    }
}