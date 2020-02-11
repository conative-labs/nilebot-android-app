package com.soleeklab.nilebot.utils;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.soleeklab.nilebot.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProgressDialogFragment extends DialogFragment {


    Unbinder unbinder;
    //    @BindView(R.id.giphy_space)
//    GifView giphySpace;
    @BindView(R.id.img_v)
    ImageView imgV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_full_screen_dialog, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        giphySpace.setVisibility(View.VISIBLE);
//        giphySpace.play();
//        giphySpace.setGifResource(R.drawable.logoloading);
//        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent)new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));

        Glide.with(getActivity()).asGif().load(R.drawable.logoloading).into(imgV);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);


    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
