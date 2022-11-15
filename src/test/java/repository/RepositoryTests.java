package repository;

import org.junit.jupiter.api.*;
import repository.classes.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTests {
    private static Repository<User> userRepository;

    @BeforeAll
    static void beforeAll() {
        userRepository = new Repository<>(User.class);
        userRepository.createTable();
    }

    @Test
    @DisplayName("getAll Should return an empty list if table is empty")
    void getAll_NoRows_ReturnsEmptyList() {
        List<User> expected = new ArrayList<>();
        List<User> actual = userRepository.getAll();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getById Should return null if id does not exist")
    void getById_NoId_ReturnsNull() {
        User actual = userRepository.getById(999);

        assertEquals(null, actual);
    }

    @Test
    @DisplayName("getByProperty Should throw IllegalArgumentException if property does not exist")
    void getByProperty_NoProperty_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {userRepository
                .getByProperty("madeUpProperty", "someValue");});
    }

    @Test
    @DisplayName("getByProperty Should return empty list if no row matches that property value")
    void getByProperty_NoMatchingPropertyValue_ReturnsEmptyList() {
        List<User> expected = new ArrayList<>();
        List<User> actual = userRepository.getByProperty("name", "Non existing name");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("deleteByProperty Should throw IllegalArgumentException if property does not exist")
    void deleteByProperty_NoProperty_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {userRepository
                .deleteByProperty("madeUpProperty", "someValue");});
    }

    @Test
    @DisplayName("updateByProperty Should throw IllegalArgumentException if property does not exist")
    void updateByProperty_NoProperty_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {userRepository
                .updateByProperty("madeUpProperty", "someValue",
                        "id", "1");});
    }

    @Test
    @DisplayName("updateByProperty Should throw IllegalArgumentException if condition property does not exist")
    void updateByProperty_NoConditionProperty_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {userRepository
                .updateByProperty("name", "Ellie",
                        "madeUpProperty", "Bellik");});
    }

    @Test
    @DisplayName("createTable Should throw IllegalArgumentException if table already exists")
    void createTable_TableExists_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {userRepository.createTable();});
    }

    @AfterAll
    static void afterAll() {
        userRepository.dropTable();
    }
}

