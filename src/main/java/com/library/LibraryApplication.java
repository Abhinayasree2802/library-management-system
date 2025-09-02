package com.library;

import com.library.dao.AuthorDAO;
import com.library.dao.BookDAO;
import com.library.resources.AuthorResource;
import com.library.resources.BookResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import java.sql.Connection;
import java.sql.SQLException;

public class LibraryApplication extends Application<LibraryConfiguration> {

    public static void main(final String[] args) throws Exception {
        new LibraryApplication().run(args);
    }

    @Override
    public String getName() {
        return "library-management-system";
    }

    @Override
    public void initialize(final Bootstrap<LibraryConfiguration> bootstrap) {
        // No Hibernate bundle needed
    }

    @Override
    public void run(final LibraryConfiguration configuration, final Environment environment) {
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        try {
            // Get a single connection from the data source
            Connection connection = dataSourceFactory.build(environment.metrics(), "mysql").getConnection();

            // Create DAOs with the connection
            final AuthorDAO authorDAO = new AuthorDAO(connection);
            final BookDAO bookDAO = new BookDAO(connection);

            // Register resources with the DAOs
            environment.jersey().register(new AuthorResource(authorDAO));
            environment.jersey().register(new BookResource(bookDAO));
        } catch (SQLException e) {
            // Handle connection errors
            e.printStackTrace();
        }
    }
}

//package com.library;
//
//import com.library.dao.AuthorDAO;
//import com.library.dao.BookDAO;
//import com.library.resources.AuthorResource;
//import com.library.resources.BookResource;
//import io.dropwizard.Application;
//import io.dropwizard.assets.AssetsBundle;
//import io.dropwizard.db.DataSourceFactory;
//import io.dropwizard.setup.Bootstrap;
//import io.dropwizard.setup.Environment;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class LibraryApplication extends Application<LibraryConfiguration> {
//
//    public static void main(final String[] args) throws Exception {
//        new LibraryApplication().run(args);
//    }
//
//    @Override
//    public String getName() {
//        return "library-management-system";
//    }
//
//    @Override
//    public void initialize(final Bootstrap<LibraryConfiguration> bootstrap) {
//        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
//    }
//
//    @Override
//    public void run(final LibraryConfiguration configuration, final Environment environment) {
//        try {
//            DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
//            Connection connection = dataSourceFactory.build(environment.metrics(), "mysql").getConnection();
//            connection.setAutoCommit(true);
//
//            final AuthorDAO authorDAO = new AuthorDAO(connection);
//            final BookDAO bookDAO = new BookDAO(connection);
//
//            environment.jersey().register(new AuthorResource(authorDAO));
//            environment.jersey().register(new BookResource(bookDAO));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}