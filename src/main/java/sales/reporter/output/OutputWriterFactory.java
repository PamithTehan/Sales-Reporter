package sales.reporter.output;


public class OutputWriterFactory {

    public OutputWriter create(String outputMethod, String outputFilePath) throws InvalidOutputMethodException {
        if (outputMethod == null) {
            throw new InvalidOutputMethodException("Output method must not be null.");
        }

        if (outputMethod.equals("console")) {
            return new ConsoleWriter();
        } else if (outputMethod.equals("file")) {
            if (outputFilePath == null || outputFilePath.isBlank()) {
                throw new InvalidOutputMethodException(
                        "An output file path is required when output-method is 'file'.");
            }
            return new FileWriterHandler(outputFilePath);
        } else {
            throw new InvalidOutputMethodException("Invalid output method: " + outputMethod);
        }
    }
}