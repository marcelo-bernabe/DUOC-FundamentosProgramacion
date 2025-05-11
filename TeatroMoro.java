import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TeatroMoro {
    // Constantes para tipos de asientos y descuentos
    private static final String[] TIPOS_ASIENTO = {"VIP", "Palco", "Platea baja", "Platea alta", "Galería"};
    private static final double[] PRECIOS_ASIENTO = {50000, 40000, 30000, 20000, 10000};
    private static final HashMap<String, Double> DESCUENTOS = new HashMap<String, Double>() {{
        put("Niño", 0.10);
        put("Mujer", 0.20);
        put("Estudiante", 0.15);
        put("Tercera edad", 0.25);
    }};
    
    // Estructuras de datos para manejar los asientos
    private static HashMap<String, ArrayList<Boolean>> asientosDisponibles;
    private static ArrayList<Entrada> entradasVendidas;
    
    // Scanner para entrada de usuario
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarSistema();
        mostrarMenuPrincipal();
    }
    
    private static void inicializarSistema() {
        // Inicializar la disponibilidad de asientos
        asientosDisponibles = new HashMap<>();
        for (int i = 0; i < TIPOS_ASIENTO.length; i++) {
            ArrayList<Boolean> asientos = new ArrayList<>();
            // Cada sección tiene 10 asientos inicialmente
            for (int j = 0; j < 10; j++) {
                asientos.add(true); // true = disponible
            }
            asientosDisponibles.put(TIPOS_ASIENTO[i], asientos);
        }
        
        // Inicializar lista de entradas vendidas
        entradasVendidas = new ArrayList<>();
    }
    
    private static void mostrarMenuPrincipal() {
        boolean salir = false;
        
        while (!salir) {
            System.out.println("\n=== SISTEMA DE VENTA DE ENTRADAS - TEATRO MORO ===");
            System.out.println("1. Vender entrada");
            System.out.println("2. Mostrar asientos disponibles");
            System.out.println("3. Mostrar boletas vendidas");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                
                switch (opcion) {
                    case 1:
                        venderEntrada();
                        break;
                    case 2:
                        mostrarAsientosDisponibles();
                        break;
                    case 3:
                        mostrarBoletasVendidas();
                        break;
                    case 4:
                        salir = true;
                        System.out.println("Gracias por usar el sistema. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número.");
                scanner.nextLine(); // Limpiar buffer
            }
        }
    }
    
    private static void venderEntrada() {
        System.out.println("\n=== VENTA DE ENTRADA ===");
        
        // Seleccionar tipo de asiento
        int tipoAsiento = seleccionarTipoAsiento();
        if (tipoAsiento == -1) return;
        
        // Seleccionar asiento específico
        int numeroAsiento = seleccionarAsiento(tipoAsiento);
        if (numeroAsiento == -1) return;
        
        // Obtener datos del cliente
        System.out.print("Ingrese nombre del cliente: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingrese edad del cliente: ");
        int edad;
        try {
            edad = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
        } catch (InputMismatchException e) {
            System.out.println("Error: La edad debe ser un número.");
            scanner.nextLine(); // Limpiar buffer
            return;
        }
        
        // Aplicar descuento según categoría
        String categoria = determinarCategoriaCliente(edad);
        double descuento = DESCUENTOS.getOrDefault(categoria, 0.0);
        
        // Calcular precio final
        double precioBase = PRECIOS_ASIENTO[tipoAsiento];
        double precioFinal = precioBase * (1 - descuento);
        
        // Crear y guardar la entrada
        Entrada entrada = new Entrada(nombre, edad, categoria, TIPOS_ASIENTO[tipoAsiento], 
                                     numeroAsiento, precioBase, descuento, precioFinal);
        entradasVendidas.add(entrada);
        
        // Marcar asiento como ocupado
        asientosDisponibles.get(TIPOS_ASIENTO[tipoAsiento]).set(numeroAsiento - 1, false);
        
        // Imprimir boleta
        imprimirBoleta(entrada);
    }
    
    private static int seleccionarTipoAsiento() {
        System.out.println("\nTipos de asiento disponibles:");
        for (int i = 0; i < TIPOS_ASIENTO.length; i++) {
            System.out.printf("%d. %s - $%,.0f%n", i + 1, TIPOS_ASIENTO[i], PRECIOS_ASIENTO[i]);
        }
        
        System.out.print("Seleccione el tipo de asiento (0 para cancelar): ");
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            if (opcion == 0) return -1;
            if (opcion < 1 || opcion > TIPOS_ASIENTO.length) {
                System.out.println("Opción no válida.");
                return -1;
            }
            
            return opcion - 1; // Convertir a índice base 0
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número.");
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }
    
    private static int seleccionarAsiento(int tipoAsiento) {
        String tipo = TIPOS_ASIENTO[tipoAsiento];
        ArrayList<Boolean> asientos = asientosDisponibles.get(tipo);
        
        System.out.println("\nAsientos disponibles para " + tipo + ":");
        for (int i = 0; i < asientos.size(); i++) {
            if (asientos.get(i)) {
                System.out.print((i + 1) + " ");
            } else {
                System.out.print("X ");
            }
        }
        System.out.println("\n(X = ocupado)");
        
        System.out.print("Seleccione número de asiento (0 para cancelar): ");
        try {
            int numero = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            if (numero == 0) return -1;
            if (numero < 1 || numero > asientos.size()) {
                System.out.println("Número de asiento no válido.");
                return -1;
            }
            if (!asientos.get(numero - 1)) {
                System.out.println("Asiento ya ocupado. Por favor seleccione otro.");
                return -1;
            }
            
            return numero;
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número.");
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }
    
    private static String determinarCategoriaCliente(int edad) {
        System.out.println("\nCategorías de cliente:");
        System.out.println("1. Niño (0-12 años) - 10% descuento");
        System.out.println("2. Mujer - 20% descuento");
        System.out.println("3. Estudiante (con credencial) - 15% descuento");
        System.out.println("4. Tercera edad (60+ años) - 25% descuento");
        System.out.println("5. Ninguna - Sin descuento");
        
        System.out.print("Seleccione categoría del cliente: ");
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    if (edad <= 12) return "Niño";
                    else {
                        System.out.println("El cliente no cumple con el rango de edad para niño.");
                        return "Ninguna";
                    }
                case 2:
                    return "Mujer";
                case 3:
                    System.out.print("¿Posee credencial de estudiante? (S/N): ");
                    String respuesta = scanner.nextLine();
                    if (respuesta.equalsIgnoreCase("S")) return "Estudiante";
                    else return "Ninguna";
                case 4:
                    if (edad >= 60) return "Tercera edad";
                    else {
                        System.out.println("El cliente no cumple con el rango de edad para tercera edad.");
                        return "Ninguna";
                    }
                case 5:
                    return "Ninguna";
                default:
                    System.out.println("Opción no válida. No se aplicará descuento.");
                    return "Ninguna";
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número. No se aplicará descuento.");
            scanner.nextLine(); // Limpiar buffer
            return "Ninguna";
        }
    }
    
    private static void imprimirBoleta(Entrada entrada) {
        System.out.println("\n=== BOLETA TEATRO MORO ===");
        System.out.println("Cliente: " + entrada.getNombre());
        System.out.println("Edad: " + entrada.getEdad());
        System.out.println("Categoría: " + entrada.getCategoria());
        System.out.println("Tipo de asiento: " + entrada.getTipoAsiento());
        System.out.println("Número de asiento: " + entrada.getNumeroAsiento());
        System.out.printf("Precio base: $%,.0f%n", entrada.getPrecioBase());
        System.out.printf("Descuento aplicado: %.0f%%%n", entrada.getDescuento() * 100);
        System.out.printf("PRECIO FINAL: $%,.0f%n", entrada.getPrecioFinal());
        System.out.println("==========================");
    }
    
    private static void mostrarAsientosDisponibles() {
        System.out.println("\n=== ASIENTOS DISPONIBLES ===");
        for (String tipo : TIPOS_ASIENTO) {
            System.out.println("\n" + tipo + ":");
            ArrayList<Boolean> asientos = asientosDisponibles.get(tipo);
            int disponibles = 0;
            
            for (int i = 0; i < asientos.size(); i++) {
                if (asientos.get(i)) {
                    System.out.print((i + 1) + " ");
                    disponibles++;
                } else {
                    System.out.print("X ");
                }
            }
            System.out.printf("%nTotal disponibles: %d/%d%n", disponibles, asientos.size());
        }
    }
    
    private static void mostrarBoletasVendidas() {
        System.out.println("\n=== HISTORIAL DE BOLETAS VENDIDAS ===");
        if (entradasVendidas.isEmpty()) {
            System.out.println("No hay boletas vendidas aún.");
            return;
        }
        
        double totalVentas = 0;
        for (Entrada entrada : entradasVendidas) {
            imprimirBoleta(entrada);
            totalVentas += entrada.getPrecioFinal();
            System.out.println(); // Espacio entre boletas
        }
        
        System.out.printf("TOTAL VENTAS: $%,.0f%n", totalVentas);
    }
}

class Entrada {
    private String nombre;
    private int edad;
    private String categoria;
    private String tipoAsiento;
    private int numeroAsiento;
    private double precioBase;
    private double descuento;
    private double precioFinal;
    
    public Entrada(String nombre, int edad, String categoria, String tipoAsiento, 
                  int numeroAsiento, double precioBase, double descuento, double precioFinal) {
        this.nombre = nombre;
        this.edad = edad;
        this.categoria = categoria;
        this.tipoAsiento = tipoAsiento;
        this.numeroAsiento = numeroAsiento;
        this.precioBase = precioBase;
        this.descuento = descuento;
        this.precioFinal = precioFinal;
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getCategoria() { return categoria; }
    public String getTipoAsiento() { return tipoAsiento; }
    public int getNumeroAsiento() { return numeroAsiento; }
    public double getPrecioBase() { return precioBase; }
    public double getDescuento() { return descuento; }
    public double getPrecioFinal() { return precioFinal; }
}