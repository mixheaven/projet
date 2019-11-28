package com.solotoband.repository;

import com.solotoband.entity.Groupe;

import java.sql.*;
import java.util.*;

import javax.sound.midi.Instrument;

public class GroupeRepository {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/solotoband?serverTimezone=GMT";
    private final static String DB_USER = "soloman";
    private final static String DB_PASSWORD = "annsolo";

    // Fonction save qui prend en paramètre les attribus du constructeur
    public static boolean createGroup(Groupe groupe) 
    {
        // Connection à la DB grâce aux indentifiants de l'user crée
        try {
            Connection connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD
            );

            // fonction et commande SQL afin de générer une clef et de créer l'insert dans la DB.
            PreparedStatement statement = connection.prepareStatement
            (
                "INSERT INTO groupe (group_name, contact_name, group_phone, group_email, group_info, instrument, music_flux, level, departement, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, groupe.getNameGroupe());
            statement.setString(2, groupe.getContactName());
            statement.setString(3, groupe.getGroupPhone());
            statement.setString(4, groupe.getGroupMail());
            statement.setString(5, groupe.getGroupInfo());
            statement.setString(6, groupe.getInstrument());
            statement.setString(7, groupe.getMusicFlux());
            statement.setString(8, groupe.getLevel());
            statement.setLong(9, groupe.getDepartement());
            statement.setString(10, groupe.getImage());


            // condition si l'execution de l'instruction SQL ne réussi pas.
            if (statement.executeUpdate() != 1) 
            {
                //throw new SQLException("failed to insert data");
                return false;
            }

            // déclaration et attribution de la variable qui récupère l'id.
            ResultSet generatedKeys = statement.getGeneratedKeys();

            // Si l'on a un id, appel au constructeur afin de générer un objet groupe pour la db.
            if (generatedKeys.next()) 
            {
                Long id = generatedKeys.getLong(1);
                groupe.setId(id); // Ajout de l'id dans l'objet
                return  true;
            } else 
            {
                //throw new SQLException("failed to get inserted id");
                return false;
            }
        } catch (SQLException e) 
            {
                return false;
            }
    }

    // méthode de selection par les musiciens dans la base de donnée groupe.
    // méthode qui prend en paramètre les champs de recherche de la page de recherche.
    public List<Groupe> findRequest(String instrument, String musicFlux, Long departement, String level) {

        try {
            Connection connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD
            );

            // commande du select dans la DB
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM groupe WHERE instrument = ? AND music_flux = ? AND departement = ? And level = ?;"
            );
            //Attributions des valeurs aux variables du constructeur.
            statement.setString(1, instrument);
            statement.setString(2, musicFlux);
            statement.setLong(3, departement);
            statement.setString(4, level);
            ResultSet resultSet = statement.executeQuery();

            //Création d'une arraylist avec en objet chaque groupe qui a été trouvé.
            List<Groupe> groupes = new ArrayList<>();

            //tant que l'on a un résultat...
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nameGroupe = resultSet.getString("group_name");
                String contactName = resultSet.getString("contact_name");
                String groupPhone = resultSet.getString("group_phone");
                String groupMail = resultSet.getString("group_email");
                String groupInfo = resultSet.getString("group_info");
                String image = resultSet.getString("image");

                // On ajoute l'objet groupe à l'arraylist.
                groupes.add(new Groupe(id, nameGroupe, contactName, groupPhone, 
                groupMail, groupInfo, instrument, musicFlux, level, departement, image));
            }
            return groupes; // On renvoi l'arraylist
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}