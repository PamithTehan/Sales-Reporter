package sales.reporter.output;

public class OutputWriterFactory {

    public OutputWriter create(String outputMethod, String outputFilePath) {
        if (outputMethod.equals("console")) {
            return new ConsoleWriter();
        } else if (outputMethod.equals("file")) {
            return new FileWriterHandler(outputFilePath);
        } else {
            System.out.println("Invalid output method: " + outputMethod);
            System.exit(1);
            return null;
        }
    }
}