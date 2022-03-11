package net.fpl.demofbrestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnLuu, btnList;
    EditText edUser, edPass;
    ListView lv;
    AdapterUser adapter;
    ArrayList<User> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLuu = findViewById(R.id.btn_luu);
        btnList = findViewById(R.id.btnlist);
        edUser = findViewById(R.id.ed_user);
        edPass = findViewById(R.id.ed_password);
        lv = findViewById(R.id.lv);
        list = new ArrayList<>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != null) {
                    list.clear();
                }
                db.collection(User.TB_NAME)

                        .get()

                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("zzzzz", document.getId() + " => " + document.getData());
                                        User user1 = new User();
                                        user1.setName(document.get(User.NAME_KEY).toString());
                                        user1.setPass(document.get(User.PASS_KEY).toString());
                                        list.add(user1);
                                        Log.d("zzzzz", "User: " + user1);
                                        Log.d("zzzzz", "list: " + list.size());

                                    }
                                } else {
                                    Log.w("zzzzz", "Error getting documents.", task.getException());
                                }
                            }
                        });
                adapter = new AdapterUser(list);
                lv.setAdapter(adapter);
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = edUser.getText().toString();
                String strPass = edPass.getText().toString();

                Map<String, Object> user = new HashMap<>();
                user.put(User.NAME_KEY, strName);
                user.put(User.PASS_KEY, strPass);

                db.collection(User.TB_NAME)
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("zzzz", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("", "Error adding document", e);
                            }
                        });

            }
        });

    }
}