package homeschool.ui;

import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringFxmlLoader {
    private AnnotationConfigApplicationContext context;

    public SpringFxmlLoader(AnnotationConfigApplicationContext context) {
        this.context = context;
    }

    public Object load(String url) {
        try (InputStream inputStream = SpringFxmlLoader.class.getResourceAsStream(url)) {
            FXMLLoader loader = new FXMLLoader();
            loader.setControllerFactory(new Callback<Class<?>, Object>() {
                @Override
                public Object call(Class<?> clazz) {
                    return context.getBean(clazz);
                }
            });
            return loader.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}