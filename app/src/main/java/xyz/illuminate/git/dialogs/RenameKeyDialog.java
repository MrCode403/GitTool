package xyz.illuminate.git.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

import xyz.illuminate.git.R;
import xyz.illuminate.git.activities.explorer.PrivateKeyManageActivity;
import xyz.illuminate.git.ssh.PrivateKeyUtils;
import xyz.illuminate.git.views.SheimiDialogFragment;

/**
 * Created by sheimi on 8/24/13.
 */
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RenameKeyDialog extends SheimiDialogFragment implements
        View.OnClickListener, DialogInterface.OnClickListener {

    public static final String FROM_PATH = "from path";
    private File mFromFile;
    private String mFromPath;
    private EditText mNewFilename;
    private PrivateKeyManageActivity mActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mActivity = (PrivateKeyManageActivity) getActivity();
        com.google.android.material.dialog.MaterialAlertDialogBuilder builder = new com.google.android.material.dialog.MaterialAlertDialogBuilder(mActivity);
        Bundle args = getArguments();
        if (args != null && args.containsKey(FROM_PATH)) {
            mFromPath = args.getString(FROM_PATH);
        }
        mFromFile = new File(mFromPath);

        builder.setTitle(getString(R.string.dialog_rename_key_title));
        View view = mActivity.getLayoutInflater().inflate(
                R.layout.dialog_rename_key, null);

        builder.setView(view);
        mNewFilename = (EditText) view.findViewById(R.id.newFilename);
        mNewFilename.setText(mFromFile.getName());

        // set button listener
        builder.setNegativeButton(R.string.label_cancel,
                new DummyDialogListener());
        builder.setPositiveButton(R.string.label_rename,
                new DummyDialogListener());

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FROM_PATH, mFromPath);
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog == null)
            return;
        Button positiveButton = (Button) dialog
                .getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String newFilename = mNewFilename.getText().toString().trim();
        if (newFilename.equals("")) {
            showToastMessage(R.string.alert_new_filename_required);
            mNewFilename
                    .setError(getString(R.string.alert_new_filename_required));
            return;
        }

        if (newFilename.contains("/")) {
            showToastMessage(R.string.alert_filename_format);
            mNewFilename.setError(getString(R.string.alert_filename_format));
            return;
        }

        File file = new File(mFromFile.getParentFile(), newFilename);
        if (file.exists()) {
            showToastMessage(R.string.alert_file_exists);
            mNewFilename.setError(getString(R.string.alert_file_exists));
            return;
        }
        mFromFile.renameTo(file);
        try {
            PrivateKeyUtils.getPublicKey(mFromFile).renameTo(PrivateKeyUtils.getPublicKey(file));
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
        mActivity.refreshList();
        dismiss();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
    }

}
