package com.cdc.desktop;

import com.badlogic.gdx.Gdx;
import com.cdc.MyGdxGame;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileWatcher extends Thread {
    private final File file,child;
    private AtomicBoolean stop = new AtomicBoolean(false);

    public FileWatcher(File child,File file) {
        this.file = file;
        this.child = child;
    }

    public boolean isStopped() { return stop.get(); }
    public void stopThread() { stop.set(true); }

    public void doOnChange() {
        System.out.println("FILE CHANGED!");
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                MyGdxGame.evaluate(child);
            }
        });
    }

    @Override
    public void run() {
        try {
            System.out.println("STARTED THREAD!");
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path path = file.toPath()/*.getParent()*/;
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
            while (!isStopped()) {
                WatchKey key;
                try { key = watcher.poll(25, TimeUnit.MILLISECONDS); }
                catch (InterruptedException e) { return; }
                if (key == null) { Thread.yield(); continue; }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    System.out.println("EVENT: "+kind.toString()+" to "+filename.toString()+" versus "+file.getName());
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        Thread.yield();
                        continue;
                    } else if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
                            //TODO: fix this to check the file name that is changed!
                            /*&& filename.toString().equals(file.getName())*/) {
                        doOnChange();
                    }
                    boolean valid = key.reset();
                    if (!valid) { break; }
                }
                Thread.yield();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}