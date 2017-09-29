package com.resume.horan.eugene.eugenehoranresume.ui.main.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.base.BaseInterface;
import com.resume.horan.eugene.eugenehoranresume.util.Common;

public class ContactFragment extends Fragment {
    public static ContactFragment newInstance() {

        return new ContactFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mCallback.whichFragment(Common.WHICH_CONTACT_FRAGMENT);
    }

    private ContactInterface mCallback;

    public interface ContactInterface extends BaseInterface {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactInterface) {
            mCallback = (ContactInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
