package com.example.fraccionamiento.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fraccionamiento.Activities.Admin.AddNewUserActivity;
import com.example.fraccionamiento.Adapters.AdapterUsers;
import com.example.fraccionamiento.Classes.UserClass;
import com.example.fraccionamiento.EditUserActivity;
import com.example.fraccionamiento.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private ArrayList<UserClass> userList;
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users");
    private AppCompatSpinner spnOrderBy;
    private String order;
    private TextView lblNoFoundUsers;
    private EditText txtSearchUser;
    private RecyclerView rcVwUsers;
    private AdapterUsers adapterUsers;
    private FloatingActionButton fABAddUser;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);

        lblNoFoundUsers = root.findViewById(R.id.lblNotUsersFound);
        spnOrderBy = root.findViewById(R.id.spnOrderBy);
        txtSearchUser = root.findViewById(R.id.txtSearchUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcVwUsers = root.findViewById(R.id.rcVwUsers);
        rcVwUsers.setLayoutManager(linearLayoutManager);
        userList = new ArrayList<>();
        adapterUsers = new AdapterUsers(userList);
        fABAddUser = root.findViewById(R.id.fabAddNewUser);

        rcVwUsers.setAdapter(adapterUsers);

        findUserFromFireBase(txtSearchUser.getText().toString(), 0);


        txtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int spnPosition = spnOrderBy.getSelectedItemPosition();
                findUserFromFireBase(s.toString(), spnPosition);

            }
        });

        spnOrderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                findUserFromFireBase(txtSearchUser.getText().toString(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fABAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser();
            }
        });

        return root;
    }

    private void newUser() {
        Intent intent = new Intent(getActivity(), AddNewUserActivity.class);
        startActivity(intent);
    }

    private void findUserFromFireBase(String text, int position) {
        if (position==0){
            order = getString(R.string.name);
        }else {
            order = getString(R.string.last_name);
        }
        dbRef.orderByChild(order).limitToFirst(100).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserClass user = snapshot.getValue(UserClass.class);
                    user.setKey(snapshot.getKey());
                    userList.add(user);
                }
                adapterUsers.notifyDataSetChanged();

                if(adapterUsers.getItemCount()==0){
                    lblNoFoundUsers.setVisibility(View.VISIBLE);
                }
                else {
                    lblNoFoundUsers.setVisibility(View.INVISIBLE);
                }

                adapterUsers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int rcvPosition = rcVwUsers.getChildLayoutPosition(v);
                        String name = userList.get(rcvPosition).getName();
                        String lastName = userList.get(rcvPosition).getLastName();
                        String email = userList.get(rcvPosition).getEmail();
                        Bundle data = new Bundle();
                        data.putString("name", name);
                        data.putString("lastName", lastName);
                        data.putString("email", email);
                        Intent intent = new Intent(getActivity(), EditUserActivity.class);
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}