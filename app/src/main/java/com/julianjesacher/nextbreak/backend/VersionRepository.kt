package com.julianjesacher.nextbreak.backend

import android.content.Context
import com.google.gson.Gson
import com.julianjesacher.nextbreak.config.AppConstants
import com.julianjesacher.nextbreak.model.Version
import com.julianjesacher.nextbreak.utils.ConnectivityChecker
import okio.IOException
import retrofit2.HttpException

sealed class CheckVersionResult {
    data class Success(val updateAvailable: Boolean) : CheckVersionResult()
    object NoInternet : CheckVersionResult()
    object Error : CheckVersionResult()
}

object VersionRepository {
    private var newestVersion: Version? = null

    suspend fun checkVersion(context: Context): CheckVersionResult {
        val response = try {
            RetrofitInstance.api.getVersion()
        } catch (e: IOException) {
            return if(ConnectivityChecker.hasInternetConnection(context)) {
                CheckVersionResult.Error
            }
            else {
                CheckVersionResult.NoInternet
            }
        } catch (e: HttpException) {
            return CheckVersionResult.Error
        }

        if(response.isSuccessful && response.body() != null) {
            val onlineVersion = response.body()
            val localVersion = getLocalVersion()

            if(onlineVersion == null) {
                return CheckVersionResult.Error
            }

            newestVersion = onlineVersion
            if(localVersion == null) {
                return CheckVersionResult.Success(true)
            }

            return CheckVersionResult.Success(isNewerVersion(localVersion, onlineVersion))
        } else {
            return CheckVersionResult.Error
        }
    }

    suspend fun saveVersionLocally() {
        if(newestVersion == null) {
            return
        }

        val jsonString = Gson().toJson(newestVersion)
        FileManager.saveFile(AppConstants.VERSION_FILE_NAME, jsonString)
    }

    suspend fun getLocalVersion(): Version? {
        val jsonString = FileManager.loadFile(AppConstants.VERSION_FILE_NAME)
        if(jsonString != null) {
            return Gson().fromJson(jsonString, Version::class.java)
        }

        return null
    }

    private fun isNewerVersion(localVersion: Version, onlineVersion: Version): Boolean {
        if(onlineVersion.year > localVersion.year) {
            return true
        }
        if(onlineVersion.hotfix > localVersion.hotfix) {
            return true
        }

        return false
    }
}