package com.fazdate.social.services.firebaseServices;

import com.fazdate.social.helpers.CollectionNames;
import com.fazdate.social.helpers.Names;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class FirestoreService {
    private final Firestore firestore = FirestoreClient.getFirestore();
    private final Logger LOGGER = LoggerFactory.getLogger(FirestoreService.class);

    /**
     * Adds a new document to the given collection.
     */
    public <T> void addDocumentToCollection(Names name, T data, String documentId) {
        getCollection(name).document(documentId).set(data);
    }

    /**
     * Returns a document from the given collection in an object
     */
    public <T> T getObjectFromDocument(Names name, Class<T> classType, String documentId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection(name).document(documentId);
        DocumentSnapshot document = documentReference.get().get();
        T object = null;
        if (document.exists()) {
            object = document.toObject(classType);
        }
        return object;
    }

    /**
     * Updates a document in the given collection. It will merge the existing and the given data.
     */
    public <T> void updateDocumentInCollection(Names name, T data, String documentId) {
        getCollection(name).document(documentId).set(data, SetOptions.merge());
    }

    /**
     * Deletes the given document from the given collection
     */
    public void deleteDocumentFromCollection(Names name, String documentId) {
        getCollection(name).document(documentId).delete();
    }

    /**
     * Returns with a list of every document in the given collection
     */
    public List<QueryDocumentSnapshot> getEveryDocumentFromCollection(Names name) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = getCollection(name).get();
        QuerySnapshot querySnapshot = query.get();
        return querySnapshot.getDocuments();
    }

    /**
     * Checks if the given documentId is in the given collection.
     * It will return true if its in the collection
     */
    public boolean checkIfDocumentExistsInCollection(Names name, String documentId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documentSnapshots = getEveryDocumentFromCollection(name);
        for (QueryDocumentSnapshot documentReference : documentSnapshots) {
            if (documentReference.getId().equals(documentId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the whole collection that correlates to the given enum
     */
    private CollectionReference getCollection(Names name) {
        return firestore.collection(Objects.requireNonNull(CollectionNames.getCollectionName(name)));
    }

    /**
     * Deletes the whole collection that correlates to the given enum
     */
    public void deleteCollection(Names name) {
        try {
            CollectionReference collection = getCollection(name);
            ApiFuture<QuerySnapshot> future = collection.limit(1024).get();
            int deleted = 0;
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
                LOGGER.info("Document with documentId " + document.getId() + " was deleted!");
                ++deleted;
            }
            if (deleted >= 1024) {
                deleteCollection(name);
            }
        } catch (Exception e) {
            LOGGER.warn("Error deleting collection : " + e.getMessage());
        }
    }
}
