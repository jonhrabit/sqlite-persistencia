package sqlite;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Target;

@Target(FIELD)
public @interface SQLITE {
	String nome() default "$";
	boolean notNull() default false;
	boolean PK() default false;
	boolean AI() default false;
}
