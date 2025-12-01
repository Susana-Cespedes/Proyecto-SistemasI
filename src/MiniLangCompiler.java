import java.util.List;

public class MiniLangCompiler {
    public static void main(String[] args) {
        // 1. Código Fuente de Entrada (MiniLang)
        // 10 + 20 - 5
        String sourceCode =
                "int a = 10; " +
                        "int b = 20; " +
                        "print a + b - 5;";
                //"print x+1";

        System.out.println("1. COMPILACIÓN");
        System.out.println("Código Fuente: " + sourceCode);

        try {
            // --- FASE DE COMPILACIÓN ---

            // Lexer
            Lexer lexer = new Lexer(sourceCode);
            List<Token> tokens = lexer.tokenize();

            // Parser & Generador
            Parser parser = new Parser(tokens);
            String intermediateCode = parser.parse();

            System.out.println("\n2. CÓDIGO INTERMEDIO GENERADO");
            System.out.println(intermediateCode);

            // --- FASE DE EJECUCIÓN (Runtime) ---

            System.out.println("\n3. EJECUCIÓN (Virtual Machine)");
            VirtualMachine vm = new VirtualMachine();
            vm.execute(intermediateCode);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            // e.printStackTrace(); // Descomentar para ver detalles si falla
        }
    }
}
