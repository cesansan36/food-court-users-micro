package plazadecomidas.users.adapters.driving.http.rest.util;

public class ControllerAdapterConstants {

    private ControllerAdapterConstants() { throw new IllegalStateException("Utility class"); }

    public static final Long ADMIN_ROLE_ID = 1L;
    public static final Long OWNER_ROLE_ID = 2L;
    public static final Long EMPLOYEE_ROLE_ID = 3L;
    public static final Long CLIENT_ROLE_ID = 4L;

    public static final String ROLE_MISMATCH_MESSAGE = "The role id you are trying to use is not valid for this module";

    public static final String USER_CLAIM = "user";
}
