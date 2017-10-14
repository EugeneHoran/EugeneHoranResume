package com.resume.horan.eugene.eugenehoranresume.post;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.databinding.ActivityNewPostBinding;
import com.resume.horan.eugene.eugenehoranresume.util.ui.LayoutUtil;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class NewPostActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private NewPostViewHolder newPostViewHolder;
    private ActivityNewPostBinding binding;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_post);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Post to feed");
        newPostViewHolder = new NewPostViewHolder(this);
        binding.setModel(newPostViewHolder);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_post:
                LayoutUtil.hideKeyboard(binding.toolbar);
                binding.bottomSheet.setVisibility(View.GONE);
                newPostViewHolder.makePost(binding.newPostText.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newPostViewHolder.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        newPostViewHolder.showImagePicker();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
