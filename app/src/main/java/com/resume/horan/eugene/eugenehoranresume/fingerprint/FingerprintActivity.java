package com.resume.horan.eugene.eugenehoranresume.fingerprint;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.ActivityFingerprintBinding;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class FingerprintActivity extends AppCompatActivity {
    private FingerprintViewModel fingerprintViewModel;
    private int whichFingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFingerprintBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fingerprint);
        whichFingerprint = getIntent().getIntExtra(Common.ARG_FINGERPRINT_TYPE, Common.WHICH_FINGERPRINT_LOGIN);
        fingerprintViewModel = new FingerprintViewModel(this, binding);
        binding.setHolder(fingerprintViewModel);
        if (whichFingerprint == Common.WHICH_FINGERPRINT_SETTINGS) {
            binding.toolbar.setVisibility(View.VISIBLE);
            binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    returnIntent.putExtra(Common.ARG_FINGERPRINT_RETURN_ERROR, "Fingerprint cancelled");
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (whichFingerprint == Common.WHICH_FINGERPRINT_LOGIN) {

        } else {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            returnIntent.putExtra(Common.ARG_FINGERPRINT_RETURN_ERROR, "Fingerprint cancelled");
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fingerprintViewModel.onResume();
    }


    @Override
    protected void onPause() {
        fingerprintViewModel.onPause();
        super.onPause();
    }
}
