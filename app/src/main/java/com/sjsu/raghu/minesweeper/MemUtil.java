package com.sjsu.raghu.minesweeper;

/**
 * Created by Raghu on 2/26/2016.
 */
public class MemUtil {

    public static final float BYTES_MB = 1024.0f * 1024.0f;

    public static float mbFree(){
        final Runtime rt = Runtime.getRuntime();
        final float bytesUsed = rt.totalMemory();
        final float mbUsed = bytesUsed/BYTES_MB;
        final float mbfree = mbAvailable() - mbUsed;
        return mbfree;
    }

    public static float mbAvailable(){
        final Runtime rt = Runtime.getRuntime();
        final float bytesAvail = rt.maxMemory();
        return bytesAvail / BYTES_MB;

    }
}
