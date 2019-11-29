package edu.temple.spiapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountFragment extends Fragment {
    private final static String TAG = "AccFrag";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    Button signoutButton;
    ImageView pictureView;
    public String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Look for existing user
        final GoogleSignInAccount googleCurrentAcc = GoogleSignIn.getLastSignedInAccount(getContext());
        final FirebaseUser firebaseCurrentAcc = FirebaseAuth.getInstance().getCurrentUser();

        ImageView userAva = view.findViewById(R.id.userAva);
        ImageView serviceIcon = view.findViewById(R.id.serviceIcon);
        TextView userName = view.findViewById(R.id.userName);
        TextView userEmail = view.findViewById(R.id.userEmail);

        //User take picture for face training
        pictureView = view.findViewById(R.id.pictureView);
        final Button takePicture = view.findViewById(R.id.takePicture);
        Button uploadPicture = view.findViewById(R.id.uploadPicture);


        //Open a camera activity to take picture of the face
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        //Upload image to firebase
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference trainingRef = null;
        if(googleCurrentAcc!=null){
            StorageReference userRef = firebaseStorage.getReference().child(googleCurrentAcc.getId());
            trainingRef = userRef.child("training/"+ (int)(Math.random() * 1000000) +".jpg");
        }
        else if(firebaseCurrentAcc !=null){
            StorageReference userRef = firebaseStorage.getReference().child(firebaseCurrentAcc.getUid());
            trainingRef = userRef.child("training/"+ (int)(Math.random() * 1000000) +".jpg");
        }

        final StorageReference finalTrainingRef = trainingRef;
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data from an ImageView as bytes
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                bitmap = scaleDown(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = finalTrainingRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Failed Uploading", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), taskSnapshot.getMetadata().getPath(), Toast.LENGTH_SHORT).show();
                        if(googleCurrentAcc!=null){
                            final DocumentReference docIdRef = db.collection("users").document(googleCurrentAcc.getId());
                            db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(docIdRef);

                                    // Note: this could be done without a transaction
                                    //       by updating the population using FieldValue.increment()
                                    ArrayList<String> familiarFaces = (ArrayList<String>) snapshot.get("familiarFaces");
                                    familiarFaces.add(taskSnapshot.getMetadata().getName());
                                    transaction.update(docIdRef, "familiarFaces", familiarFaces);
                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Transaction success!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Transaction failure.", e);
                                }
                            });
                        }
                        else if(firebaseCurrentAcc!=null){
                            final DocumentReference docIdRef2 = db.collection("users").document(firebaseCurrentAcc.getUid());
                            db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(docIdRef2);

                                    // Note: this could be done without a transaction
                                    //       by updating the population using FieldValue.increment()
                                    ArrayList<String> familiarFaces = (ArrayList<String>) snapshot.get("familiarFaces");
                                    familiarFaces.add(taskSnapshot.getMetadata().getName());
                                    transaction.update(docIdRef2, "familiarFaces", familiarFaces);
                                    // Success
                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Transaction success!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Transaction failure.", e);
                                }
                            });
                        }
                    }
                });
            }
        });



        if (googleCurrentAcc != null) {
            Picasso.get().load(googleCurrentAcc.getPhotoUrl()).into(userAva);
            serviceIcon.setBackground(getResources().getDrawable(R.drawable.fui_ic_googleg_color_24dp, null));
            userName.setText("Name: " + googleCurrentAcc.getDisplayName());
            userEmail.setText("Email: " + googleCurrentAcc.getEmail());

            DocumentReference docIdRef = db.collection("users").document(googleCurrentAcc.getId());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Document", "Document exists!");
                        } else {
                            Log.d("Document", "Document does not exist!");
                            //Create data
                            Map<String, Object> docData = new HashMap<>();
                            docData.put("cameraIds",new ArrayList<String>());
                            docData.put("email",googleCurrentAcc.getEmail());
                            docData.put("familiarFaces",new ArrayList<String>());
                            docData.put("firstName",googleCurrentAcc.getGivenName());
                            docData.put("lastName",googleCurrentAcc.getFamilyName());

                            //Add document to firestore
                            db.collection("users").document(googleCurrentAcc.getId())
                                    .set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("AddDoc", "DocumentSnapshot successfully written!");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("AddDoc", "Error writing document", e);
                                        }
                                    });
                        }
                    } else {
                        Log.d("Document", "Failed with: ", task.getException());
                    }
                }
            });


        } else if (firebaseCurrentAcc != null) {
            Picasso.get().load(firebaseCurrentAcc.getPhotoUrl()).into(userAva);

            if (firebaseCurrentAcc.getProviderId() == "firebase") {
                serviceIcon.setBackground(getResources().getDrawable(R.drawable.fui_ic_mail_white_24dp, null));
                serviceIcon.setBackgroundTintList(getResources().getColorStateList(R.color.fui_bgPhone, null));
                userName.setText("Name: " + firebaseCurrentAcc.getEmail()
                        .substring(0, firebaseCurrentAcc.getEmail().indexOf('@')));
            } else {
                serviceIcon.setBackground(getResources().getDrawable(R.drawable.fui_ic_github_white_24dp, null));
                serviceIcon.setBackgroundTintList(getResources().getColorStateList(R.color.fui_bgGitHub, null));
                userName.setText("Name: " + firebaseCurrentAcc.getDisplayName());
            }
            userEmail.setText("Email: " + firebaseCurrentAcc.getEmail());
        }
        signoutButton = view.findViewById(R.id.signoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInAccount googleCurrentAcc = GoogleSignIn.getLastSignedInAccount(getContext());
                FirebaseUser githubCurrentAcc = FirebaseAuth.getInstance().getCurrentUser();
                if (googleCurrentAcc != null) {
                    GoogleSignIn.getClient(getContext(), gso).signOut();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LogIn.class));
                } else if (githubCurrentAcc != null) {
                    FirebaseAuth.getInstance(FirebaseAuth.getInstance().getApp()).signOut();
                    GoogleSignIn.getClient(getContext(), gso).signOut();
                    startActivity(new Intent(getActivity(), LogIn.class));
                }
                getActivity().finish();
            }
        });
        return view;
    }

    //Camera methods
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            galleryAddPic();
            setPic();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                String.valueOf((int)(Math.random()*1000000)),  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = pictureView.getWidth();
        int targetH = pictureView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        pictureView.setImageBitmap(bitmap);
    }

    public static Bitmap scaleDown(Bitmap realImage) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, 640,480,false);
        return newBitmap;
    }
}
