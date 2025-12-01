import java.util.*;

public class VirtualMachine {
    // La Pila: donde ocurren los cálculos temporales
    private Stack<Integer> stack = new Stack<>();

    // La Memoria RAM: donde guardamos las variables (ej. 'a' -> 10)
    private Map<String, Integer> memory = new HashMap<>();

    public void execute(String code) {
        System.out.println("--- Iniciando Ejecución en VM ---");

        // Dividimos el código intermedio en líneas (instrucciones)
        String[] lines = code.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            // Separamos la instrucción de su argumento (ej: "PUSH 10" -> ["PUSH", "10"])
            String[] parts = line.trim().split(" ");
            String instruction = parts[0];
            String arg = (parts.length > 1) ? parts[1] : null;

            switch (instruction) {
                case "PUSH":
                    // Mete un número en la cima de la pila
                    int value = Integer.parseInt(arg);
                    stack.push(value);
                    // System.out.println("Debug: Pushed " + value);
                    break;

                case "STORE":
                    // Saca el valor de la cima y lo guarda en la variable indicada
                    int valToStore = stack.pop();
                    memory.put(arg, valToStore);
                    // System.out.println("Debug: Stored " + valToStore + " in " + arg);
                    break;

                case "LOAD":
                    // Busca el valor de la variable y lo mete a la pila
                    if (!memory.containsKey(arg)) {
                        throw new RuntimeException("Error en ejecución: Variable '" + arg + "' no definida.");
                    }
                    stack.push(memory.get(arg));
                    break;

                case "ADD":
                    // Saca dos números, los suma y mete el resultado
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a + b);
                    break;

                case "SUB":
                    // Saca dos números, los resta y mete el resultado
                    // Nota: el primero en salir es el sustraendo (el de la derecha)
                    int subB = stack.pop();
                    int subA = stack.pop();
                    stack.push(subA - subB);
                    break;

                case "PRINT":
                    // Saca el valor y lo muestra al usuario final
                    if (stack.isEmpty()) {
                        System.out.println("Nada para imprimir");
                    } else {
                        System.out.println("SALIDA: " + stack.pop());
                    }
                    break;

                default:
                    throw new RuntimeException("Instrucción desconocida: " + instruction);
            }
        }
        System.out.println("--- Fin de Ejecución ---");
    }
}