package parse.ex3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MultipartFile {

    private String text;

    public String getText() {
        return text;
    }

    public MultipartFile(String text) {
        this.text = text;
    }

    public void store(String url) throws IOException {
        OutputStream output = new FileOutputStream(url);
        byte[] by = getText().getBytes();
        output.write(by);
        output.close();
    }
}
