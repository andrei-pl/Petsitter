package com.example.andrey.petsitter;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andrey.petsitter.Models.Classified;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassifiedInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassifiedInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassifiedInfoFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    private String mParam1;
    private Classified classified;
    private static ClassifiedInfoFragment fragment = null;

    private ImageView image;
    private TextView txtRegion;
    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtPhone;
    private TextView txtName;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ClassifiedInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassifiedInfoFragment newInstance(String sectionNumber, Classified classified) {
        ClassifiedInfoFragment fragment = new ClassifiedInfoFragment();
        Bundle args = new Bundle();
        args.putInt(sectionNumber, 2);
        fragment.setArguments(args);
        fragment.classified = classified;
        return fragment;
    }

    public ClassifiedInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classified_info, container, false);

        this.image = (ImageView) view.findViewById(R.id.picInfo);
        this.image.setImageBitmap(classified.getImage());
        this.txtRegion = (TextView) view.findViewById(R.id.tvInfoRegion);
        this.txtRegion.setText(classified.getAddress());
        this.txtTitle = (TextView) view.findViewById(R.id.tvInfoTitle);
        this.txtTitle.setText(classified.getTitle());
        this.txtDescription = (TextView) view.findViewById(R.id.tvInfoDescription);
        this.txtDescription.setText(classified.getDescription());
        this.txtPhone = (TextView) view.findViewById(R.id.tvInfoTel);
        this.txtPhone.setText(classified.getPhone());
        this.txtName = (TextView) view.findViewById(R.id.tvInfoName);
        this.txtName.setText(classified.getAuthorName());

        return view;
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
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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
