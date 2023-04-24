/*
 *  Created by Team Symphony on 4/24/23, 3:50 AM
 *  Copyright (c) 2023 . All rights reserved.
 *  Last modified 4/24/23, 3:49 AM
 */

package com.symphony.mrfit

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.symphony.mrfit.data.login.LoginRepository
import org.junit.Before
import org.mockito.Mockito.mock


class FirebaseAuthTest {
    private lateinit var loginRepository: LoginRepository
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockActivity: Activity
    private lateinit var mockUser: MutableLiveData<User>

    @Before
    fun setup() {
        loginRepository = LoginRepository()
        mockAuth = mock(FirebaseAuth::class.java)
        mockActivity = mock(Activity::class.java)
        mockUser = MutableLiveData()
    }
    /*       @Test
           fun testLoginSuccess(){
               val email = "test@example.com"
               val password = "password"
               val authResult=loginRepository.firebaseLogin(mockActivity,email,password,mockUser)
               assertEquals(true,authResult)
               assertEquals(mockUser.value, email)
           }
           @Test
           fun testLoginFailure(){
               val email = "test@example.com"
               val password = "password"
               val authResult =loginRepository.firebaseLogin(mockActivity, email, password, mockUser)
               assertEquals(false,authResult)
               assertEquals(mockUser.value,null)
           }
           @Test
           fun loginSuccess() {
               `when`(mockAuth.signInWithEmailAndPassword(email, password))
                   .thenReturn(mockk())
               val authResult = loginRepository.firebaseLogin(mockAuth, email, password)
               assertEquals(authResult.user, mockAuth.currentUser)
           }

           @Test
           fun loginFailure() {
               `when`(mockAuth.signInWithEmailAndPassword(email, password))
                   .thenThrow(FirebaseAuthException("Invalid email or password",email))
               try{
                   loginRepository.firebaseLogin(mockAuth,email,password)
                   fail("Expected FirebaseAuthException")
               } catch (e: FirebaseAuthException){
                   assertEquals("Invalid email or password", e.message)
               }
               val authResult = loginRepository.firebaseLogin(mockAuth, email, password)
               assertEquals(authResult.user, mockAuth.currentUser)
           }


     */
}