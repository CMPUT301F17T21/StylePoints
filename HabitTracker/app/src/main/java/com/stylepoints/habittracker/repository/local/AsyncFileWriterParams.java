package com.stylepoints.habittracker.repository.local;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Helper class to store the parameters needed for AsyncFileWriter
 *
 * @author Mackenzie Hauck
 * @see AsyncFileWriter
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
