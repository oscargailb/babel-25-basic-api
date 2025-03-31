package com.helloworld.babel.restaurant.model;

import com.helloworld.babel.restaurant.daos.model.Restaurante;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Modelo que representa un local.")
public class Local {

	@NotNull(message = "El CIF del local no puede ser nulo.")
	@Size(min = 9, max = 9, message = "El CIF debe tener 9 caracteres.")
	@Schema(description = "CIF único del local", required = true, example = "B12345678")
	private String cif;

	@NotNull(message = "El nombre del local no puede ser nulo.")
	@Size(min = 3, max = 100, message = "El nombre del local debe tener entre 3 y 100 caracteres.")
	@Schema(description = "Nombre del local", example = "Restaurante El Buen Sabor")
	private String nombre;

	@NotNull(message = "La dirección del local no puede ser nula.")
	@Size(min = 5, max = 255, message = "La dirección del local debe tener entre 5 y 255 caracteres.")
	@Schema(description = "Dirección del local", example = "Calle Falsa 123")
	private String direccion;

	@NotNull(message = "El teléfono del local no puede ser nulo.")
	@Size(min = 9, max = 15, message = "El teléfono debe tener entre 9 y 15 caracteres.")
	@Schema(description = "Teléfono del local", example = "+34 912 345 678")
	private String telefono;

	@Schema(description = "Lista de platos del local")
	private List<Plato> carta = new ArrayList<>();

	public Local(String cif, String nombre, String direccion, String telefono) {
		this.cif = cif;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
	}

	public String getCif() {
		return cif;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public List<Plato> getCarta() {
		return carta;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void addPlato(Plato plato) {
		carta.add(plato);
	}

	public static Local fromRestaurante(Restaurante restaurante) {
		return new Local(
				restaurante.cif(),
				restaurante.nombre(),
				restaurante.direccion(),
				restaurante.telefono());
	}

	public Restaurante toRestaurante() {
		return new Restaurante(cif, nombre, direccion, telefono);
	}
}

