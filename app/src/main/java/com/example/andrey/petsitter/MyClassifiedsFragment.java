package com.example.andrey.petsitter;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andrey.petsitter.Adapters.ClassifiedsAdapter;
import com.example.andrey.petsitter.Database.PetsitterApp;
import com.example.andrey.petsitter.Models.Classified;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyClassifiedsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyClassifiedsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyClassifiedsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    private ArrayList<Classified> classifieds;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyClassifiedsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyClassifiedsFragment newInstance(ArrayList<Classified> myClassifieds) {
        MyClassifiedsFragment fragment = new MyClassifiedsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.classifieds = myClassifieds;
        return fragment;
    }

    public MyClassifiedsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_classifieds, container, false);

        listView = (ListView) rootView.findViewById(R.id.listMyClassifieds);

        final ArrayList<Classified> classifiedsArray = classifieds;

        ClassifiedsAdapter adapter = new ClassifiedsAdapter(getActivity(), R.layout.classified_view, classifiedsArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Classified classified = classifieds.get(position);

                Fragment fragment = ClassifiedInfoFragment.newInstance("section_number", classified);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
