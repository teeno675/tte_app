package com.teeno.tteapp.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

sealed class UploadState {
    object Idle : UploadState()
    data class Progress(val percent: Int) : UploadState()
    data class Success(val downloadUrl: String) : UploadState()
    data class Error(val message: String) : UploadState()
}

class MediaUploadRepository {

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference

    /**
     * Uploads a photo or video to Firebase Storage.
     * Path structure: media/{uuid}.{extension}
     */
    fun uploadMedia(uri: Uri, isVideo: Boolean): Flow<UploadState> = callbackFlow {
        trySend(UploadState.Idle)

        val extension = if (isVideo) "mp4" else "jpg"
        val fileName = "media/${UUID.randomUUID()}.$extension"
        val fileRef = storageRef.child(fileName)

        val uploadTask = fileRef.putFile(uri)

        uploadTask.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
            trySend(UploadState.Progress(progress))
        }.addOnSuccessListener {
            // Get download URL after successful upload
            fileRef.downloadUrl
                .addOnSuccessListener { downloadUri ->
                    trySend(UploadState.Success(downloadUri.toString()))
                    close()
                }
                .addOnFailureListener { e ->
                    trySend(UploadState.Error(e.message ?: "Failed to get download URL"))
                    close()
                }
        }.addOnFailureListener { e ->
            trySend(UploadState.Error(e.message ?: "Upload failed"))
            close()
        }

        awaitClose {
            // Optional: cancel upload if flow is cancelled
            // uploadTask.cancel()
        }
    }

    /**
     * Simple suspend version if you prefer not using Flow.
     */
    suspend fun uploadMediaSuspend(uri: Uri, isVideo: Boolean): Result<String> {
        return try {
            val extension = if (isVideo) "mp4" else "jpg"
            val fileName = "media/${UUID.randomUUID()}.$extension"
            val fileRef = storageRef.child(fileName)

            fileRef.putFile(uri).await()
            val downloadUrl = fileRef.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
