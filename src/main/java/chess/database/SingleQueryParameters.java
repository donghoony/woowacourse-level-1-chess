package chess.database;

public class SingleQueryParameters {

    private final String[] parameters;

    public SingleQueryParameters(String... parameters) {
        this.parameters = parameters;
    }

    public String[] getParameters() {
        return parameters;
    }
}
