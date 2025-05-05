import java.util.*;

public class TeatroMoro {
    // Arreglos
    static String[] ventas = new String[100];
    static String[] asientos = new String[100];
    static String[] clientes = new String[100];

    // Listas
    static List<String> descuentos = new ArrayList<>();
    static List<Reserva> reservas = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);
    static int ventaIndex = 0, asientoIndex = 0, clienteIndex = 0;

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Menú Teatro Moro ---");
            System.out.println("1. Vender entrada");
            System.out.println("2. Mostrar ventas");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt(); scanner.nextLine();

            switch (opcion) {
                case 1 -> venderEntrada();
                case 2 -> mostrarVentas();
                case 3 -> salir = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    static void venderEntrada() {
        System.out.print("Ingrese nombre del cliente: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese edad del cliente: ");
        int edad = scanner.nextInt(); scanner.nextLine();

        System.out.print("Ingrese número de asiento (1-100): ");
        int numAsiento = scanner.nextInt(); scanner.nextLine();

        if (numAsiento < 1 || numAsiento > 100) {
            System.out.println("Número de asiento inválido. Debe estar entre 1 y 100.");
            return;
        }

        String asiento = String.valueOf(numAsiento);

        if (asientoOcupado(asiento)) {
            System.out.println("El asiento ya está ocupado.");
            return;
        }

        String idVenta = "V" + (ventaIndex + 1);
        String idCliente = "C" + (clienteIndex + 1);
        String idReserva = "R" + (reservas.size() + 1);

        double precioBase = 10000;
        double descuento = calcularDescuento(edad);
        double precioFinal = precioBase - (precioBase * descuento);

        ventas[ventaIndex++] = idVenta + ": $" + precioFinal;
        clientes[clienteIndex++] = idCliente + ": " + nombre;
        asientos[asientoIndex++] = asiento;
        reservas.add(new Reserva(idReserva, idCliente, idVenta, asiento));

        System.out.println("Venta realizada con éxito. Precio final: $" + precioFinal);
    }

    static double calcularDescuento(int edad) {
        if (edad <= 25) {
            descuentos.add("10% estudiante");
            return 0.10;
        } else if (edad >= 60) {
            descuentos.add("15% tercera edad");
            return 0.15;
        }
        return 0.0;
    }

    static boolean asientoOcupado(String asiento) {
        for (int i = 0; i < asientoIndex; i++) {
            if (asiento.equals(asientos[i])) return true;
        }
        return false;
    }

    static void mostrarVentas() {
        System.out.println("\n--- Ventas Realizadas ---");
        for (int i = 0; i < ventaIndex; i++) {
            System.out.println(ventas[i] + " - " + clientes[i] + " - Asiento: " + asientos[i]);
        }
    }

    // Clase interna para reservas
    static class Reserva {
        String idReserva;
        String idCliente;
        String idVenta;
        String asiento;

        Reserva(String idReserva, String idCliente, String idVenta, String asiento) {
            this.idReserva = idReserva;
            this.idCliente = idCliente;
            this.idVenta = idVenta;
            this.asiento = asiento;
        }
    }
}