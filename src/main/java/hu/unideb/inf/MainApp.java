package hu.unideb.inf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.h2.tools.Server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.sql.SQLException;

public class MainApp extends Application {

    private static Scene scene;

    public static void main(String[] args) throws SQLException {
        startDatabase();

        System.out.println("Open your browser and navigate to http://localhost:8082/");
        System.out.println("JDBC URL: jdbc:h2:file:./my_database");
        System.out.println("User Name: sa");
        System.out.println("Password: ");

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        /*User newUser = new User();
        newUser.setFirstName("Alex");

        entityManager.getTransaction().begin();
        entityManager.persist(newUser);
        entityManager.getTransaction().commit();*/

        /*entityManager.getTransaction().begin();

        VehicleType vehicleType = new VehicleType();
        vehicleType.setName("Személygépjármű");

        VehicleType vehicleType1 = new VehicleType();
        vehicleType1.setName("Mezőgazdasági munkagép");

        entityManager.persist(vehicleType);
        entityManager.persist(vehicleType1);
        entityManager.createQuery("UPDATE Vehicle SET type_id = 1").executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();*/

        launch();
    }

    private static void startDatabase() throws SQLException {
        new Server().runTool("-tcp", "-web", "-ifNotExists");
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(loadFXML("HomePage"), 1647, 720);
        stage.setTitle("SFM Autókölcsönző");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
