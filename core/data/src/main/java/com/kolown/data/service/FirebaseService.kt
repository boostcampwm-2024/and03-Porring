package com.kolown.data.service

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

interface FirebaseService {
    fun getCollection(collectionName: String): CollectionReference
}

class FirebaseServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseService {
    override fun getCollection(collectionName: String): CollectionReference {
        return firestore.collection(collectionName)
    }
}