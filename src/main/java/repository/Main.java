package repository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello abadayy!");
        Repository repository = new Repository(User.class);
        List users = repository.executeQuery();
        System.out.println(users);
    }
}