package homeschool.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import homeschool.config.Config;

public class App extends Application {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static AnnotationConfigApplicationContext context;
    private static SpringFxmlLoader loader;

    private Resources resources;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(Config.class);
        context.registerShutdownHook();
        loader = new SpringFxmlLoader(context);
        launch(App.class, args);
    }

    @Override
    public void init() throws Exception {
        resources = context.getBean(Resources.class);
        logger.info("App init.");
    }

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("App start.");
        stage.setTitle(resources.getString("app.title"));
        stage.setResizable(true);
        Parent rootPane = (Parent) loader.load("/homeschool.fxml");
        Scene scene = new Scene(rootPane, resources.getInt("scene.width"), resources.getInt("scene.height"));
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        logger.info("App stop.");
    }
}