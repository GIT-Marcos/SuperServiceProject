module org.superservice.superservice {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.apache.poi.ooxml;
    requires com.github.librepdf.openpdf;
    requires java.desktop;
    requires org.jfree.jfreechart;
    requires jbcrypt;
    requires org.postgresql.jdbc;


    opens org.superservice.superservice to javafx.fxml;
    exports org.superservice.superservice;
    exports org.superservice.superservice.entities;
    exports org.superservice.superservice.utilities;
    exports org.superservice.superservice.controllers;
    exports org.superservice.superservice.DTOs;
    opens org.superservice.superservice.controllers to javafx.fxml;
    opens org.superservice.superservice.entities to org.hibernate.orm.core;
    exports org.superservice.superservice.DAOs;
    opens org.superservice.superservice.DAOs to javafx.fxml;
    opens org.superservice.superservice.DTOs to javafx.base;
    exports org.superservice.superservice.excepciones;

}