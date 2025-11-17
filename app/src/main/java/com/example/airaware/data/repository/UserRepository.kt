package com.example.airaware.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.airaware.data.local.User
import com.example.airaware.data.local.UserDao


class UserRepository(private val userDao: UserDao) {

    suspend fun saveFirebaseUser(uid: String, email: String) {
        userDao.insertUser(User(firebaseUid = uid, email = email))
    }

    suspend fun getLocalUser(uid: String): User? {
        return userDao.getUserByUid(uid)
    }
}
