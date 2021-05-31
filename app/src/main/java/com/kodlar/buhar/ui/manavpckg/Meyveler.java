package com.kodlar.buhar.ui.manavpckg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kodlar.buhar.R;
import com.kodlar.buhar.Urun1;

import com.squareup.picasso.Picasso;

public class Meyveler extends Fragment {
    private View MeyvelerView;
    private RecyclerView myMeyvelerList;
    private DatabaseReference MeyvelerRef,ContacsRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MeyvelerView= inflater.inflate(R.layout.meyveler_layout,container,false);

        myMeyvelerList=(RecyclerView) MeyvelerView.findViewById(R.id.meyveler_list);
        myMeyvelerList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth= FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        // ContacsRef = FirebaseDatabase.getInstance().getReference().child("Kampus").child("icecekler").child("Su").child(currentUserID);
        MeyvelerRef = FirebaseDatabase.getInstance().getReference().child("Kampus").child("Manav").child("Meyveler");



        return MeyvelerView;
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<Urun1>()
                        .setQuery(MeyvelerRef, Urun1.class)
                        .build();

        final FirebaseRecyclerAdapter<Urun1, MeyvelerViewHolder> adapter = new FirebaseRecyclerAdapter<Urun1, MeyvelerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MeyvelerViewHolder MeyvelerViewHolder, int i, @NonNull Urun1 urun) {

                String userIDs = getRef(i).getKey();

                MeyvelerRef.child(userIDs).addValueEventListener(new ValueEventListener(){

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                        String urunfotografi = dataSnapshot.child("image").getValue(String.class);
                        String urunadi = dataSnapshot.child("urunadi").getValue(String.class);
                        String urunagirlik = dataSnapshot.child("urunagirlik").getValue(String.class);
                        String urunfiyat = dataSnapshot.child("urunfiyati").getValue(String.class);

                        MeyvelerViewHolder.urunadi.setText(urunadi);
                        MeyvelerViewHolder.urunfiyat.setText(urunfiyat);
                        MeyvelerViewHolder.urunagirlik.setText(urunagirlik);
                        Picasso.get().load(urunfotografi.toString()).into(MeyvelerViewHolder.urunfotografi);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(MeyvelerView, "HATA!!!!!!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }

            @NonNull
            @Override
            public MeyvelerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.urun1,viewGroup,false);
                MeyvelerViewHolder viewHolder = new MeyvelerViewHolder(view);
                return viewHolder;
            }
        };
        myMeyvelerList.setAdapter(adapter);
        adapter.startListening();


    }

    public static class MeyvelerViewHolder extends RecyclerView.ViewHolder{

        TextView urunadi,urunfiyat,urunagirlik;
        ImageFilterView urunfotografi;

        public MeyvelerViewHolder(@NonNull View itemView) {
            super(itemView);
            urunadi= itemView.findViewById(R.id.urunAdi);
            urunfiyat= itemView.findViewById(R.id.urunFiyat);
            urunagirlik= itemView.findViewById(R.id.urunAgirlik);
            urunfotografi=itemView.findViewById(R.id.urunfotografi);

        }
    }

}


