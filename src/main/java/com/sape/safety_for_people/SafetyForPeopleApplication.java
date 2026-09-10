package com.sape.safety_for_people;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
public class SafetyForPeopleApplication {

	public static void main(String[] args) {
		cargarEnv();
		SpringApplication.run(SafetyForPeopleApplication.class, args);
	}

	private static void cargarEnv() {
		try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
			String linea;
			while ((linea = reader.readLine()) != null) {
				linea = linea.trim();
				if (!linea.isEmpty() && !linea.startsWith("#") && linea.contains("=")) {
					String[] partes = linea.split("=", 2);
					String clave = partes[0].trim();
					String valor = partes[1].trim();
					System.setProperty(clave, valor);
				}
			}
		} catch (IOException e) {
			System.out.println("No se encontró el archivo .env o no se pudo cargar: " + e.getMessage());
		}
	}
}