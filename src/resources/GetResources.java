package resources;

import java.net.URL;

public class GetResources {
    public URL getFileResource(String fileName) {
        return this.getClass().getResource(fileName);
    }

}
