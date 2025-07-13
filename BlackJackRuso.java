package BlackJack;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class BlackJackRuso {

	public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
	    int banco;
	    
	    System.out.println("¡Bienvenido al juego de BlackJack!");
	    banco = pedirFichas(scanner); // Начальный банк

	    boolean seguirJugando = true; // Флаг, указывающий, хочет ли игрок продолжить игру

	    while (seguirJugando) {
	        System.out.println("\nTu banco actual: $" + banco);
	        
	        int apuesta = pedirApuesta(scanner, banco); // Ставка на текущий раунд
	        
	        ArrayList<String> mazo = crearMazo(); // Создаем колоду
		    mezclarMazo(mazo); // Мешаем колоду

		    String resultado = jugarUnaPartida(mazo, scanner); // Возвращаем результат раунда
		   
	        // Обновляем банк в зависимости от результата (расчет выигрыша)
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

	        // Проверка на пустой банк
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
	        // Спрашиваем, хочет ли игрок продолжить
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
	
	// Метод запрашивает у пользователя сумму для обмена на фишки (начальный банк)
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
    
    // Метод запрашивает у пользователя ставку на первый раунд из банка
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
	
 // Функция создания колоды
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
    
    // Функция перемешивания колоды
    public static void mezclarMazo(ArrayList<String> mazo) {
        Collections.shuffle(mazo);
    }
    
    // Метод - вытаскивание карты из колоды
 	public static String sacarCarta(ArrayList<String> mazo) {
 	    return mazo.remove(0);
 	}
 	
     // Функция показа карт (для проверки)
     public static void mostrarMazo(ArrayList<String> mazo) {
         for (String carta : mazo) {
             System.out.println(carta);
         }
     }
     
     // Функция подсчета очков карт
     public static int contarPuntos(ArrayList<String> mano) {
         int suma = 0;
         int cantidadAs = 0;

         for (String carta : mano) {
             // Извлекаем значение карты, убирая масть (♠♥♦♣)
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
                     cantidadAs++; // Считаем количество тузов, решим позже какой у них номинал 1 или 11
                     break;
             }
         }
         // Обработка тузов: сначала как 11, но если перебор — как 1
         for (int i = 0; i < cantidadAs; i++) {
             if (suma + 11 <= 21) {
                 suma += 11;
             } else {
                 suma += 1;
             }
         }
         return suma;
         }
     
	// Основной метод, реализующий один раунд игры в Blackjack
	public static String jugarUnaPartida(ArrayList<String> mazo, Scanner scanner) {
	    // Рука игрока
	    ArrayList<String> manoJugador = new ArrayList<>();
	    manoJugador.add(sacarCarta(mazo));
	    manoJugador.add(sacarCarta(mazo));

	    // Рука дилера
	    ArrayList<String> manoCrupier = new ArrayList<>();
	    manoCrupier.add(sacarCarta(mazo));
	    manoCrupier.add(sacarCarta(mazo));

	    // Подсчёт очков
	    int puntosJugador = contarPuntos(manoJugador);
	    int puntosCrupier = contarPuntos(manoCrupier);
	    
	    // Показать результат
	    System.out.println("Tus cartas: " + manoJugador + " → Total: " + puntosJugador);
	    System.out.println("Cartas del crupier: " + manoCrupier + " → Total: " + puntosCrupier);
	    
	    // Проверка на BlackJack у дилера и игрока
	    if (puntosJugador == 21 && puntosCrupier == 21) {
	        System.out.println("Ambos tienen Blackjack. Empate — la apuesta se devuelve");
	        return "empate";
	    } else if (puntosCrupier == 21) {
	        System.out.println("El crupier tiene Blackjack.");
	        return "derrota";
	    } else if (puntosJugador == 21) {
	        System.out.println("¡Blackjack!");
	        return "blackjack"; // Идём сразу к ходу дилера (без запроса на карту)
	    }
	    // Только если не 21 — спрашиваем про дополнительную карту
	    if (puntosJugador != 21) {
	        String respuesta;
	        
	        do {
	        	System.out.print("¿Querés otra carta? (s/n): ");
	        	respuesta = scanner.nextLine().toLowerCase();
	        	esperar(1000); // Задержка на 1 секунду
	        	while (!respuesta.equals("s") && !respuesta.equals("n")) {
	        		System.out.print("Entrada inválida. Por favor, escribí 's' o 'n': ");
	        		respuesta = scanner.nextLine().toLowerCase();
	        	}
	     
	        	if (respuesta.equals("s")) {
	        		manoJugador.add(sacarCarta(mazo));
	        		puntosJugador = contarPuntos(manoJugador);
	        		System.out.println("Tus cartas: " + manoJugador + " → Total: " + puntosJugador);
	        		
	        		// Проверка на 21, чтобы не запрашивать больше карту
	        		if (puntosJugador == 21) {
	        	        System.out.println("Tienes 21. ¡Turno del crupier!");
	        	        break; // Прекращаем спрашивать карту
	        	    }
	        		
	        		// Проверка на перебор
	        		if (puntosJugador > 21) {
	        			System.out.println("Te pasaste de 21.");
	        			return "derrota"; // Завершаем игру, т.к. игрок проиграл
	        		}
	        	}
	        	
	        if (respuesta.equals("n")) {
	            System.out.println("Te plantaste con " + puntosJugador + " puntos.");
	        }
	        
	        } while (respuesta.equals("s"));

	        // Ход дилера
	        System.out.println("\nTurno del crupier...");
	        puntosCrupier = contarPuntos(manoCrupier);
	        System.out.println("Cartas del crupier: " + manoCrupier + " → Total: " + puntosCrupier);

	        // Дилер берёт карты, пока не наберёт 17 и больше
	        while (puntosCrupier < 17) {
	        	System.out.println("El crupier toma una carta...");
	        	esperar(1000); // Задержка на 1 секунду
	        	manoCrupier.add(sacarCarta(mazo));
	        	puntosCrupier = contarPuntos(manoCrupier);
	        	System.out.println("Cartas del crupier: " + manoCrupier + " → Total: " + puntosCrupier);
	        }
	        
	        esperar(1000); // Задержка на 1 секунду
	        if (puntosCrupier > 21) {
	            System.out.println("El crupier se pasó con " + puntosCrupier + " puntos.");
	        } else if (puntosCrupier == 21) {
	            System.out.println("El crupier tiene 21.");
	        } else if (puntosCrupier >= 17) {
	            System.out.println("El crupier decide plantarse con " + puntosCrupier + " puntos.");
	        }
	        
	        // Итог игры
	        System.out.println("\nRESULTADO FINAL");
	        System.out.println("Tus puntos: " + puntosJugador);
	        System.out.println("Puntos del crupier: " + puntosCrupier);

	        // Сравнение очков
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
    
    // Метод задержки времени
    public static void esperar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // корректно восстанавливаем флаг прерывания
        }
    }
}