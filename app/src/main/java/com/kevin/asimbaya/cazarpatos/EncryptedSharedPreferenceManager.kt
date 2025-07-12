package com.kevin.asimbaya.cazarpatos

import android.app.Activity
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptedSharedPreferenceManager(val actividad: Activity) : FileHandler {
    companion object {
        private const val LOGIN_KEY = "LOGIN_KEY"
        private const val PASSWORD_KEY = "PASSWORD_KEY"
        private const val PREF_FILE_NAME = "encrypted_prefs"
    }

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        PREF_FILE_NAME,
        masterKeyAlias,
        actividad.applicationContext, // Use applicationContext for EncryptedSharedPreferences
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        val editor = encryptedSharedPreferences.edit()
        editor.putString(LOGIN_KEY, datosAGrabar.first)
        editor.putString(PASSWORD_KEY, datosAGrabar.second)
        editor.apply()
    }

    override fun ReadInformation(): Pair<String, String> {
        val email = encryptedSharedPreferences.getString(LOGIN_KEY, "") ?: ""
        val clave = encryptedSharedPreferences.getString(PASSWORD_KEY, "") ?: ""
        return email to clave
    }
}