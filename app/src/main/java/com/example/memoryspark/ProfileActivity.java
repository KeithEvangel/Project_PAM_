package com.example.memoryspark;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView userNameTextView;
    private Button backButton;
    private RequestQueue requestQueue;
    private Gson gson;

    // URL for the JSON API endpoint
    private static final String PROFILE_API_URL = "https://6729d4c76d5fa4901b6e8a87.mockapi.io/api/profile/picture/profilepicture/1"; // Replace with your actual URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // Initialize UI elements
        profileImageView = findViewById(R.id.profileImageView);
        userNameTextView = findViewById(R.id.userNameTextView);
        backButton = findViewById(R.id.backButton);

        // Initialize Volley Request Queue and Gson
        requestQueue = Volley.newRequestQueue(this);
        gson = new Gson();

        // Fetch profile data from the API
        fetchProfileData();

        // Set up back button
        backButton.setOnClickListener(view -> finish());
    }

    // Method to fetch profile data from API
    private void fetchProfileData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PROFILE_API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse JSON response using Gson
                        UserProfile userProfile = gson.fromJson(response, UserProfile.class);

                        // Set the user name
                        userNameTextView.setText(userProfile.getName());

                        // Load profile image from URL
                        loadImageFromUrl(userProfile.getImageUrl());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    // Method to load image from URL using Volley
    private void loadImageFromUrl(String url) {
        ImageRequest imageRequest = new ImageRequest(url,
                response -> profileImageView.setImageBitmap(response),
                0, // Width
                0, // Height
                ImageView.ScaleType.CENTER_CROP,
                null, // Bitmap config
                error -> Toast.makeText(ProfileActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show());

        // Add the request to the RequestQueue
        requestQueue.add(imageRequest);
    }
}
