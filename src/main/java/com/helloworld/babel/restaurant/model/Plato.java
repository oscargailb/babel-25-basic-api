package com.helloworld.babel.restaurant.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Modelo que representa un plato en el menú del restaurante.")
public class Plato {

	@Schema(description = "Categoría del plato. Representa si el plato es un primer plato, segundo plato o postre.",
			example = "PRIMER_PLATO", required = true)
	public enum Categoria {
		@Schema(description = "Entrante o primer plato del menú.")
		PRIMER_PLATO("Entrante"),

		@Schema(description = "Plato principal del menú.")
		SEGUNDO_PLATO("Plato principal"),

		@Schema(description = "Postre del menú.")
		POSTRE("Postre");

		private String descripcion;

		Categoria(String descripcion) {
			this.descripcion = descripcion;
		}

		@JsonValue
		public String getDescripcion() {
			return descripcion;
		}

		@JsonCreator
		public static Categoria fromDescripcion(String descripcion) {
			return switch (descripcion) {
				case "Entrante" -> PRIMER_PLATO;
				case "Plato principal" -> SEGUNDO_PLATO;
				case "Postre" -> POSTRE;
				default -> PRIMER_PLATO;
			};
		}
	}

	@NotNull(message = "El ID del plato no puede ser nulo.")
	@Min(value = 1, message = "El ID debe ser un valor mayor o igual a 1.")
	@Schema(description = "ID único del plato", required = true, example = "1")
	private Integer id;

	@NotNull(message = "El nombre del plato no puede ser nulo.")
	@Size(min = 2, max = 100, message = "El nombre del plato debe tener entre 2 y 100 caracteres.")
	@Schema(description = "Nombre del plato en el menú.", example = "Ensalada César")
	private String nombre;

	@NotNull(message = "El precio del plato no puede ser nulo.")
	@Min(value = 0, message = "El precio debe ser un valor positivo.")
	@Schema(description = "Precio del plato en euros.", example = "12.50")
	private double precio;

	@NotNull(message = "La categoría del plato no puede ser nula.")
	@Schema(description = "Categoría del plato, indicando si es primer plato, segundo plato o postre.",
			example = "PRIMER_PLATO")
	private Categoria categoria;

	public Plato(Integer id, String nombre, double precio, Categoria categoria) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.categoria = categoria;
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public static Plato fromPlatoDAO(com.helloworld.babel.restaurant.daos.model.Plato plato) {
		Categoria categoria = switch (plato.categoria()) {
			case 1 -> Categoria.PRIMER_PLATO;
			case 2 -> Categoria.SEGUNDO_PLATO;
			case 3 -> Categoria.POSTRE;
			default -> Categoria.PRIMER_PLATO;
		};

		return new Plato(
				plato.id(),
				plato.nombre(),
				plato.precio(),
				categoria
		);
	}

	public com.helloworld.babel.restaurant.daos.model.Plato toPlatoDAO() {
		int cat = switch (this.getCategoria()) {
			case PRIMER_PLATO -> 1;
			case SEGUNDO_PLATO -> 2;
			case POSTRE -> 3;
		};

		return new com.helloworld.babel.restaurant.daos.model.Plato(
				this.getId(),
				this.getNombre(),
				this.getPrecio(),
				cat
		);
	}
}

