package annotacions;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface Coluna {
	String nome() default "$";
	boolean notNull() default false;
	boolean PK() default false;
	boolean AI() default false;
}
