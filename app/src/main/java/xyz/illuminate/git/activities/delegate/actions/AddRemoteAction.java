package xyz.illuminate.git.activities.delegate.actions;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import timber.log.Timber;
import xyz.illuminate.git.R;
import xyz.illuminate.git.activities.RepoDetailActivity;
import xyz.illuminate.git.database.models.Repo;
import xyz.illuminate.git.dialogs.DummyDialogListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AddRemoteAction extends RepoAction {

    public AddRemoteAction(Repo repo, RepoDetailActivity activity) {
        super(repo, activity);
    }

    @Override
    public void execute() {
        showAddRemoteDialog();
        mActivity.closeOperationDrawer();
    }

    public void addToRemote(String name, String url) throws IOException {
        mRepo.setRemote(name, url);
        mRepo.updateRemote();
        mActivity.showToastMessage(R.string.success_remote_added);

    }

    public void showAddRemoteDialog() {
        com.google.android.material.dialog.MaterialAlertDialogBuilder builder = new com.google.android.material.dialog.MaterialAlertDialogBuilder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_add_remote, null);
        final EditText remoteName = (EditText) layout
                .findViewById(R.id.remoteName);
        final EditText remoteUrl = (EditText) layout
                .findViewById(R.id.remoteUrl);

        builder.setTitle(R.string.dialog_add_remote_title)
                .setView(layout)
                .setPositiveButton(R.string.dialog_add_remote_positive_label,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {
                                String name = remoteName.getText().toString();
                                String url = remoteUrl.getText().toString();
                                try {
                                    addToRemote(name, url);
                                } catch (IOException e) {
                                    Timber.e(e);
                                    mActivity.showMessageDialog(R.string.dialog_error_title,
                                            mActivity.getString(R.string.error_something_wrong));
                                }
                            }
                        })
                .setNegativeButton(R.string.label_cancel,
                        new DummyDialogListener()).show();
    }

}
