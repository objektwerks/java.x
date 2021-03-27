package homeschool.ui;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.springframework.context.support.ResourceBundleMessageSource;

public class Resources extends ResourceBundleMessageSource {
    public Resources() {
        setBasename("resources");
    }

    public String getString(String resource) {
        return getMessage(resource, null, null);
    }

    public int getInt(String resource) {
        return Integer.parseInt(getString(resource));
    }

    public double getDouble(String resource) {
        return Double.parseDouble(getString(resource));
    }

    public Image getImage(String resource) {
        return getImageIcon(resource).getImage();
    }

    public ImageIcon getImageIcon(String resource) {
        return new ImageIcon(getClass().getResource(getString(resource)));
    }
}