package com.devdroiddev.notesapp;

import static java.util.Locale.filter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devdroiddev.notesapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private NotesAdapter notesAdapter;
    private List<NotesModel> notesModelList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesModelList = new ArrayList<>();
        notesAdapter = new NotesAdapter(this);
        binding.notesRecycler.setAdapter(notesAdapter);
        binding.notesRecycler.setLayoutManager(new LinearLayoutManager(this));

        binding.floatingAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddActivity.class));
            }
        });

        binding.searhBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.length()>0) {
                    filter(text);
                }
                else {
                    notesAdapter.clear();
                    notesAdapter.filterList(notesModelList);
                }

            }
        });
    }

    private void filter(String text) {
        List<NotesModel> adaptetList = notesAdapter.getList();
        List<NotesModel> notesModelList = new ArrayList<>();
        for (int i = 0; i<adaptetList.size(); i++) {
            NotesModel notesModel = adaptetList.get(i);
            if (notesModel.getTitle().toLowerCase().contains(text.toLowerCase()) || notesModel.getDescription().toLowerCase().contains(text.toLowerCase())){
                notesModelList.add(notesModel);
            }
        }
        notesAdapter.filterList(notesModelList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Checking User");
        dialog.setIcon(R.drawable.checking_user);
        dialog.setMessage("processing");
        //TODO: Here we check the user already signIn or not
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // if current user is not signIn
        if (auth.getCurrentUser() == null) {
            //TODO: SignIn the user as a guest
            auth.signInAnonymously()
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            dialog.cancel();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO: Make a method to fetch the data from the database
        getData();
    }

    private void getData() {
        FirebaseFirestore.getInstance()
                .collection("Notes")
                .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        notesAdapter.clear();
                       List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                       for (int i = 0; i<documentSnapshotList.size(); i++)
                       {
                           DocumentSnapshot snapshot = documentSnapshotList.get(i);
                           NotesModel notesModel = snapshot.toObject(NotesModel.class);
                           notesModelList.add(notesModel);
                           notesAdapter.add(notesModel);
                       }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
