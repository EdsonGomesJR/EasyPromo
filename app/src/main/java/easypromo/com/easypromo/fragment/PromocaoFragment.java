package easypromo.com.easypromo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import easypromo.com.easypromo.R;
import easypromo.com.easypromo.activity.PrincipalActivity;
import easypromo.com.easypromo.adapter.AdminPromocaoAdapter;
import easypromo.com.easypromo.adapter.PromocaoAdapter;
import easypromo.com.easypromo.model.Promocao;

public class PromocaoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private DatabaseReference dbReference;

    private List<Promocao> mPromocaoList;

    public PromocaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promocao, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewAdmin);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPromocaoList = new ArrayList<>();

        dbReference = FirebaseDatabase.getInstance().getReference("promocoes");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPromocaoList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Promocao promocao  = postSnapshot.getValue(Promocao.class);
                    promocao.getUrl();

                    if (promocao.getStatus().equals("0")){
                        mPromocaoList.add(promocao);
                    }
                }

                Collections.sort(mPromocaoList);
                mAdapter = new AdminPromocaoAdapter(getActivity(),mPromocaoList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getActivity(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
