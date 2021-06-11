//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.utils

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.yosemiteyss.greentransit.domain.states.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Created by kevin on 8/25/20
 */

private const val TAG = "FirebaseExtensions"

const val ERROR_DOCUMENT_NOT_EXIST = "Document does not exist."
const val ERROR_NETWORK_UNAVAILABLE = "Network error."

internal fun QuerySnapshot.isNetworkData(): Boolean {
    return !metadata.isFromCache
}

internal suspend inline fun <reified T, R> CollectionReference.getAwaitResult(mapper: (T) -> R): List<R> {
    return get()
        .await()
        .toObjects(T::class.java)
        .map { mapper(it) }
}

internal suspend inline fun <reified T, R> DocumentReference.getAwaitResult(mapper: (T) -> R): R {
    val result = get()
        .await()
        .toObject(T::class.java)
        ?: throw Exception(ERROR_DOCUMENT_NOT_EXIST)

    return mapper(result)
}

internal suspend inline fun <reified T, R> Query.getAwaitResult(mapper: (T) -> R): List<R> {
    return get()
        .await()
        .toObjects(T::class.java)
        .map { mapper(it) }
}

internal inline fun <reified T, R> CollectionReference.snapshotFlow(
    crossinline mapper: (T) -> R,
    keepAlive: Boolean = false
): Flow<Resource<List<R>>> = callbackFlow {
    channel.trySend(Resource.Loading())

    val listener = this@snapshotFlow.addSnapshotListener { snapshot, error ->
        if (error != null) {
            Log.d(TAG, "${error.message}")
            channel.trySend(Resource.Error(error.message))
            channel.close(CancellationException(error.message))
        } else {
            snapshot?.let { snap ->
                if (snap.isEmpty && snap.metadata.isFromCache) {
                    channel.trySend(Resource.Error(ERROR_NETWORK_UNAVAILABLE))
                    if (!keepAlive) {
                        channel.close()
                    }
                } else if (!snap.metadata.hasPendingWrites()) {
                    val results = snap.toObjects(T::class.java)
                    val successResult = Resource.Success(results.map { result -> mapper(result) })
                    channel.trySend(successResult)

                    if (results.isEmpty() && !keepAlive) {
                        channel.close()
                    }
                }
            } ?: channel.close(CancellationException(ERROR_DOCUMENT_NOT_EXIST))
        }
    }

    awaitClose { listener.remove() }
}

internal inline fun <reified T, R> DocumentReference.snapshotFlow(
    crossinline mapper: (T) -> R,
    keepAlive: Boolean = false
): Flow<Resource<R>> = callbackFlow {
    channel.trySend(Resource.Loading())

    val listener = this@snapshotFlow.addSnapshotListener { snapshot, error ->
        if (error != null) {
            Log.d(TAG, "${error.message}")
            channel.trySend(Resource.Error(error.message))
            channel.close(CancellationException(error.message))
        } else {
            snapshot?.let { snap ->
                if (!snap.metadata.hasPendingWrites()) {
                    val result = snap.toObject(T::class.java)
                    if (result == null) {
                        channel.trySend(Resource.Error(ERROR_DOCUMENT_NOT_EXIST))

                        if (!keepAlive) {
                            channel.close(CancellationException(ERROR_DOCUMENT_NOT_EXIST))
                        }
                    } else {
                        channel.trySend(Resource.Success(mapper(result)))
                    }
                }
            } ?: channel.close(CancellationException(ERROR_DOCUMENT_NOT_EXIST))
        }
    }

    awaitClose { listener.remove() }
}

internal inline fun <reified T, R> Query.snapshotFlow(
    crossinline mapper: (T) -> R,
    keepAlive: Boolean = false
): Flow<Resource<List<R>>> = callbackFlow {
    channel.trySend(Resource.Loading())

    val listener = this@snapshotFlow.addSnapshotListener { snapshot, error ->
        if (error != null) {
            Log.d(TAG, "${error.message}")
            channel.trySend(Resource.Error(error.message))
            channel.close(CancellationException(error.message))
        } else {
            snapshot?.let { snap ->
                if (snap.isEmpty && snap.metadata.isFromCache) {
                    channel.trySend(Resource.Error(ERROR_NETWORK_UNAVAILABLE))
                    if (!keepAlive) {
                        channel.close()
                    }
                } else if (!snap.metadata.hasPendingWrites()) {
                    val results = snap.toObjects(T::class.java)
                    val successResult = Resource.Success(results.map { result -> mapper(result) })
                    channel.trySend(successResult)

                    if (results.isEmpty() && !keepAlive) {
                        channel.close()
                    }
                }
            } ?: channel.close(CancellationException(ERROR_DOCUMENT_NOT_EXIST))
        }
    }

    awaitClose { listener.remove() }
}

/*
internal fun StorageReference.putFileFlow(uri: Uri): Flow<Resource<Unit>> = callbackFlow {
    val progressListener = OnProgressListener<UploadTask.TaskSnapshot> {
        val progress = 100.0 * it.bytesTransferred / it.totalByteCount
        channel.trySend(Resource.Loading(progress))
    }
    val successListener = OnSuccessListener<UploadTask.TaskSnapshot> {
        channel.trySend(Resource.Success(Unit))
        channel.close()
    }
    val failureListener = OnFailureListener {
        channel.trySend(Resource.Error(it.message))
        channel.close(CancellationException(it.message))
    }

    val uploadTask = this@putFileFlow.putFile(uri)
        .addOnProgressListener(progressListener)
        .addOnSuccessListener(successListener)
        .addOnFailureListener(failureListener)

    awaitClose {
        uploadTask.run {
            removeOnProgressListener(progressListener)
            removeOnSuccessListener(successListener)
            removeOnFailureListener(failureListener)
        }
    }
}

 */
