package com.helloworld.babel.restaurant.controllers.platos;

import com.helloworld.babel.restaurant.model.Plato;
import com.helloworld.babel.restaurant.servicios.platos.PlatosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("restaurante/platos")
@Tag(name = "Platos", description = "Operaciones relacionadas con los platos del restaurante.")
public class PlatosControllerImpl implements PlatosController {

	private final PlatosService platosService;

	public PlatosControllerImpl(PlatosService platosService) {
		this.platosService = platosService;
	}


	@Override
	@GetMapping("")
	@Operation(summary = "Listado de platos", description = "Devuelve una lista con todos los platos disponibles en el restaurante.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Listado de platos encontrado"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	public List<Plato> getPlatos() {
		List<Plato> platos = platosService.getPlatos();
		return platos;
	}


	@Override
	@GetMapping("/{id}")
	@Operation(summary = "Obtener un plato por ID", description = "Devuelve los detalles de un plato específico utilizando su ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Plato encontrado"),
			@ApiResponse(responseCode = "404", description = "Plato no encontrado"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	public Plato getPlatosById(@Parameter(description = "ID del plato a obtener") @PathVariable String id) {
		Optional<Plato> plato = platosService.getPlatosById(Integer.parseInt(id));
		if (plato.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado");
		} else {
			return plato.get();
		}
	}

	@Override
	@PutMapping("/{id}")
	@Operation(summary = "Actualizar un plato", description = "Permite actualizar un plato existente utilizando su ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Plato actualizado correctamente"),
			@ApiResponse(responseCode = "404", description = "Plato a actualizar no encontrado"),
			@ApiResponse(responseCode = "400", description = "Petición mal formada"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	public ResponseEntity<Void> updatePlato(@Parameter(description = "ID del plato a actualizar") @PathVariable int id, @Parameter(description = "Nuevo objeto Plato con la información actualizada") @RequestBody Plato plato) {
		plato.setId(id);
		Optional<Plato> updatedPlato = platosService.updatePlato(plato);
		if (updatedPlato.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato a actualizar no encontrado");
		} else {
			return ResponseEntity.
					noContent().
					header("Content-Location", "/restaurante/platos/" + plato.getId()).
					build();
		}
	}

	@Override
	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un plato", description = "Permite eliminar un plato utilizando su ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Plato eliminado correctamente"),
			@ApiResponse(responseCode = "404", description = "Plato no encontrado"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	public ResponseEntity<Void> deletePlato(@Parameter(description = "ID del plato a eliminar") @PathVariable int id) {
		platosService.deletePlato(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@PostMapping("")
	@Operation(summary = "Crear un nuevo plato", description = "Permite crear un nuevo plato en el restaurante.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Plato creado correctamente"),
			@ApiResponse(responseCode = "400", description = "Petición mal formada"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	public ResponseEntity<Long> createPlato(@Parameter(description = "Objeto Plato con los detalles del plato a crear") @RequestBody Plato plato) {
		long platoCreadoId = platosService.createPlato(plato);
		return ResponseEntity.
				created(URI.create("/restaurante/platos/" + platoCreadoId)).
				body(platoCreadoId);
	}
}
