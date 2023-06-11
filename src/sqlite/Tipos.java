package sqlite;

import java.lang.reflect.Type;

public enum Tipos {
	INTEGER,
	TEXT,
    NULL,
    REAL,
    BLOB;

    public static Tipos getTipo(Type tipo) {
    	switch(tipo.getTypeName()) {
    	case "java.util.Date":
    		return TEXT;
    	case "java.lang.String":
    		return TEXT;
    	case "java.lang.Integer":
    		return INTEGER;
    	case "java.lang.Boolean":
    		return BLOB;
    	case "java.lang.Float":
    		return REAL;
    	default: return NULL;
    	}

    }

}
