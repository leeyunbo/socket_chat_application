package Model;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;

public class FilePractice {
    final private String FILENAME = "test.txt";
    final private String string = "hello world!";
    Context ctx;

    try
    {
        FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
    } catch(
    IOException e)

    {
        e.printStackTrace();
    } catch(
    FileNotFoundException e)

    {
        e.printStackTrace();
    }
}


