package pi.banka.web.rest;

import java.io.Console;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.transaction.TransactionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import pi.banka.repository.AnalitikaIzvodaRepository;
import pi.banka.service.AnalitikaIzvodaService;
import pi.banka.service.dto.AnalitikaIzvodaDTO;
import pi.banka.service.dto.KlijentDTO;
import pi.banka.service.dto.ReqAnalitikaIzvodaDto;

/**
 * REST controller for managing {@link pi.banka.domain.AnalitikaIzvoda}.
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/analitike-izvoda")
public class AnalitikaIzvodaController {

    private final Logger log = LoggerFactory.getLogger(AnalitikaIzvodaController.class);

    private final AnalitikaIzvodaService analitikaIzvodaService;
    private final AnalitikaIzvodaRepository analitikaIzvodaRepository;

    public AnalitikaIzvodaController(AnalitikaIzvodaService analitikaIzvodaService, AnalitikaIzvodaRepository analitikaIzvodaRepository) {
        this.analitikaIzvodaService = analitikaIzvodaService;
        this.analitikaIzvodaRepository = analitikaIzvodaRepository;
    }

    @GetMapping()
    public List<AnalitikaIzvodaDTO> getAllAnalitikaIzvoda() {
        log.debug("REST request to get all");
        return analitikaIzvodaService.findAll();
    }

    @GetMapping("/{id}/{datumPocetka}/{datumKraja}")
    public List<AnalitikaIzvodaDTO> getByKlijentAndDate(@PathVariable("id") Long id, @PathVariable("datumPocetka") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datumPocetka,
                                                        @PathVariable("datumKraja") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datumKraja) {
        log.debug("REST request to get all");
        return analitikaIzvodaService.findByKlijentAndDate(id, datumPocetka, datumKraja);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalitikaIzvodaDTO> getAnalitikaIzvoda(@PathVariable Long id) {
        log.debug("REST request to get AnalitikaIzvoda : {}", id);
        Optional<AnalitikaIzvodaDTO> analitikaIzvodaDTO = analitikaIzvodaService.findOne(id);
        if(analitikaIzvodaDTO.isEmpty()) {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity
                .ok()
                .body(analitikaIzvodaDTO.get());
    }

    @PostMapping
    public ResponseEntity<ReqAnalitikaIzvodaDto> createAnalitikaIzvoda(@RequestBody ReqAnalitikaIzvodaDto reqAnalitikaIzvodaDto) throws URISyntaxException{
        if (reqAnalitikaIzvodaDto.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println(reqAnalitikaIzvodaDto);
        ReqAnalitikaIzvodaDto result = analitikaIzvodaService.save(reqAnalitikaIzvodaDto);

        return ResponseEntity
                .created(new URI("/api/analitike-izvoda/" + result.getId()))
                .body(result);
    }
}
