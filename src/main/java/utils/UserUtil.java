package utils;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {
    private List<String> users = new ArrayList<>();

    public void addUser(String username) {
        users.add(username);
    }
}
