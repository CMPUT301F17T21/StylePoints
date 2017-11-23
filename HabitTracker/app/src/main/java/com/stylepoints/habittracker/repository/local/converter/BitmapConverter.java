package com.stylepoints.habittracker.repository.local.converter;

import android.arch.persistence.room.TypeConverter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by qikai on 2017-11-19.
 */

public class BitmapConverter {
    @TypeConverter
    public static Bitmap toBitmap(byte[] photoByte) {
        if (photoByte == null) {
            return null;
        } else {
            return BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
        }
    }

    @TypeConverter
    public static byte[] toBlob(Bitmap photo) {
        if (photo == null) {
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }
    }

}
