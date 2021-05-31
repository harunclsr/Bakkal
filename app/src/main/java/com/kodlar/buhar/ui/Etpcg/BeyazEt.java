package com.kodlar.buhar.ui.Etpcg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class BeyazEt extends Fragment {

    private View beyazEtView;
    private RecyclerView myBeyazEtList;
    private DatabaseReference BeyazEtRef,SepetRef,SepetTutarRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        beyazEtView= inflater.inflate(R.layout.activity_beyaz_et,container,false);

        myBeyazEtList=(RecyclerView) beyazEtView.findViewById(R.id.beyazEt_list);
        myBeyazEtList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth= FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();


        // ContacsRef = FirebaseDatabase.getInstance().getReference().child("Kampus").child("icecekler").child("Su").child(currentUserID);
        BeyazEtRef = FirebaseDatabase.getInstance().getReference().child("Kampus").child("Et").child("Beyaz Et");
        SepetRef = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(currentUserID).child("Sepet").child("Urunlistesi");
        SepetTutarRef = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(currentUserID).child("Sepet").child("SepetTutari");



        return beyazEtView;
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<Urun1>()
                        .setQuery(BeyazEtRef, Urun1.class)
                        .build();

        final FirebaseRecyclerAdapter<Urun1, BeyazEtViewHolder> adapter = new FirebaseRecyclerAdapter<Urun1, BeyazEtViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final BeyazEtViewHolder BeyazEtViewHolder, int i, @NonNull Urun1 urun) {

                String userIDs = getRef(i).getKey();

                BeyazEtRef.child(userIDs).addValueEventListener(new ValueEventListener(){

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String urunfotografi = dataSnapshot.child("image").getValue(String.class);
                        String urunadi = dataSnapshot.child("urunadi").getValue(String.class);
                        String urunagirlik = dataSnapshot.child("urunagirlik").getValue(String.class);
                        Integer urunfiyat = dataSnapshot.child("urunfiyati").getValue(Integer.class);
                        String urunid = dataSnapshot.child("urunid").getValue(String.class);

                        BeyazEtViewHolder.urunadi.setText(urunadi);
                        BeyazEtViewHolder.urunfiyat.setText(""+urunfiyat);
                        BeyazEtViewHolder.urunagirlik.setText(urunagirlik);

                        Picasso.get().load(urunfotografi.toString()).into(BeyazEtViewHolder.urunfotografi);

                        BeyazEtViewHolder.sepetArti.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {


                                DatabaseReference SepetRef;
                                SepetRef = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(currentUserID).child("Sepet").child("Urunlistesi");
                                BeyazEtViewHolder.miktar++;

                                SepetRef.child(userIDs).setValue(new Urun1(urunadi, urunagirlik, urunfiyat, urunfotografi, urunid));
                                SepetRef.child(userIDs).child("miktar").setValue(BeyazEtViewHolder.miktar);
                                SepetRef.child(userIDs).child("uruntutari").setValue(urun.getUruntutari());
                                Snackbar.make(beyazEtView, "Sepetinize ürün eklendi.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();


                                urun.setUruntutari(BeyazEtViewHolder.miktar * urunfiyat);

                                SepetRef.child(userIDs).child("uruntutari").setValue(urun.getUruntutari());


                                Snackbar.make(beyazEtView, "Sepetinize ürün eklendi.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }


                        });




                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(beyazEtView, "HATA!!!!!!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }

            @NonNull
            @Override
            public BeyazEtViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.urun1,viewGroup,false);
                BeyazEtViewHolder viewHolder = new BeyazEtViewHolder(view);
                return viewHolder;
            }
        };
        myBeyazEtList.setAdapter(adapter);
        adapter.startListening();


    }

    public static class BeyazEtViewHolder extends RecyclerView.ViewHolder{

        TextView urunadi,urunfiyat,urunagirlik;
        ImageFilterView urunfotografi;
        private ImageButton sepetArti;


        private int miktar ;
        private int tutar;


        public BeyazEtViewHolder(@NonNull View itemView) {
            super(itemView);
            urunadi= itemView.findViewById(R.id.urunAdi);
            urunfiyat= itemView.findViewById(R.id.urunFiyat);
            urunagirlik= itemView.findViewById(R.id.urunAgirlik);
            urunfotografi=itemView.findViewById(R.id.urunfotografi);
            sepetArti = itemView.findViewById(R.id.sepetarti);



        }
    }



}