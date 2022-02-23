package com.fazdate.social.services.firebaseServices;

import com.fazdate.social.helpers.CollectionNames;
import com.fazdate.social.helpers.Names;
import com.fazdate.social.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {
    private final Firestore firestore = FirestoreClient.getFirestore();
    private final AuthService authService = ServiceLocator.getAuthService();
    private final Logger LOGGER = LoggerFactory.getLogger(FirestoreService.class);

    public <T> void addDocumentToCollection(Names name, T data, String documentId) {
        getCollection(name).document(documentId).set(data);
    }

    public <T> T getObjectFromDocument(Names name, Class<T> classType, String documentId) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection(name).document(documentId);
        DocumentSnapshot document = documentReference.get().get();
        T object = null;
        if (document.exists()) {
            object = document.toObject(classType);
        }
        return object;
    }

    public <T> void updateDocumentInCollection(Names name, T data, String documentId) {
        getCollection(name).document(documentId).set(data);
    }

    public void deleteDocumentFromCollection(Names name, String documentId) {
        getCollection(name).document(documentId).delete();
    }

    public <T> void addToArrayInDocument(Names name, String documentId, T newData) {
        getCollection(name).document(documentId).set(newData, SetOptions.merge());
    }

    public List<QueryDocumentSnapshot> getEveryDocumentFromCollection(Names name) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = getCollection(name).get();
        QuerySnapshot querySnapshot = query.get();
        return querySnapshot.getDocuments();
    }

    // If a document exists, it will return true
    public boolean checkIfDocumentExistsInCollection(Names name, String documentId) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documentSnapshots = getEveryDocumentFromCollection(name);
        for (QueryDocumentSnapshot documentReference : documentSnapshots) {
            if (documentReference.getId().equals(documentId)) {
                return true;
            }
        }
        return false;
    }

    // Returns true if the messages are between user1 and user2
    public boolean checkIfMessagesHasTheseUsers(User user1, User user2) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> allDocument = getEveryDocumentFromCollection(Names.USERS);
        for (DocumentSnapshot document : allDocument) {
            if (isBetweenTheseUsers(user1, user2, document)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBetweenTheseUsers(User user1, User user2, DocumentSnapshot document) {
        return (Objects.equals(document.get("user1Id"), user1.getUserId()) && Objects.equals(document.get("user2Id"), user2.getUserId())) ||
                (Objects.equals(document.get("user1Id"), user2.getUserId()) && Objects.equals(document.get("user2Id"), user1.getUserId()));
    }

    public DocumentSnapshot getDocument(Names name, String documentId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getCollection(name).document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document;
        }
        return null;
    }

    private CollectionReference getCollection(Names name) {
        return firestore.collection(Objects.requireNonNull(CollectionNames.getCollectionName(name)));
    }

    public void deleteCollection(Names name) {
        try {
            CollectionReference collection = getCollection(name);
            ApiFuture<QuerySnapshot> future  = collection.limit(1024).get();
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

    public boolean doesUserExists(String userId) throws ExecutionException, InterruptedException {
        return checkIfDocumentExistsInCollection(Names.USERS, userId);
    }
}
