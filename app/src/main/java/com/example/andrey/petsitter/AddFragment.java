package com.example.andrey.petsitter;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andrey.petsitter.Database.PetsitterApp;
import com.example.andrey.petsitter.Location.MyLocationListener;
import com.example.andrey.petsitter.Models.AnimalType;
import com.example.andrey.petsitter.Models.Classified;
import com.example.andrey.petsitter.Models.ClassifiedParse;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements View.OnClickListener {

    private final int CAPTURE_CAMERA = 1888;
    private final int LOAD_PICTURE = 1111;
    private static int idNumber = 2;

    private OnFragmentInteractionListener mListener;

    private EditText edtTitle;
    private EditText edtDescription;
    private EditText edtAddress;
    private EditText edtPhone;
    private EditText edtName;
    private Spinner sprAnimals;
    private Button btnTakePicture;
    private Button btnLoadPicture;
    private Button btnAdd;
    private ImageView imgPicture;
    private ProgressBar progressBar;

    private MyLocationListener listener;
    private Bitmap takenPhoto;
    private File takenPictureFile;
    private String nameText;
    private String titleText;
    private String descriptionText;
    private String phoneText;
    private String addressText;
    private String finalPhotoName;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(MyLocationListener listener) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.listener = listener;
        return fragment;
    }

    public AddFragment() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        initizalizeComponents(rootView);

        setClassifiedsAdapter();

        changeAddress();

        return rootView;
    }

    private void setClassifiedsAdapter() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.animal_options, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sprAnimals.setAdapter(adapter);
    }

    private void initizalizeComponents(View rootView) {
        this.edtTitle = (EditText) rootView.findViewById(R.id.edtTitle);
        this.edtDescription = (EditText) rootView.findViewById(R.id.edtDescription);
        this.edtAddress = (EditText) rootView.findViewById(R.id.edtAddress);
        this.edtPhone = (EditText) rootView.findViewById(R.id.edtPhone);
        this.edtName = (EditText) rootView.findViewById(R.id.edtName);
        this.imgPicture = (ImageView) rootView.findViewById(R.id.imgPicture);
        this.btnTakePicture = (Button) rootView.findViewById(R.id.btnTakePicture);
        this.btnTakePicture.setOnClickListener(this);
        this.btnLoadPicture = (Button) rootView.findViewById(R.id.btnLoadPicture);
        this.btnLoadPicture.setOnClickListener(this);
        this.btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        this.btnAdd.setOnClickListener(this);
        this.progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.GONE);
        this.sprAnimals = (Spinner) rootView.findViewById(R.id.sprAnimal);
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

    @Override
    public void onClick(View v) {

        int viewId = v.getId();
        if(viewId == R.id.btnAdd){

            addClassified(v);

        } else if(viewId == R.id.btnTakePicture){
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String photoName = "IMG_";
            String suffix = ".jpg";
            finalPhotoName = photoName + timeStamp + suffix;

            File photosFolder = null;
            if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                photosFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM" + "/Petsitter");

                if(!photosFolder.exists()){
                    photosFolder.mkdirs();
                }

                takenPictureFile = new File(photosFolder, finalPhotoName);

                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takenPictureFile));
                startActivityForResult(takePicture, CAPTURE_CAMERA);

            }
        } else if(viewId == R.id.btnLoadPicture){
            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, LOAD_PICTURE);
        }
    }

    public void changeAddress(){
        if(!listener.getCurrentAddress().equals("")){
            edtAddress.setText(listener.getCurrentAddress());
        }
    }

    private void addClassified(View view) {
        this.progressBar.setVisibility(View.VISIBLE);

        if(isValidInfo()){
            String spinnerText = sprAnimals.getSelectedItem().toString();
            AnimalType animalType = AnimalType.Other;

            if(spinnerText.equals("Dog")){
                animalType = AnimalType.Dog;
            } else if(spinnerText.equals("Cat")){
                animalType = AnimalType.Cat;
            } else if(spinnerText.equals("Bird")){
                animalType = AnimalType.Bird;
            } else if(spinnerText.equals("Rodent")){
                 animalType = AnimalType.Rodent;
            } else if(spinnerText.equals("Fish")){
                 animalType = AnimalType.Fish;
            }

            final ClassifiedParse classifiedParse = new ClassifiedParse();
            classifiedParse.setTitle(titleText);
            classifiedParse.setDescription(descriptionText);
            classifiedParse.setAuthorName(nameText);
            classifiedParse.setPhone(phoneText);
            classifiedParse.setAddress(addressText);
            classifiedParse.setImage(takenPhoto, finalPhotoName);
            classifiedParse.setAnimalType(animalType);

            Location location = listener.getMyLocation();
            if(location != null){
                classifiedParse.setLocation(location);
            }

            final Classified classified = new Classified(titleText, descriptionText, nameText, phoneText, addressText, takenPhoto, animalType, location);

            classifiedParse.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        classified.setId(classifiedParse.getObjectId());

                        saveInDatabase(classified);

                        addNotification();
                        progressBar.setVisibility(View.GONE);
                        goToMainPage(classified);
                    } else {
                        // The save failed.
                        Log.d("Failed", "User update error: " + e);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            this.progressBar.setVisibility(View.GONE);
        }
    }

    private void goToMainPage(Classified classified) {
        ArrayList<Classified> classifieds = MainActivity.classifieds;
        classifieds.add(classified);

        Fragment fragment = ClassifiedsFragment.newInstance("section_number", 1, MainActivity.classifieds, listener);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void saveInDatabase(Classified classified) {
        boolean isSavedIntoDB = ((PetsitterApp) getActivity().getApplication()).getUserDB().addClassified(classified);

        if(isSavedIntoDB){
            Toast.makeText(getActivity(), "Classified saved into DB", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNotification() {
        Activity activity = getActivity();

        // Create an intent that will be fired when the user clicks the notification.
        Intent intent = new Intent(activity, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity (activity, 0, intent, 0);

        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);

        // Icon in the notification bar. Also appears in the lower right hand corner of the notification itself.
        builder.setSmallIcon(R.drawable.logo4);
        // The content title, which appears in large type at the top of the notification
        builder.setContentTitle(titleText);
        // The content text, which appears in smaller text below the title
        builder.setContentText(descriptionText);
        // Icon which appears on the left of the notification.
        builder.setLargeIcon(takenPhoto);
        // The subtext, which appears under the text on newer devices.
        // Devices running versions of Android prior to 4.2 will ignore this field, so don't use it for anything vital!
        builder.setSubText(phoneText + " " + nameText);
        // Notification will disappear after the user taps it, rather than remaining until it's explicitly dismissed.
        builder.setAutoCancel(true);
        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Immediately display the notification icon in the notification bar.
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // not working on Genymotion
        // only on real device (Sony Xperia S - my phone :) )
        if(requestCode == CAPTURE_CAMERA && resultCode == Activity.RESULT_OK){
            takenPhoto  = (Bitmap) data.getExtras().get("data");
            imgPicture.setImageBitmap(takenPhoto);
        } else if(requestCode == LOAD_PICTURE && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            String pictureName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
            File file = new File(picturePath, pictureName);
            Bitmap thumbnail = BitmapFactory.decodeFile(String.valueOf(data.getData().getPath()));
            Log.w("path of image from gallery......******************.........", picturePath + "");
            imgPicture.setImageBitmap(thumbnail);
        }
    }

    private boolean isValidInfo(){
        nameText = edtName.getText().toString();
        titleText = edtTitle.getText().toString();
        descriptionText = edtDescription.getText().toString();
        phoneText = edtPhone.getText().toString();
        addressText = edtAddress.getText().toString();

        if(titleText.trim().equals("") || descriptionText.trim().equals("") || nameText.trim().equals("") || phoneText.trim().equals("") || addressText.trim().equals("") ){
            Toast.makeText(getActivity(), "Every field must not be empty or whitespace", Toast.LENGTH_SHORT).show();
            return false;
        } else if (titleText.length() < 5 || titleText.length() > 20){
            Toast.makeText(getActivity(), "Title must contain 5-20 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (descriptionText.trim().length() < 20 || descriptionText.trim().length() > 200){
            Toast.makeText(getActivity(), "Description must contain 20-200 characters.", Toast.LENGTH_SHORT).show();
            return false;
        } else if(phoneText.trim().length() < 4 || phoneText.trim().length() > 20){
            Toast.makeText(getActivity(), "Phone must be a number and must contains 4-20 digits", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nameText.trim().length() < 3 || nameText.trim().length() > 20){
            Toast.makeText(getActivity(), "Name must contain 3-20 characters.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (addressText.trim().length() < 3 || addressText.trim().length() > 20){
            Toast.makeText(getActivity(), "Address must contain 3-20 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imgPicture.getDrawable() == null){
            Toast.makeText(getActivity(), "Give me some picture!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("image", photo);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        try{
//            Bitmap bm = (Bitmap) savedInstanceState.getParcelable("image");
//            image.setImageBitmap(bm);
//            photo = bm;
//        } catch (Exception ex) {
//        }
//    }

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
