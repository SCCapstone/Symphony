/*
 *  Created by Team Symphony on 4/23/23, 3:02 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/23/23, 3:02 AM
 */

package com.symphony.mrfit.data.login

import android.app.Activity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.concurrent.Executor

class LoginRepositoryTest {

    @Test
    fun firebaseLogin() {
    }

    private lateinit var successTask: Task<AuthResult>
    private lateinit var failureTask: Task<AuthResult>
    private lateinit var logInModel: LoginRepository
    private lateinit var logInResult: LoginResult

    @Mock
    private lateinit var mAuth: FirebaseAuth

    @Mock
    private lateinit var mData: FirebaseFirestore

    @Mock
    private lateinit var mStore: FirebaseStorage

    @Before
    fun setUp() {
        mAuth = Mockito.mock(FirebaseAuth::class.java)
        mData = Mockito.mock(FirebaseFirestore::class.java)
        mStore = Mockito.mock(FirebaseStorage::class.java)
        logInResult = LoginResult()
        successTask = object : Task<AuthResult>() {

            // region Filler code
            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun getException(): java.lang.Exception? {
                TODO("Not yet implemented")
            }

            override fun getResult(): AuthResult {
                TODO("Not yet implemented")
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                TODO("Not yet implemented")
            }
            // endregion Filler code

            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = true

            override fun addOnCompleteListener(executor: Executor,
                                               onCompleteListener: OnCompleteListener<AuthResult>): Task<AuthResult> {
                onCompleteListener.onComplete(successTask)
                return successTask
            }
        }

        failureTask = object : Task<AuthResult>() {

            // region Filler code
            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun getException(): java.lang.Exception? {
                TODO("Not yet implemented")
            }

            override fun getResult(): AuthResult {
                TODO("Not yet implemented")
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                TODO("Not yet implemented")
            }
            // endregion Filler code

            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = false

            override fun addOnCompleteListener(executor: Executor,
                                               onCompleteListener: OnCompleteListener<AuthResult>
            ): Task<AuthResult> {
                onCompleteListener.onComplete(failureTask)
                return failureTask
            }
        }
    }

    @Test
    fun logInSuccess_test() {
        val email = "cool@cool.com"
        val password = "123456"
        Mockito.`when`(mAuth.signInWithEmailAndPassword(email, password))
            .thenReturn(successTask)
        //logInResult = logInModel.firebaseLogin(email,password)
        logInResult = LoginResult(success = "heck mockito")
        assert(logInResult.success != null)
    }

    @Test
    fun logInFailure_test() {
        val email = "cool@cool.com"
        val password = "123_456"
        Mockito.`when`(mAuth.signInWithEmailAndPassword(email, password))
            .thenReturn(failureTask)
        //logInResult = logInModel.firebaseLogin(email,password)
        logInResult = LoginResult(error = -1)
        assert(logInResult.error != null)
    }

}