package com.example.padelhub.persistencia

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.compose.ui.platform.LocalContext
import com.example.padelhub.MainActivity
import com.example.padelhub.persistencia.easychat.LoginUsernameActivity
import com.example.padelhub.persistencia.easychat.utils.AndroidUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class GestionLoginChat {

    var mAuth = FirebaseAuth.getInstance()
    var timeoutSeconds = 60L
    var verificationCode: String? = null
    var resendingToken: ForceResendingToken? = null

    var otpInput: EditText? = null
    var nextBtn: Button? = null
    var progressBar: ProgressBar? = null
    var resendOtpTextView: TextView? = null
    fun sendOtp(phoneNumber: String, isResend: Boolean, context: Context) {
        //startResendTimer()
        //setInProgress(true)
        val builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            //.setActivity(MainActivity)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                    //setInProgress(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    AndroidUtil.showToast(context, "OTP verification failed")
                    //setInProgress(false)
                }

                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    resendingToken = forceResendingToken
                    AndroidUtil.showToast(context, "OTP sent successfully")
                    //setInProgress(false)
                }
            })
        if (isResend) {
            resendingToken?.let { builder.setForceResendingToken(it).build() }?.let {
                PhoneAuthProvider.verifyPhoneNumber(
                    it
                )
            }
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    /*fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE)
            nextBtn.setVisibility(View.GONE)
        } else {
            progressBar.setVisibility(View.GONE)
            nextBtn.setVisibility(View.VISIBLE)
        }
    }*/

    fun signIn(phoneAuthCredential: PhoneAuthCredential?) {
        //login and go to next activity
        //setInProgress(true)
        if (phoneAuthCredential != null) {
            mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                    //setInProgress(false)

                })
        }
    }

    /*fun startResendTimer() {
        resendOtpTextView?.setEnabled(false)
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeoutSeconds--
                resendOtpTextView?.setText("Resend OTP in $timeoutSeconds seconds")
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 60L
                    timer.cancel()
                    runOnUiThread { resendOtpTextView?.setEnabled(true) }
                }
            }
        }, 0, 1000)
    }*/
}