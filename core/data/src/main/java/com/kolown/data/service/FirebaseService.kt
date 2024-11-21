package com.kolown.data.service

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

interface FirebaseService {
    fun getCollection(collectionName: String): CollectionReference
    fun getStorage(): FirebaseStorage
}

class FirebaseServiceImpl (
    private val firebase: Firebase
) : FirebaseService {
    override fun getCollection(collectionName: String): CollectionReference {
        return firebase.firestore.collection(collectionName)
    }

    override fun getStorage(): FirebaseStorage = firebase.storage
}