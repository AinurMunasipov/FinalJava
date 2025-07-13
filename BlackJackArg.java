package BlackJack;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class BlackJackArg {

	public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
	    int banco;
	    
	    System.out.println("¡Bienvenido al juego de BlackJack!");
	    banco = pedirFichas(scanner); // Banco inicial

	    boolean seguirJugando = true; // Bandera que indica si el jugador quiere seguir jugando

	    while (seguirJugando) {
	        System.out.println("\nTu banco actual: $" + banco);
	        
	        int apuesta = pedirApuesta(scanner, banco); // Apuesta para la ronda actual
	        
	        ArrayList<String> mazo = crearMazo(); // Crear el mazo
		    mezclarMazo(mazo); // Mezclar el mazo

		    String resultado = jugarUnaPartida(mazo, scanner); // Obtener el resultado de la ronda
		   
	        // Actualizar banco según el resultado (cálculo de ganancia)
	        if (resultado.equals("blackjack")) {
	            double ganancia = apuesta * 3 / 2;
	            banco += ganancia;
	            System.out.println("¡Ganaste $" + ganancia + "! Tu banco ahora es: $" + banco);
	        } else if (resultado.equals("victoria")) {
	            banco += apuesta;
	            System.out.println("¡Ganaste $" + apuesta + "! Tu banco ahora es: $" + banco);
	        } else if (resultado.equals("derrota")) {
	            banco -= apuesta;
	            System.out.println("Perdiste $" + apuesta + ". Tu banco ahora es: $" + banco);
	        } else {
	            System.out.println("Empate. Tu banco queda igual: $" + banco);
	        }

	        // Verificación de banco vacío
	        if (banco <= 0) {
	            String respuesta;
	            do {
	            	System.out.print("Te quedaste sin dinero. ¿Querés recargar tu banco? (s/n): ");
	            	respuesta = scanner.nextLine().toLowerCase();
	            	if (!respuesta.equals("s") && !respuesta.equals("n")) {
	                    System.out.println("Ingresá 's' o 'n'.");
	            	}
	            } while (!respuesta.equals("s") && !respuesta.equals("n"));
	            
	            if (respuesta.equals("s")) {
	                banco = pedirFichas(scanner);
	                continue;
	            } else {
	                break;
	            }
	        }
	        // Preguntar si el jugador quiere seguir jugando
	        String respuesta;
	        do {
	            System.out.print("¿Querés jugar otra vez? (s/n): ");
	            respuesta = scanner.nextLine().toLowerCase();
	        } while (!respuesta.equals("s") && !respuesta.equals("n"));
	        seguirJugando = respuesta.equals("s");
	    }

	    System.out.println("Tu banco final fue: $" + banco);
	    System.out.println("Gracias por jugar. ¡Hasta la próxima!");
	    scanner.close();
		}
	
	// Método para pedir al usuario la cantidad de fichas (banco inicial)
    public static int pedirFichas(Scanner scanner) {
        int fichas = 0;
        do {
            System.out.print("Ingresa la cantidad de fichas (от $1 до $1000): ");
            try {
                fichas = Integer.parseInt(scanner.nextLine());
                if (fichas < 1 || fichas > 1000) {
                    System.out.println("La cantidad debe estar entre $1 y $1000");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero valido");
            }
        } while (fichas < 1 || fichas > 1000);
        return fichas;
    }
    
    // Método para pedir al usuario la apuesta desde su banco
    public static int pedirApuesta(Scanner scanner, int banco) {
        int apuesta = 0;
        do {
            System.out.print("Ingresa tu apuesta para esta ronda (hasta $" + banco + "): ");
            try {
                apuesta = Integer.parseInt(scanner.nextLine());
                if (apuesta < 1 || apuesta > banco) {
                    System.out.println("La apuesta debe estar entre $1 y $" + banco);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero valido");
            }
        } while (apuesta < 1 || apuesta > banco);
        return apuesta;
    }
	
    // Crear un nuevo mazo de cartas
    public static ArrayList<String> crearMazo() {
        String[] palos = {"♠", "♥", "♦", "♣"};
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        ArrayList<String> mazo = new ArrayList<>();

        for (String palo : palos) {
            for (String valor : valores) {
                mazo.add(valor + palo);
            }
        }
        return mazo;
    }
    
    // Mezclar el mazo de cartas
    public static void mezclarMazo(ArrayList<String> mazo) {
        Collections.shuffle(mazo);
    }
    
    // Sacar una carta del mazo
 	public static String sacarCarta(ArrayList<String> mazo) {
 	    return mazo.remove(0);
 	}
 	
     // Mostrar el contenido del mazo (para pruebas)
     public static void mostrarMazo(ArrayList<String> mazo) {
         for (String carta : mazo) {
             System.out.println(carta);
         }
     }
     
     // Calcular los puntos totales de una mano
     public static int contarPuntos(ArrayList<String> mano) {
         int suma = 0;
         int cantidadAs = 0;

         for (String carta : mano) {
        	 // Extraer el valor de la carta (sin el palo ♠♥♦♣)
             String valor = carta.replaceAll("[♠♥♦♣]", "");

             switch (valor) {
                 case "2": suma += 2; break;
                 case "3": suma += 3; break;
                 case "4": suma += 4; break;
                 case "5": suma += 5; break;
                 case "6": suma += 6; break;
                 case "7": suma += 7; break;
                 case "8": suma += 8; break;
                 case "9": suma += 9; break;
                 case "10": suma += 10; break;
                 case "J": case "Q": case "K":
                     suma += 10; break;
                 case "A":
                     cantidadAs++; // // Contar ases; después se decide si valen 1 o 11
                     break;
             }
         }
         // Evaluar ases: primero como 11, pero si se pasa, contar como 1
         for (int i = 0; i < cantidadAs; i++) {
             if (suma + 11 <= 21) {
                 suma += 11;
             } else {
                 suma += 1;
             }
         }
         return suma;
         }
     
	// Método principal que ejecuta una ronda de BlackJack
	public static String jugarUnaPartida(ArrayList<String> mazo, Scanner scanner) {
	    
		// Mano del jugador
	    ArrayList<String> manoJugador = new ArrayList<>();
	    manoJugador.add(sacarCarta(mazo));
	    manoJugador.add(sacarCarta(mazo));

	    // Mano del crupier
	    ArrayList<String> manoCrupier = new ArrayList<>();
	    manoCrupier.add(sacarCarta(mazo));
	    manoCrupier.add(sacarCarta(mazo));

	    // Calcular puntos
	    int puntosJugador = contarPuntos(manoJugador);
	    int puntosCrupier = contarPuntos(manoCrupier);
	    
	    // Mostrar resultado inicial
	    System.out.println("Tus cartas: " + manoJugador + " → Total: " + puntosJugador);
	    System.out.println("Cartas del crupier: " + manoCrupier + " → Total: " + puntosCrupier);
	    
	    // Verificar si hay BlackJack
	    if (puntosJugador == 21 && puntosCrupier == 21) {
	        System.out.println("Ambos tienen Blackjack. Empate — la apuesta se devuelve");
	        return "empate";
	    } else if (puntosCrupier == 21) {
	        System.out.println("El crupier tiene Blackjack.");
	        return "derrota";
	    } else if (puntosJugador == 21) {
	        System.out.println("¡Blackjack!");
	        return "blackjack"; // Pasamos directo al crupier sin pedir más cartas
	    }
	    // Si no hay 21, preguntar si quiere otra carta
	    if (puntosJugador != 21) {
	        String respuesta;
	        
	        do {
	        	System.out.print("¿Querés otra carta? (s/n): ");
	        	respuesta = scanner.nextLine().toLowerCase();
	        	esperar(1000); // Pausa de 1 segundo
	        	while (!respuesta.equals("s") && !respuesta.equals("n")) {
	        		System.out.print("Entrada inválida. Por favor, escribí 's' o 'n': ");
	        		respuesta = scanner.nextLine().toLowerCase();
	        	}
	     
	        	if (respuesta.equals("s")) {
	        		manoJugador.add(sacarCarta(mazo));
	        		puntosJugador = contarPuntos(manoJugador);
	        		System.out.println("Tus cartas: " + manoJugador + " → Total: " + puntosJugador);
	        		
	        		// Si llegó a 21, no seguir pidiendo
	        		if (puntosJugador == 21) {
	        	        System.out.println("Tienes 21. ¡Turno del crupier!");
	        	        break; // Прекращаем спрашивать карту
	        	    }
	        		
	        		// Si se pasó, termina la ronda
	        		if (puntosJugador > 21) {
	        			System.out.println("Te pasaste de 21.");
	        			return "derrota";
	        		}
	        	}
	        	
	        if (respuesta.equals("n")) {
	            System.out.println("Te plantaste con " + puntosJugador + " puntos.");
	        }
	        
	        } while (respuesta.equals("s"));

	        // Turno del crupier
	        System.out.println("\nTurno del crupier...");
	        puntosCrupier = contarPuntos(manoCrupier);
	        System.out.println("Cartas del crupier: " + manoCrupier + " → Total: " + puntosCrupier);

	        // El crupier saca cartas hasta llegar a 17 o más
	        while (puntosCrupier < 17) {
	        	System.out.println("El crupier toma una carta...");
	        	esperar(1000); // Pausa de 1 segundo
	        	manoCrupier.add(sacarCarta(mazo));
	        	puntosCrupier = contarPuntos(manoCrupier);
	        	System.out.println("Cartas del crupier: " + manoCrupier + " → Total: " + puntosCrupier);
	        }
	        
	        esperar(1000); // Pausa de 1 segundo
	        if (puntosCrupier > 21) {
	            System.out.println("El crupier se pasó con " + puntosCrupier + " puntos.");
	        } else if (puntosCrupier == 21) {
	            System.out.println("El crupier tiene 21.");
	        } else if (puntosCrupier >= 17) {
	            System.out.println("El crupier decide plantarse con " + puntosCrupier + " puntos.");
	        }
	        
	        // Resultado final de la ronda
	        System.out.println("\nRESULTADO FINAL");
	        System.out.println("Tus puntos: " + puntosJugador);
	        System.out.println("Puntos del crupier: " + puntosCrupier);

	        // Comparación de puntos
	        if (puntosCrupier > 21) {
	        	System.out.println("¡El crupier se pasó! ¡Ganaste!");
	        	return "victoria";
	        } else if (puntosJugador > puntosCrupier) {
	        	return "victoria";
	        } else if (puntosJugador < puntosCrupier) {
	        	return "derrota";
	        } else {
	        	return "empate";
	        }
	    }
	return "error"; 
	}
    
    // Método para pausar el juego (usado para simular animación o espera)
    public static void esperar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurar correctamente el estado de interrupción
        }
    }
}