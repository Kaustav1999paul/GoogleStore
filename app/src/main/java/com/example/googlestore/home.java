package com.example.googlestore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Model.Accessories;
import Model.Fabrics;
import Model.Home;
import Model.Products;
import Prevalent.Prevalent;
import ViewHolder.AccessoriesViewHolder;
import ViewHolder.ProductViewHolder;
import maes.tech.intentanim.CustomIntent;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    public home() {
        // Required empty public constructor
    }

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    private ImageView ph, stadia ,daydream, pixelBook, google, assistant, personImage;
    private RecyclerView accessories_list, fabric_list, home_list;
    private DatabaseReference AccessoriesRef, FabricRef, HomeRef;
    private TextView personName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        +++++++++++++++++++++++++++++ Assign ID to variables +++++++++++++++++++++++++++++++++++++
        ph = view.findViewById(R.id.ph);
        pixelBook =view.findViewById(R.id.pixelBookGo);
        google = view.findViewById(R.id.google);
        assistant = view.findViewById(R.id.assistant);
        accessories_list = view.findViewById(R.id.accessories_list);
        fabric_list = view.findViewById(R.id.fabric_list);
        home_list= view.findViewById(R.id.home_list);
        personName = view.findViewById(R.id.personName);
        personImage = view.findViewById(R.id.personImage);
//        +++++++++++++++++++++++++++++ Assign ID to variables +++++++++++++++++++++++++++++++++++++

        String im = Prevalent.CurrentOnlineUser.getPhoto();
        Glide.with(personImage).load(im).into(personImage);
        personName.setText(Prevalent.CurrentOnlineUser.getName());
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.googlequicksearchbox");
                startActivity(i);
            }
        });
        assistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getActivity().getPackageManager().getLaunchIntentForPackage("com.android.vending");
                startActivity(i);
            }
        });
        ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Products_List.class);
                startActivity(intent);
                CustomIntent.customType(getContext(), "left-to-right");
            }
        });

        pixelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PixelBookGo.class);
                startActivity(intent);
                CustomIntent.customType(getContext(), "left-to-right");
            }
        });


        //        =========================== Banner Slider ===================================
        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.p4));
        sliderItems.add(new SliderItem(R.drawable.ghome));
        sliderItems.add(new SliderItem(R.drawable.stadia));
        sliderItems.add(new SliderItem(R.drawable.pixel3xl));
        sliderItems.add(new SliderItem(R.drawable.reg));
        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(1.00f + r * 0.10f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(slideRunnable);
                sliderHandler.postDelayed(slideRunnable, 2000);
            }
        });
        //        =========================== Banner Slider ===================================


        accessories_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
//        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
        accessories_list.setLayoutManager(linearLayoutManager);

        fabric_list.setHasFixedSize(true);
        LinearLayoutManager linearManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        fabric_list.setLayoutManager(linearManager);

        home_list.setHasFixedSize(true);
        LinearLayoutManager linear = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        home_list.setLayoutManager(linear);

        AccessoriesRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Accessories");
        FabricRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Fabrics");
        HomeRef = FirebaseDatabase.getInstance().getReference().child("Products").child("Home");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
// ++++++++++++++++++++++++++++++++++++++++ For Accessories +++++++++++++++++++++++++++++++++++++++++++++++++++++++
        FirebaseRecyclerOptions<Accessories> options = new FirebaseRecyclerOptions.Builder<Model.Accessories>()
                .setQuery(AccessoriesRef, Model.Accessories.class)
                .build();
        FirebaseRecyclerAdapter<Accessories, AccessoriesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Model.Accessories, AccessoriesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final AccessoriesViewHolder holder, int position, @NonNull final Model.Accessories model) {
                        holder.txtProductName.setText(model.getProduct_Name());
                        holder.txtProductPrice.setText(model.getPrice());
                        Glide.with(holder.imageView).load(model.getProduct_Image()).into(holder.imageView);

                        final TextView txV = holder.txtProductName;
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                                CustomIntent.customType(getContext(), "bottom-to-up");
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public AccessoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accessories_layout, parent,false);
                        AccessoriesViewHolder holder = new AccessoriesViewHolder(view);
                        return holder;
                    }
                };
        accessories_list.setAdapter(adapter);
        adapter.startListening();
// ++++++++++++++++++++++++++++++++++++++++ For Accessories +++++++++++++++++++++++++++++++++++++++++++++++++++++++

//        ++++++++++++++++++++++++++++++++++++++++++ For Fabric List ++++++++++++++++++++++++++++++++

        FirebaseRecyclerOptions<Fabrics> option = new FirebaseRecyclerOptions.Builder<Model.Fabrics>()
                .setQuery(FabricRef, Model.Fabrics.class)
                .build();

        FirebaseRecyclerAdapter<Fabrics, AccessoriesViewHolder> adapters =
                new FirebaseRecyclerAdapter<Model.Fabrics, AccessoriesViewHolder>(option) {
                    @Override
                    protected void onBindViewHolder(@NonNull AccessoriesViewHolder holder, int position, @NonNull final Model.Fabrics model) {
                        holder.txtProductName.setText(model.getProduct_Name());
                        holder.txtProductPrice.setText(model.getPrice());
                        Picasso.get().load(model.getProduct_Image()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intenta = new Intent(getContext(), ProductDetailsActivity.class);
                                intenta.putExtra("pid", model.getPid());
                                startActivity(intenta);
                                CustomIntent.customType(getContext(), "bottom-to-up");
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public AccessoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accessories_layout, parent,false);
                        AccessoriesViewHolder holder = new AccessoriesViewHolder(view);
                        return holder;
                    }
                };
        fabric_list.setAdapter(adapters);
        adapters.startListening();
//        ++++++++++++++++++++++++++++++++++++++++++ For Fabric List ++++++++++++++++++++++++++++++++

        //        ++++++++++++++++++++++++++++++++++++++++++ For Home List ++++++++++++++++++++++++++++++++
        FirebaseRecyclerOptions<Home> optiona = new FirebaseRecyclerOptions.Builder<Model.Home>()
                .setQuery(HomeRef, Model.Home.class)
                .build();

        FirebaseRecyclerAdapter<Home, AccessoriesViewHolder> homeadapter =
                new FirebaseRecyclerAdapter<Model.Home, AccessoriesViewHolder>(optiona) {
                    @Override
                    protected void onBindViewHolder(@NonNull AccessoriesViewHolder holder, int position, @NonNull final Model.Home model) {
                        holder.txtProductName.setText(model.getProduct_Name());
                        holder.txtProductPrice.setText(model.getPrice());
                        Picasso.get().load(model.getProduct_Image()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intenta = new Intent(getContext(), ProductDetailsActivity.class);
                                intenta.putExtra("pid", model.getPid());
                                startActivity(intenta);
                                CustomIntent.customType(getContext(), "bottom-to-up");
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public AccessoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accessories_layout, parent,false);
                        AccessoriesViewHolder holder = new AccessoriesViewHolder(view);
                        return holder;
                    }
                };
        home_list.setAdapter(homeadapter);
        homeadapter.startListening();
//        ++++++++++++++++++++++++++++++++++++++++++ For Home List ++++++++++++++++++++++++++++++++

    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(slideRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(slideRunnable, 2000);
    }




}
