package net.fpl.demofbrestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    Button btnLuu, btnList, btnDelete, btnUpdate;
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
        btnDelete = findViewById(R.id.btn_xoa);
        btnUpdate = findViewById(R.id.btn_update);

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

                db.collection(User.TB_NAME).document((String) user.get(User.NAME_KEY))
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                                Log.d("zzzz", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Lưu thất bại", Toast.LENGTH_SHORT).show();

                                Log.w("zzzz", "Error writing document", e);
                            }
                        });


            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = edUser.getText().toString();

                db.collection(User.TB_NAME).document(strName).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Đã xóa User: " + strName, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = edUser.getText().toString();
                String strPass = edPass.getText().toString();
                Map<String, Object> user = new HashMap<>();
                user.put(User.PASS_KEY, strPass);

                db.collection(User.TB_NAME).document(strName)
                        .update(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Cập nhập thành công", Toast.LENGTH_SHORT).show();
                                Log.d("zzzz", "onSuccess: Cập nhập thành công");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Cập nhập lỗi", Toast.LENGTH_SHORT).show();
                                Log.d("zzzz", "onSuccess: Cập nhập lỗi");
                            }
                        });
            }
        });
    }
}