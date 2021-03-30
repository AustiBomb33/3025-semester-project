package com.marcoux.simplysale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.marcoux.simplysale.databinding.ActivityCreateListingBinding
import com.marcoux.simplysale.databinding.ActivityMainBinding

class CreateListingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateListingBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateListingBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val backButton = findViewById<Button>(R.id.backButton);
        backButton.setOnClickListener {
            finish();
        }
    }
}