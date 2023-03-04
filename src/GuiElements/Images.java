package GuiElements;

import javax.swing.*;
import resources.GetResources;

public class Images {

    GetResources resources = new GetResources();

    public ImageIcon loadImage(String imageFilename) {
        return new ImageIcon(resources.getFileResourceURL(imageFilename));
    }

}
