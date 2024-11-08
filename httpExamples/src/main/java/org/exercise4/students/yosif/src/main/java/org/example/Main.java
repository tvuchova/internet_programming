package org.example;

import java.io.IOException;

import static org.example.ReqResClient.*;

public class Main {
    public static void main(String[] args) throws IOException{

            listUsers();

            getSingleUser(2);

            handleUserNotFound(23);

            createUser("John", "Developer");

            updateUser(2, "Jane", "Manager");

            deleteUser(2);
        }
    }