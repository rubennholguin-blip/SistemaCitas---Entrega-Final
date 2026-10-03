import java.io.*;
import java.util.Scanner;

public class Main {
    private static final String RUTA_DB = "db/";

    public static void main(String[] args) {
        validarDirectorio();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- ACCESO ADMINISTRADORES ---");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        if (user.equals("admin") && pass.equals("1234")) {
            mostrarMenu(scanner);
        } else {
            System.out.println("Acceso denegado.");
        }
    }

    private static void validarDirectorio() {
        File directorio = new File(RUTA_DB);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }

    private static void mostrarMenu(Scanner scanner) {
        int opcion = 0;
        do {
            try {
                System.out.println("\n--- SISTEMA DE CITAS ---");
                System.out.println("1. Alta Doctor");
                System.out.println("2. Alta Paciente");
                System.out.println("3. Crear Cita");
                System.out.println("4. Salir");
                System.out.print("Elija opción: ");
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1: registrarDoctor(scanner); break;
                    case 2: registrarPaciente(scanner); break;
                    case 3: registrarCita(scanner); break;
                    case 4: System.out.println("Saliendo..."); break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        } while (opcion != 4);
    }

    private static void registrarDoctor(Scanner scanner) {
        System.out.print("ID Doctor: "); String id = scanner.nextLine();
        System.out.print("Nombre: "); String nombre = scanner.nextLine();
        System.out.print("Especialidad: "); String esp = scanner.nextLine();
        Doctor doc = new Doctor(id, nombre, esp);
        guardarEnCSV("doctores.csv", doc.toCSV());
        System.out.println("Doctor registrado.");
    }

    private static void registrarPaciente(Scanner scanner) {
        System.out.print("ID Paciente: "); String id = scanner.nextLine();
        System.out.print("Nombre: "); String nombre = scanner.nextLine();
        Paciente pac = new Paciente(id, nombre);
        guardarEnCSV("pacientes.csv", pac.toCSV());
        System.out.println("Paciente registrado.");
    }

    private static void registrarCita(Scanner scanner) {
        System.out.print("ID Cita: "); String id = scanner.nextLine();
        System.out.print("Fecha y Hora: "); String fecha = scanner.nextLine();
        System.out.print("Motivo: "); String motivo = scanner.nextLine();
        System.out.print("ID Doctor: "); String idDoc = scanner.nextLine();
        System.out.print("ID Paciente: "); String idPac = scanner.nextLine();

        String datos = id + "," + fecha + "," + motivo + "," + idDoc + "," + idPac;
        guardarEnCSV("citas.csv", datos);
        System.out.println("Cita agendada.");
    }

    private static void guardarEnCSV(String archivo, String datos) {
        try (FileWriter fw = new FileWriter(RUTA_DB + archivo, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(datos);
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }
}

// Clases de apoyo con herencia
abstract class Persona {
    protected String id;
    protected String nombre;
    public Persona(String id, String nombre) { this.id = id; this.nombre = nombre; }
    public abstract String toCSV();
}

class Doctor extends Persona {
    private String especialidad;
    public Doctor(String id, String nombre, String especialidad) {
        super(id, nombre);
        this.especialidad = especialidad;
    }
    @Override
    public String toCSV() { return id + "," + nombre + "," + especialidad; }
}

class Paciente extends Persona {
    public Paciente(String id, String nombre) { super(id, nombre); }
    @Override
    public String toCSV() { return id + "," + nombre; }
}