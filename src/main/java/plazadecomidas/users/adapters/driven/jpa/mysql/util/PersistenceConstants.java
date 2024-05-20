package plazadecomidas.users.adapters.driven.jpa.mysql.util;

public class PersistenceConstants {

    private PersistenceConstants() {throw new IllegalStateException("Utility class");}

    public static final String USER_ALREADY_EXISTS_MESSAGE = "The user you are trying to add already exists in the database.";
    public static final String USER_NOT_FOUND_MESSAGE = "The user you are trying to find does not exist in the database.";
}
