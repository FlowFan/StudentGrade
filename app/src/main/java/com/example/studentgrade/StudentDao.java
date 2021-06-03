package com.example.studentgrade;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    void insertStudents(Student... students);

    @Query("DELETE FROM STUDENT")
    void deleteAllStudents();

    @Query("SELECT * FROM STUDENT ORDER BY ID DESC")
    LiveData<List<Student>> getAllStudents();

    @Query("SELECT * FROM STUDENT WHERE name LIKE :pattern ORDER BY ID DESC")
    LiveData<List<Student>> findStudentsWithPattern(String pattern);
}
