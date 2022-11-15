package repository.annotations;

public @interface Auto_Increment {
    Constraints AutoIncrement() default Constraints.AUTO_INCREMENT;
}
