package resources;

import javax.swing.*;

public class Images {

    public ImageIcon loadImage(String imageFilename) {
        return new ImageIcon(this.getClass().getResource(imageFilename).getFile());
    }

}
