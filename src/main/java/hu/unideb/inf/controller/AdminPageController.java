package hu.unideb.inf.controller;

import hu.unideb.inf.model.Vehicle;
import hu.unideb.inf.model.VehicleType;
import hu.unideb.inf.repository.VehicleDAO;
import hu.unideb.inf.repository.VehicleDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {

    @FXML
    private Button addVehicleBt;
    @FXML
    private Button deleteBt;
    @FXML
    private Button logoutButton;

    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private TextField vehicleTypeTF;
    @FXML
    private TextField makeTF;
    @FXML
    private TextField modelTF;
    @FXML
    private TextField yearTF;
    @FXML
    private TextField engineTF;
    @FXML
    private TextField fuelTF;
    @FXML
    private TextField seatingTF;
    @FXML
    private TextField priceTF;
    @FXML
    private Button backButton;
    @FXML
    private ComboBox<VehicleType> typeIDCB;

    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML
    private TableColumn<Vehicle, String> makeColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, Integer> yearColumn;
    @FXML
    private TableColumn<Vehicle, String> engineColumn;
    @FXML
    private TableColumn<Vehicle, String> fuelTypeColumn;
    @FXML
    private TableColumn<Vehicle, Integer> seatingCapacityColumn;
    @FXML
    private TableColumn<Vehicle, String> priceColumn;
    @FXML
    private TableColumn<Vehicle, Integer> TypeIDColumn;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        entityManager = entityManagerFactory.createEntityManager();

        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        engineColumn.setCellValueFactory(new PropertyValueFactory<>("engine"));
        fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        seatingCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("seatingCapacity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TypeIDColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        loadDataFromDatabase();
        loadVehicleTypes();
    }

    private void loadVehicleTypes() {
        VehicleDAO vehicleDAO = new VehicleDAOImpl(entityManagerFactory, entityManager);
        List<VehicleType> vehicleTypes = vehicleDAO.getAllVehicleTypes();

        typeIDCB.setItems(FXCollections.observableArrayList(vehicleTypes));

        typeIDCB.setCellFactory(param -> new ListCell<VehicleType>() {
            @Override
            protected void updateItem(VehicleType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });

        typeIDCB.setButtonCell(new ListCell<VehicleType>() {
            @Override
            protected void updateItem(VehicleType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });
    }

    private void loadDataFromDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        VehicleDAO vehicleDAO = new VehicleDAOImpl(entityManagerFactory, entityManager);

        List<Vehicle> vehicles = vehicleDAO.getVehicles();
        vehicleList.setAll(vehicles);

        entityManager.getTransaction().commit();
        entityManager.close();

        vehicleTable.setItems(vehicleList);
    }

    @FXML
    private void addVehicleBtClicked(ActionEvent event) {
        if (vehicleTypeTF.getText().isEmpty() || makeTF.getText().isEmpty() || modelTF.getText().isEmpty() ||
                yearTF.getText().isEmpty() || engineTF.getText().isEmpty() || fuelTF.getText().isEmpty() ||
                seatingTF.getText().isEmpty() || priceTF.getText().isEmpty() || typeIDCB.getSelectionModel().getSelectedItem() == null) {

            System.out.println("Kérlek töltsd/jelöld ki az összes mezőt.");
            return;
        }

        String vehicleType = vehicleTypeTF.getText();
        String make = makeTF.getText();
        String model = modelTF.getText();
        int year = Integer.parseInt(yearTF.getText());
        String engine = engineTF.getText();
        String fuelType = fuelTF.getText();
        int seatingCapacity = Integer.parseInt(seatingTF.getText());
        int price = Integer.parseInt(priceTF.getText());
        VehicleType selectedType = typeIDCB.getSelectionModel().getSelectedItem();

        Vehicle newVehicle = new Vehicle(vehicleType, make, model, year, engine, fuelType, seatingCapacity, price, selectedType);

        VehicleDAO vehicleDAO = new VehicleDAOImpl(entityManagerFactory, entityManager);
        vehicleDAO.insertVehicle(newVehicle);

        loadDataFromDatabase();
    }

    @FXML
    private void deleteVehicleBtClicked(ActionEvent event) {

        List<Vehicle> selectedVehicle = vehicleTable.getSelectionModel().getSelectedItems();
        long vehicleID;
        VehicleDAO vehicleDAO = new VehicleDAOImpl(entityManagerFactory, entityManager);

        for(Vehicle vehicle : selectedVehicle) {
            System.out.println(vehicle.getId());
            vehicleDAO.deleteVehicle(vehicle.getId());
            vehicleTable.getItems().remove(vehicle);
            System.out.println("Törölve!");
        }
    }

    @FXML
    private void backToHome(ActionEvent event) {
        try {
            hu.unideb.inf.MainApp.setRoot("HomePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logoutUser(ActionEvent event) {
        SessionManager.logout();
        try {
            hu.unideb.inf.MainApp.setRoot("HomePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
