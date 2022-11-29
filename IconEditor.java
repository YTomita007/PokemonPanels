package PokemonPanels;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;

public class IconEditor {

    public static ImageIcon setIcon(ArrayList<String> pokemons, int num, int side){
        ImageIcon icon = new ImageIcon("./PokemonPanels/images/" + Integer.parseInt(pokemons.get(num).substring(1)) + ".png");
        icon = resizeIcon(icon, side-1, side-1);
        return icon;
    }

    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height){
        Image CGresize = icon.getImage();
        BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(CGresize, 0, 0, width, height, null);
        g2.dispose();
        ImageIcon resized = new ImageIcon();
        resized.setImage(resizedImg);
        return resized;
    }
}
