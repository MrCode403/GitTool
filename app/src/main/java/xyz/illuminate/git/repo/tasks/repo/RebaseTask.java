package xyz.illuminate.git.repo.tasks.repo;

import xyz.illuminate.git.R;
import xyz.illuminate.git.database.models.Repo;
import xyz.illuminate.git.exception.StopTaskException;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RebaseTask extends RepoOpTask {

    public String mUpstream;
    private AsyncTaskPostCallback mCallback;

    public RebaseTask(Repo repo, String upstream, AsyncTaskPostCallback callback) {
        super(repo);
        mUpstream = upstream;
        mCallback = callback;
        setSuccessMsg(R.string.success_rebase);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return rebase();
    }

    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        if (mCallback != null) {
            mCallback.onPostExecute(isSuccess);
        }
    }

    public boolean rebase() {
        try {
            mRepo.getGit().rebase().setUpstream(mUpstream).call();
        } catch (StopTaskException e) {
            return false;
        } catch (Throwable e) {
            setException(e);
            return false;
        }
        return true;
    }
}
