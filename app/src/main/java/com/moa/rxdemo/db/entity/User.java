package com.moa.rxdemo.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * 创建users表
 * <p>
 * Created by：wangjian on 2018/12/19 14:05
 */
@Entity(tableName = "users")
public class User {
    public String name;
    public String address;
    public String phone;
    @PrimaryKey
    @NonNull
    public String id;
}
