package xyz.illuminate.git.repo.tasks.repo;

import org.eclipse.jgit.api.Git;

import xyz.illuminate.git.database.RepoContract;
import xyz.illuminate.git.database.models.Repo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class InitLocalTask extends RepoOpTask {

    public InitLocalTask(Repo repo) {
        super(repo);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean result = init();
        if (!result) {
            mRepo.deleteRepoSync();
            return false;
        }
        return true;
    }

    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        if (isSuccess) {
            mRepo.updateLatestCommitInfo();
            mRepo.updateStatus(RepoContract.REPO_STATUS_NULL);
        }
    }

    public boolean init() {
        try {
            Git.init().setDirectory(mRepo.getDir()).call();
        } catch (Throwable e) {
            setException(e);
            return false;
        }
        return true;
    }
}
