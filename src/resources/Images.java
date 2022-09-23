package resources;

import javax.swing.*;
import java.util.Objects;

public class Images {

    public ImageIcon loadImage(String imageFilename) {
        return new ImageIcon(Objects.requireNonNull(this.getClass().getResource(imageFilename)).getFile());
    }

}
