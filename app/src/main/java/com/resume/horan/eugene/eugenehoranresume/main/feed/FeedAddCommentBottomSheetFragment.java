package com.resume.horan.eugene.eugenehoranresume.main.feed;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.resume.horan.eugene.eugenehoranresume.R;
import com.resume.horan.eugene.eugenehoranresume.util.Common;
import com.resume.horan.eugene.eugenehoranresume.util.ui.MultiTextWatcher;
import com.resume.horan.eugene.eugenehoranresume.util.ui.TextInputView;

public class FeedAddCommentBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static FeedAddCommentBottomSheetFragment newInstance(String postKey) {
        FeedAddCommentBottomSheetFragment fragment = new FeedAddCommentBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(Common.ARG_POST_KEY, postKey);
        fragment.setArguments(args);
        return fragment;
    }

    private String strPostKey;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strPostKey = getArguments().getString(Common.ARG_POST_KEY);
        }
        Log.e("Testing", strPostKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_bs_request_data, container, false);
    }

    private Button btnComplete;
    private EditText editComment;

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        TextView title = v.findViewById(R.id.title);
        title.setText(R.string.add_comment);

        btnComplete = v.findViewById(R.id.btnComplete);
        btnComplete.setText(R.string.post_comment);
        btnComplete.setOnClickListener(this);
        btnComplete.setEnabled(false);

        TextInputView inputEmail = v.findViewById(R.id.inputEmail);
        inputEmail.setHint("Comment");
        editComment = v.findViewById(R.id.editEmail);
        editComment.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        new MultiTextWatcher()
                .registerEditText(editComment)
                .setCallback(textWatcherInterface);
    }

    @Override
    public void onClick(View view) {
        if (view == btnComplete) {
            if (listener != null) {
                if (!TextUtils.isEmpty(editComment.getText().toString())) {
                    listener.addCommentToPost(strPostKey, editComment.getText().toString().trim());
                    getDialog().dismiss();
                }
            }
        }
    }

    private MultiTextWatcher.MultiTextWatcherInterface textWatcherInterface = new MultiTextWatcher.MultiTextWatcherInterface() {
        @Override
        public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(editComment.getText().toString())) {
                btnComplete.setEnabled(true);
            } else {
                btnComplete.setEnabled(false);
            }
        }
    };

    /**
     * Callbacks
     */

    private Listener listener;

    public interface Listener {
        void addCommentToPost(String postKey, String postComment);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
