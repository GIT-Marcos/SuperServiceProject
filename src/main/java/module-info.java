module org.superservice.superservice {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens org.superservice.superservice to javafx.fxml;
    exports org.superservice.superservice;
    exports org.superservice.superservice.entities;
    exports org.superservice.superservice.utilities;
    exports org.superservice.superservice.controllers;
    opens org.superservice.superservice.controllers to javafx.fxml;
    opens org.superservice.superservice.entities to org.hibernate.orm.core;
    exports org.superservice.superservice.DAOs;
    opens org.superservice.superservice.DAOs to javafx.fxml;
    opens org.superservice.superservice.DTOs to javafx.base;

}