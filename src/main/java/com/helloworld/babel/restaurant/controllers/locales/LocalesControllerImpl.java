package com.helloworld.babel.restaurant.controllers.locales;

import com.helloworld.babel.restaurant.servicios.exceptions.NotFoundException;
import com.helloworld.babel.restaurant.model.Local;
import com.helloworld.babel.restaurant.model.Plato;
import com.helloworld.babel.restaurant.servicios.locales.LocalesService;
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
@RequestMapping("restaurante/locales")
@Tag(name = "Locales", description = "Operaciones relacionadas con los locales del restaurante.")
public class LocalesControllerImpl implements LocalesController {

	private final LocalesService localesService;

	public LocalesControllerImpl(LocalesService localesService) {
		this.localesService = localesService;
	}


	@Override
	@GetMapping("")
	@Operation(summary = "Obtener lista de locales", description = "Este endpoint devuelve una lista con todos los locales disponibles.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de locales obtenida exitosamente"),
			@ApiResponse(responseCode = "500", description = "Error en el servidor")
	})
	public List<Local> getLocales() {
		return localesService.getLocales();
	}

	@Override
	@GetMapping("/{cif}")
	@Operation(summary = "Obtener local por CIF", description = "Este endpoint devuelve los detalles de un local en base a su CIF.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Local encontrado exitosamente"),
			@ApiResponse(responseCode = "404", description = "Local no encontrado")
	})
	public Local getLocalByCif( @Parameter(description = "CIF del local que se desea consultar")@PathVariable String cif) {
		Optional<Local> local = localesService.getLocalByCif(cif);
		if (local.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Local no encontrado");
		} else {
			return local.get();
		}
	}

	@Override
	@PutMapping("/{cif}")
	@Operation(summary = "Crear o actualizar un local", description = "Este endpoint crea o actualiza un local en base a su CIF.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Local creado exitosamente"),
			@ApiResponse(responseCode = "204", description = "Local actualizado exitosamente"),
			@ApiResponse(responseCode = "400", description = "Petición incorrecta")
	})
	public ResponseEntity<Void> createOrUpdateLocal(@Parameter(description = "CIF del local a crear o actualizar")@PathVariable String cif, @RequestBody @Parameter(description = "Datos del local a crear o actualizar") Local local) {
		local.setCif(cif);
		Optional<Local> updatedLocal = localesService.updateLocal(local);
		if (updatedLocal.isEmpty()) {
			localesService.createLocal(local);
			return ResponseEntity.
					created(URI.create("/restaurante/locales/" + local.getCif())).
					build();
		} else {
			return ResponseEntity.
					noContent().
					header("Content-Location", "/restaurante/locales/" + local.getCif()).
					build();
		}
	}

	@Override
	@DeleteMapping("/{cif}")
	@Operation(summary = "Eliminar un local", description = "Este endpoint elimina un local en base a su CIF.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Local eliminado exitosamente"),
			@ApiResponse(responseCode = "404", description = "Local no encontrado")
	})
	public ResponseEntity<Void> deleteLocal(@Parameter(description = "CIF del local a eliminar")@PathVariable String cif) {
		localesService.deleteLocal(cif);
		return ResponseEntity.noContent().build();
	}

	@Override
	@GetMapping("/{cif}/platos")
	@Operation(summary = "Obtener platos de un local", description = "Este endpoint devuelve los platos de un local especificado por su CIF.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Platos obtenidos exitosamente"),
			@ApiResponse(responseCode = "404", description = "Local no encontrado")
	})
	public List<Plato> getPlatos(@Parameter(description = "CIF del local del que se desean obtener los platos") @PathVariable String cif) {
		try {
			return localesService.getPlatosByLocal(cif);
		}catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@Override
	@PostMapping("/{cif}/platos")
	@Operation(summary = "Agregar un plato a un local", description = "Este endpoint permite agregar un plato a un local específico usando su CIF.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Plato agregado exitosamente"),
			@ApiResponse(responseCode = "204", description = "Plato ya existe o no se agregó"),
			@ApiResponse(responseCode = "404", description = "Local no encontrado")
	})
	public ResponseEntity<Void> addPlato(@Parameter(description = "CIF del local al que se desea agregar el plato") @PathVariable String cif, @RequestBody @Parameter(description = "ID del plato a agregar")  int plato) {
		try {
			if (localesService.addPlato(cif, plato)>0) {
				return ResponseEntity
						.created(URI.create("/restaurante/locales/" + cif + "/platos/" + plato))
						.build();
			}
			else {
				return ResponseEntity
						.noContent()
						.build();
			}
		}catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}

	}

	@Override
	@DeleteMapping("/{cif}/platos/{plato}")
	@Operation(summary = "Eliminar un plato de un local", description = "Este endpoint elimina un plato de un local especificado por su CIF.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Plato eliminado exitosamente"),
			@ApiResponse(responseCode = "404", description = "Local o plato no encontrado")
	})
	public ResponseEntity<Void> removePlato(@Parameter(description = "CIF del local del que se desea eliminar el plato") @PathVariable String cif, @Parameter(description = "ID del plato a eliminar") @PathVariable int plato) {
		try {
			localesService.removePlato(cif, plato);
		}catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return ResponseEntity.noContent().build();
	}
}
