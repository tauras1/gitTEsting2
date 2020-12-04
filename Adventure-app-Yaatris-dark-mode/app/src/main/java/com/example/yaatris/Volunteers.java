package com.example.yaatris;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Volunteers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Volunteers extends Fragment {


    public Volunteers() {
        // Required empty public constructor
    }


    public static Volunteers newInstance() {
        Volunteers fragment = new Volunteers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_volunteers, container, false);
    }
}