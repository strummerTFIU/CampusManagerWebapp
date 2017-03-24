package es.unizar.campusManager.controller;


import es.unizar.campusManager.model.CampusIncidence;
import es.unizar.campusManager.model.repository.IncidenceRepository;

import es.unizar.campusManager.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api")
public class IncidenceController {

    private final Logger log = Logger.getLogger(IncidenceController.class.getName());

    @Autowired
    private IncidenceRepository incidenceRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Servicio REST que devuelve todas las incidencias de la base de datos
     */
    @GetMapping(value = "/incidencia")
    public @ResponseBody List<CampusIncidence> getAllIncidencias() {
        log.info("Consultando todas las incidencias.");

        return incidenceRepository.findAll();
    }

    /**
     * Servicio REST que devuelve la incidencia con id el especificado en el parámetro.
     */
    @GetMapping(value = "/incidencia/{id:.*}")
    public @ResponseBody CampusIncidence getIncidencia(@PathVariable String id) {
        log.info("Consultando incidencia.");

        return incidenceRepository.findById(Integer.parseInt(id));
    }

    /**
     * Servicio REST que devuelve todas las incidencias del worker especificado en el parámetro.
     */
    @GetMapping(value = "/incidencia/worker/{workerEmail:.*}")
    public @ResponseBody List<CampusIncidence> getWorkerIncidencias(@PathVariable String workerEmail) {
        log.info("Consultando las incidencias del trabajador " + workerEmail +".");
        if (userRepository.findByEmail(workerEmail) == null) {
            log.info("Error, no existe el trabajador " + workerEmail +".");
            return null;
        }
        log.info("Devueltas las incidencias del trabajador " + workerEmail +".");
        return incidenceRepository.findByWorkerEmail(workerEmail);
    }

    /**
     * Servicio REST que devuelve todas las incidencias con estado especificado en el parámetro.
     */
    @GetMapping(value = "/incidencia/estado/{status:.*}")
    public @ResponseBody List<CampusIncidence> getStatusIncidencias(@PathVariable String status) {
        log.info("Consultando las incidencias con estado " + status +".");

        return incidenceRepository.findByStatus(status);
    }

    /**
     * Añade una nueva incidencia con estado "UNASSIGNED", workerEmail=null y la fecha actual
     * a la base de datos.
     */
    @PostMapping(value = "/incidencia")
    public ResponseEntity<?> addIncidencia(@RequestBody CampusIncidence incidence) {
        log.info("Añadiendo nueva incidencia");

        CampusIncidence campusIncidence = new CampusIncidence(incidence.getName(), incidence.getDescription(),
                incidence.getPlace());
        incidenceRepository.save(campusIncidence);

        log.info("Incidencia \"" + campusIncidence.getName() + "\" en \"" + campusIncidence.getPlace() +
                "\" añadida correctamente");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}