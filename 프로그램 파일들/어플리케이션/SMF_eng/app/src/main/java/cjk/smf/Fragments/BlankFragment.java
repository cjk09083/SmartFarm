package cjk.smf.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cjk.smf.R;


public class BlankFragment extends Fragment {


    ///기준 19년 04월 27일 ~ 19년 07월 10일
    public BlankFragment() {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_reference, container, false);


        return myView;
    }


}



