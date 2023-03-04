package resources;

import java.io.InputStream;
import java.net.URL;

public class GetResources {
    public URL getFileResourceURL(String fileName) {
        return this.getClass().getResource(fileName);
    }

    public InputStream getFileResourceStream(String fileName) {
        return this.getClass().getResourceAsStream(fileName);
    }

}
