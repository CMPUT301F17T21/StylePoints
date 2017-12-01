package com.stylepoints.habittracker.repository.local;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by mchauck on 12/1/17.
 */

public class AsyncFileWriterParams {
    List list;
    BufferedWriter writer;
    FileOutputStream fos;

    public AsyncFileWriterParams(List list, FileOutputStream fos) {
        this.fos = fos;
        this.list = list;
        this.writer = new BufferedWriter(new OutputStreamWriter(fos));
    }
}
