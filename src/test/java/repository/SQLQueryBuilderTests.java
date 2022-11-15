package repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SQLQueryBuilderTests {
    private static Class<TestClass> testClass = TestClass.class;

    @Test
    @DisplayName("building a select * from where query should return expected string")
    void selectFromWhere_BuildsCorrectQuery() {
        List<String> conditions = new ArrayList<>();
        conditions.add("id = \"" + 1 + "\"");
        conditions.add("text = \"" + "some text" + "\"");

        String query = new SQLQuery.SQLQueryBuilder().select().from(testClass).where(conditions).build().toString()
                .trim().replaceAll(" +", " ");
        String expectedQuery = "SELECT * FROM testclass WHERE id = \"1\" AND text = \"some text\""
                .trim().replaceAll(" +", " ");

        assertTrue(query.compareTo(expectedQuery) == 0);
    }

    @Test
    @DisplayName("building a delete from where query should return expected string")
    void deleteFromWhere_BuildsCorrectQuery() {
        List<String> conditions = new ArrayList<>();
        conditions.add("id = \"" + 1 + "\"");
        conditions.add("text = \"" + "some text" + "\"");

        String query = new SQLQuery.SQLQueryBuilder().delete().from(testClass).where(conditions).build().toString()
                .trim().replaceAll(" +", " ");
        String expectedQuery = "DELETE FROM testclass WHERE id = \"1\" AND text = \"some text\""
                .trim().replaceAll(" +", " ");

        assertTrue(query.compareTo(expectedQuery) == 0);
    }

    @Test
    @DisplayName("building a drop table query should return expected string")
    void dropTable_BuildsCorrectQuery() {
        String query = new SQLQuery.SQLQueryBuilder().dropTable(testClass).build().toString()
                .trim().replaceAll(" +", " ");
        String expectedQuery = "DROP TABLE testclass"
                .trim().replaceAll(" +", " ");

        assertTrue(query.compareTo(expectedQuery) == 0);
    }

    @Test
    @DisplayName("building a truncate table query should return expected string")
    void truncateTable_BuildsCorrectQuery() {
        String query = new SQLQuery.SQLQueryBuilder().truncateTable(testClass).build().toString()
                .trim().replaceAll(" +", " ");
        String expectedQuery = "TRUNCATE TABLE testclass"
                .trim().replaceAll(" +", " ");

        assertTrue(query.compareTo(expectedQuery) == 0);
    }

    class TestClass {
        private int id;
        private String text;
        private List<String> list;

        public TestClass(int id, String text, List<String> list) {
            this.id = id;
            this.text = text;
            this.list = list;
        }
    }
}
