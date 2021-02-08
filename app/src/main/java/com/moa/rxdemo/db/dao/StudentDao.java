package com.moa.rxdemo.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import android.database.Cursor;

import com.moa.rxdemo.db.entity.Student;

import java.util.List;


/**
 * 类或文件描述
 * <p>
 * Created by：wangjian on 2018/12/27 18:01
 */
@Dao
public interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudent(Student user);
    
    @Delete
    void deleteStudent(Student user);
    
    @Query("SELECT * FROM student")
    LiveData<List<Student>> loadStudents();
    
    //==========以下测试provider的使用===============
    
    @Query("SELECT * FROM student")
    Cursor loadStudentsCursor();
    
    @Query("SELECT * FROM student where bookid=:bookid")
    Cursor getStudentById(String bookid);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertStudent2(Student student);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllStudent(Student[] students);
    
    @Query("DELETE FROM student WHERE uid=:uid")
    int deleteStudent2(String uid);
    
    @Update
    int updateStudent2(Student user);
    
    
    
}
