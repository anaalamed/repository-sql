package repository.annotations;

public enum Constraints {
    PRIMARY_KEY("PRIMARY KEY"),
    NOT_NULL("NOT NULL"),
    UNIQUE("UNIQUE");

    Constraints(String fieldName){
       this.fieldName=fieldName;
    }

    @Override
    public String toString() {
        return fieldName;
    }
    public final String fieldName;


}
