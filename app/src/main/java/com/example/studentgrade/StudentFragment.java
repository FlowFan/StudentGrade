package com.example.studentgrade;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentFragment extends Fragment {
    private StudentDatabase studentDatabase;
    private StudentDao studentDao;
    private final MyAdapter myAdapter = new MyAdapter();
    private LiveData<List<Student>> allStudents;
    private LiveData<List<Student>> filteredStudents;

    public StudentFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clearData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("清空学生数据");
            builder.setPositiveButton("确定", (dialog, which) -> {
                studentDatabase = Room.databaseBuilder(requireActivity().getApplicationContext(), StudentDatabase.class, "student_database")
                        .allowMainThreadQueries()
                        .build();
                studentDao = studentDatabase.getStudentDao();
                studentDao.deleteAllStudents();
                allStudents = studentDao.getAllStudents();  //获取数据库列表
                allStudents.observe(getViewLifecycleOwner(), myAdapter::submitList);    //刷新界面
            });
            builder.setNegativeButton("取消", (dialog, which) -> {

            });
            builder.create();
            builder.show();
        } else if (item.getItemId() == R.id.exit) {
            requireActivity().finishAndRemoveTask();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();

                studentDatabase = Room.databaseBuilder(requireActivity().getApplicationContext(), StudentDatabase.class, "student_database")
                        .allowMainThreadQueries()
                        .build();
                studentDao = studentDatabase.getStudentDao();
                filteredStudents = studentDao.findStudentsWithPattern("%" + pattern + "%");
                filteredStudents.observe(getViewLifecycleOwner(), myAdapter::submitList);   //刷新界面
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_studentFragment_to_addFragment);
        });

        RecyclerView recyclerView = requireActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(myAdapter);

        studentDatabase = Room.databaseBuilder(requireActivity().getApplicationContext(), StudentDatabase.class, "student_database")
                .allowMainThreadQueries()
                .build();
        studentDao = studentDatabase.getStudentDao();
        allStudents = studentDao.getAllStudents();  //获取数据库列表
        allStudents.observe(getViewLifecycleOwner(), myAdapter::submitList);    //刷新界面
        recyclerView.smoothScrollToPosition(0);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onResume() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        super.onResume();
    }
}