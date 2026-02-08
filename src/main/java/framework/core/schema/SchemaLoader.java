package framework.core.schema;

import java.io.InputStream;

public final class SchemaLoader {

    private SchemaLoader() {}

    public static InputStream load(String path) {
        InputStream stream =
                SchemaLoader.class
                        .getClassLoader()
                        .getResourceAsStream("schemas/" + path);

        if (stream == null) {
            throw new RuntimeException("Schema not found: " + path);
        }

        return stream;
    }
}
