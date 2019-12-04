package com.example.flickrexample;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class creates and holds executors for the whole app so that we can group async tasks and
 * execute them independently from one another.
 *
 * For example we may want to run network requests with a thread pool and DB requests with a
 * separate, single thread. That way, if there are network issues then we won't block DB requests and
 * vice versa.
 */
public class AppExecutors {

    private final Executor mDiskIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
    }

    /**
     * Creates the default AppExecutors containing Executors for networkIO, DiskIO, and a main thread Executor.
     */
    AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(1), new MainThreadExecutor());
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
