package pi.banka.service.impl;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pi.banka.domain.AnalitikaIzvoda;
import pi.banka.domain.Banka;
import pi.banka.domain.DnevnoStanje;
import pi.banka.domain.Racun;
import pi.banka.repository.AnalitikaIzvodaRepository;
import pi.banka.service.AnalitikaIzvodaService;
import pi.banka.service.BankaService;
import pi.banka.service.DnevnoStanjeService;
import pi.banka.service.RacunService;
import pi.banka.service.dto.AnalitikaIzvodaDTO;
import pi.banka.service.dto.ReqAnalitikaIzvodaDto;
import pi.banka.service.mapper.AnalitikaIzvodaMapper;
import pi.banka.service.mapper.AnalitikaIzvodaToDto;
import pi.banka.service.mapper.ReqAnalitikaIzvodaMapper;

/**
 * Service Implementation for managing {@link AnalitikaIzvoda}.
 */
@Service
@Transactional
public class AnalitikaIzvodaServiceImpl implements AnalitikaIzvodaService {

    private final Logger log = LoggerFactory.getLogger(AnalitikaIzvodaServiceImpl.class);

    private final AnalitikaIzvodaRepository analitikaIzvodaRepository;

    private final AnalitikaIzvodaMapper analitikaIzvodaMapper;

    private final ReqAnalitikaIzvodaMapper reqAnalitikaIzvodaMapper;

    private final DnevnoStanjeService dnevnoStanjeService;

    private final  RacunService racunService;

    private final BankaService bankaService;

    @Autowired
    private AnalitikaIzvodaToDto toDto;

    public AnalitikaIzvodaServiceImpl(AnalitikaIzvodaRepository analitikaIzvodaRepository, AnalitikaIzvodaMapper analitikaIzvodaMapper,
                                      ReqAnalitikaIzvodaMapper reqAnalitikaIzvodaMapper,DnevnoStanjeService dnevnoStanjeService,
                                      RacunService racunService, BankaService bankaService) {
        this.analitikaIzvodaRepository = analitikaIzvodaRepository;
        this.analitikaIzvodaMapper = analitikaIzvodaMapper;
        this.reqAnalitikaIzvodaMapper = reqAnalitikaIzvodaMapper;
        this.dnevnoStanjeService = dnevnoStanjeService;
        this.racunService = racunService;
        this.bankaService = bankaService;
    }

    @Override
    public AnalitikaIzvodaDTO saves(AnalitikaIzvodaDTO analitikaIzvodaDTO) {
        log.debug("Request to save AnalitikaIzvoda : {}", analitikaIzvodaDTO);
        AnalitikaIzvoda analitikaIzvoda = analitikaIzvodaMapper.toEntity(analitikaIzvodaDTO);
        analitikaIzvoda = analitikaIzvodaRepository.save(analitikaIzvoda);
        return analitikaIzvodaMapper.toDto(analitikaIzvoda);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReqAnalitikaIzvodaDto save(ReqAnalitikaIzvodaDto reqAnalitikaIzvodaDto){
        log.debug("Request to save AnalitikaIzvoda : {}", reqAnalitikaIzvodaDto);
        AnalitikaIzvoda analitikaIzvoda = reqAnalitikaIzvodaMapper.toEntity(reqAnalitikaIzvodaDto);
        analitikaIzvoda.setBrojStavke((int)((Math.random() * 1000)/2.5));
        analitikaIzvoda.setVrstaPlacanja("uplata");

        Racun racunDuznika = racunService.findRacunByBrojRacuna(analitikaIzvoda.getRacunDuznika());
        Racun racunPrimaoca = racunService.findRacunByBrojRacuna(analitikaIzvoda.getRacunPrimaoca());
        if(racunPrimaoca !=null){
            AnalitikaIzvoda analitikaIzvodaPrimaoca = kreirajAnalitikuIzvodaPrimaoca(analitikaIzvoda);

            DnevnoStanje dnevnoStanjeDuznika = dnevnoStanjeService.findByLastDateAndRacun(racunDuznika.getId());
            DnevnoStanje dnevnoStanjePrimaoca = dnevnoStanjeService.findByLastDateAndRacun(racunPrimaoca.getId());

            DnevnoStanje novoDnevnoStanjeDuznika = dnevnoStanjeDuznika(analitikaIzvoda, dnevnoStanjeDuznika, racunDuznika);
            DnevnoStanje novoDnevnoStanjePrimaoca = dnevnoStanjePrimaoca(analitikaIzvodaPrimaoca, dnevnoStanjePrimaoca, racunPrimaoca);


            analitikaIzvoda.setDnevnoStanje(novoDnevnoStanjeDuznika);
            analitikaIzvodaRepository.save(analitikaIzvoda);
            analitikaIzvodaPrimaoca.setDnevnoStanje(novoDnevnoStanjePrimaoca);
            analitikaIzvodaRepository.save(analitikaIzvodaPrimaoca);


            return reqAnalitikaIzvodaMapper.toDto(analitikaIzvoda);

        }else if(analitikaIzvoda.getHitno() || analitikaIzvoda.getIznos() > 300000){

            DnevnoStanje dnevnoStanjeDuznika = dnevnoStanjeService.findByLastDateAndRacun(racunDuznika.getId());
            DnevnoStanje novoDnevnoStanjeDuznika = dnevnoStanjeDuznika(analitikaIzvoda, dnevnoStanjeDuznika, racunDuznika);

            Banka banka = bankaService.findOneById(racunDuznika.getBanka().getId());

            analitikaIzvoda.setDnevnoStanje(novoDnevnoStanjeDuznika);
            analitikaIzvodaRepository.save(analitikaIzvoda);

            rtgs(analitikaIzvoda, banka);

            return reqAnalitikaIzvodaMapper.toDto(analitikaIzvoda);
        }else{
            DnevnoStanje dnevnoStanjeDuznika = dnevnoStanjeService.findByLastDateAndRacun(racunDuznika.getId());
            DnevnoStanje novoDnevnoStanjeDuznika = dnevnoStanjeDuznika(analitikaIzvoda, dnevnoStanjeDuznika, racunDuznika);

            Banka banka = bankaService.findOneById(racunDuznika.getBanka().getId());

            Double rezervisano = novoDnevnoStanjeDuznika.getRezervisano() + analitikaIzvoda.getIznos();
            novoDnevnoStanjeDuznika.setRezervisano(rezervisano);
            analitikaIzvoda.setDnevnoStanje(novoDnevnoStanjeDuznika);
            analitikaIzvodaRepository.save(analitikaIzvoda);
            clearing(analitikaIzvoda,banka);

            return reqAnalitikaIzvodaMapper.toDto(analitikaIzvoda);
        }
    }



    public DnevnoStanje dnevnoStanjeDuznika(AnalitikaIzvoda analitikaIzvoda, DnevnoStanje dnevnoStanjeDuznika, Racun racunDuznika){


        if(dnevnoStanjeDuznika == null) {
            int brojIzvoda = (int) ((Math.random() * 1000)/2.5);
            DnevnoStanje novoDnevnoStanje = new DnevnoStanje();
            novoDnevnoStanje.setNovoStanje(0 - analitikaIzvoda.getIznos());
            novoDnevnoStanje.setBrojIzvoda(brojIzvoda);
            novoDnevnoStanje.setDatumIzvoda(LocalDate.now());
            novoDnevnoStanje.setPrometNaTeret(analitikaIzvoda.getIznos());
            novoDnevnoStanje.setPrethodnoStanje(0d);
            novoDnevnoStanje.setPrometUKorist(0d);
            novoDnevnoStanje.setRezervisano(0d);
            novoDnevnoStanje.setRacunPrivatnihLica(racunDuznika);
            novoDnevnoStanje.getAnalitikaIzvodas().add(analitikaIzvoda);


            return novoDnevnoStanje;
        }
        else if(dnevnoStanjeDuznika.getDatumIzvoda().equals(LocalDate.now())){
            double nStanjeDuznika = dnevnoStanjeDuznika.getNovoStanje() - analitikaIzvoda.getIznos();
            double noviPrometNaTeret = dnevnoStanjeDuznika.getPrometNaTeret() + analitikaIzvoda.getIznos();
            dnevnoStanjeDuznika.getAnalitikaIzvodas().add(analitikaIzvoda);
            dnevnoStanjeDuznika.setPrethodnoStanje(nStanjeDuznika + analitikaIzvoda.getIznos());
            dnevnoStanjeDuznika.setNovoStanje(nStanjeDuznika);
            dnevnoStanjeDuznika.setPrometNaTeret(noviPrometNaTeret);


            return dnevnoStanjeDuznika;
        }else{
            int brojIzvoda = (int) ((Math.random() * 1000)/2.5);
            double nStanjeDuznika = dnevnoStanjeDuznika.getNovoStanje() - analitikaIzvoda.getIznos();
//            double noviPrometNaTeret = dnevnoStanjeDuznika.getPrometNaTeret() + analitikaIzvoda.getIznos();
            DnevnoStanje novoDnevnoStanje = new DnevnoStanje();
            novoDnevnoStanje.setNovoStanje(nStanjeDuznika);
            novoDnevnoStanje.setBrojIzvoda(brojIzvoda);
            novoDnevnoStanje.setDatumIzvoda(LocalDate.now());
            novoDnevnoStanje.setPrometNaTeret(analitikaIzvoda.getIznos());
            novoDnevnoStanje.setPrethodnoStanje(nStanjeDuznika + analitikaIzvoda.getIznos());
            novoDnevnoStanje.setPrometUKorist(0d);
            novoDnevnoStanje.setRezervisano(0d);
            novoDnevnoStanje.setRacunPrivatnihLica(racunDuznika);
            novoDnevnoStanje.getAnalitikaIzvodas().add(analitikaIzvoda);


            return novoDnevnoStanje;
        }
    }

    public DnevnoStanje dnevnoStanjePrimaoca(AnalitikaIzvoda analitikaIzvoda, DnevnoStanje dnevnoStanjePrimaoca, Racun racunPrimaoca){



        if(dnevnoStanjePrimaoca == null) {

            int brojIzvoda = (int) ((Math.random() * 1000)/2.5);
            DnevnoStanje novoDnevnoStanje = new DnevnoStanje();
            novoDnevnoStanje.setNovoStanje(analitikaIzvoda.getIznos());
            novoDnevnoStanje.setBrojIzvoda(brojIzvoda);
            novoDnevnoStanje.setDatumIzvoda(LocalDate.now());
            novoDnevnoStanje.setPrometNaTeret(0d);
            novoDnevnoStanje.setPrethodnoStanje(0d);
            novoDnevnoStanje.setPrometUKorist(analitikaIzvoda.getIznos());
            novoDnevnoStanje.setRezervisano(0d);
            novoDnevnoStanje.setRacunPrivatnihLica(racunPrimaoca);
            novoDnevnoStanje.getAnalitikaIzvodas().add(analitikaIzvoda);


            return novoDnevnoStanje;
        }
        else if(dnevnoStanjePrimaoca.getDatumIzvoda().equals(LocalDate.now())){
            double nstanjePrimaoca = dnevnoStanjePrimaoca.getNovoStanje() + analitikaIzvoda.getIznos();
            double noviPrometUKorist = dnevnoStanjePrimaoca.getPrometUKorist() + analitikaIzvoda.getIznos();
            dnevnoStanjePrimaoca.getAnalitikaIzvodas().add(analitikaIzvoda);
            dnevnoStanjePrimaoca.setPrethodnoStanje(nstanjePrimaoca - analitikaIzvoda.getIznos());
            dnevnoStanjePrimaoca.setNovoStanje(nstanjePrimaoca);
            dnevnoStanjePrimaoca.setPrometUKorist(noviPrometUKorist);


            return dnevnoStanjePrimaoca;

        }else{
            int brojIzvoda = (int) ((Math.random() * 1000)/2.5);
            double nstanjePrimaoca = dnevnoStanjePrimaoca.getNovoStanje() + analitikaIzvoda.getIznos();
            DnevnoStanje novoDnevnoStanje = new DnevnoStanje();
            novoDnevnoStanje.setNovoStanje(nstanjePrimaoca);
            novoDnevnoStanje.setBrojIzvoda(brojIzvoda);
            novoDnevnoStanje.setDatumIzvoda(LocalDate.now());
            novoDnevnoStanje.setPrometNaTeret(0d);
            novoDnevnoStanje.setPrethodnoStanje(dnevnoStanjePrimaoca.getNovoStanje());
            novoDnevnoStanje.setPrometUKorist(analitikaIzvoda.getIznos());
            novoDnevnoStanje.setRezervisano(0d);
            novoDnevnoStanje.setRacunPrivatnihLica(racunPrimaoca);
            novoDnevnoStanje.getAnalitikaIzvodas().add(analitikaIzvoda);


            return novoDnevnoStanje;
        }
    }

    public AnalitikaIzvoda kreirajAnalitikuIzvodaPrimaoca(AnalitikaIzvoda analitikaIzvoda){

        AnalitikaIzvoda analitikaIzvodaPrimaoca = new AnalitikaIzvoda();
        analitikaIzvodaPrimaoca.setAdresaPrimaoca(analitikaIzvoda.getAdresaPrimaoca());
        analitikaIzvodaPrimaoca.setSvrhaPlacanja(analitikaIzvoda.getSvrhaPlacanja());
        analitikaIzvodaPrimaoca.setBrojStavke(analitikaIzvoda.getBrojStavke());
        analitikaIzvodaPrimaoca.setAdresaDuznika(analitikaIzvoda.getAdresaDuznika());
        analitikaIzvodaPrimaoca.setDuznik(analitikaIzvoda.getDuznik());
        analitikaIzvodaPrimaoca.setPrimalac(analitikaIzvoda.getPrimalac());
        analitikaIzvodaPrimaoca.setHitno(analitikaIzvoda.getHitno());
        analitikaIzvodaPrimaoca.setModel(analitikaIzvoda.getModel());
        analitikaIzvodaPrimaoca.setIznos(analitikaIzvoda.getIznos());
        analitikaIzvodaPrimaoca.setPozivNaBroj(analitikaIzvoda.getPozivNaBroj());
        analitikaIzvodaPrimaoca.setRacunDuznika(analitikaIzvoda.getRacunDuznika());
        analitikaIzvodaPrimaoca.setRacunPrimaoca(analitikaIzvoda.getRacunPrimaoca());
        analitikaIzvodaPrimaoca.setVrstaPlacanja(analitikaIzvoda.getVrstaPlacanja());
        return analitikaIzvodaPrimaoca;
    }


    @Override
    @Transactional(readOnly = true)
    public List<AnalitikaIzvodaDTO> findAll() {
        log.debug("Request to get all");
        return analitikaIzvodaRepository
            .findAll()
            .stream()
            .map(analitikaIzvodaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public  List<AnalitikaIzvodaDTO> findByKlijentAndDate(Long id, LocalDate datumPocetka, LocalDate krajnjiDatum) {
        return toDto.convertList(analitikaIzvodaRepository.findByKlijentAndDate(id, datumPocetka, krajnjiDatum));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnalitikaIzvodaDTO> findOne(Long id) {
        log.debug("Request to get AnalitikaIzvoda : {}", id);
        return analitikaIzvodaRepository.findById(id).map(analitikaIzvodaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnalitikaIzvoda : {}", id);
        analitikaIzvodaRepository.deleteById(id);
    }

    public void rtgs(AnalitikaIzvoda analitikaIzvoda, Banka banka){
        try {
            Path path = Paths.get("src/main/resources/MT103");


            String line = "SWIFT kod banke: " + banka.getSwiftKodBanke() + "|" + "Obracunski racun: " + banka.getObracunskiRacun()
                    + "|" + "Nalog za prenos: " + analitikaIzvoda.getBrojStavke()
                    + analitikaIzvoda.getDuznik() + ";" + analitikaIzvoda.getRacunDuznika()
                    + ";" + analitikaIzvoda.getAdresaDuznika() + ";" + analitikaIzvoda.getPrimalac()
                    + ";" + analitikaIzvoda.getRacunPrimaoca() + ";" + analitikaIzvoda.getAdresaPrimaoca()
                    + ";" + analitikaIzvoda.getModel() + ";" + analitikaIzvoda.getPozivNaBroj()
                    + ";" + analitikaIzvoda.getIznos() + ";" + analitikaIzvoda.getSvrhaPlacanja()
                    + ";" + analitikaIzvoda.getVrstaPlacanja() + ";" + "Hitno: " +  analitikaIzvoda.getHitno();

            Files.write(path, Collections.singleton(line), StandardCharsets.UTF_8);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void clearing(AnalitikaIzvoda analitikaIzvoda, Banka banka){
        try {
            Path path = Paths.get("src/main/resources/MT102");


                String line = "SWIFT kod banke: " + banka.getSwiftKodBanke() + "|" + "Obracunski racun: " + banka.getObracunskiRacun()
                        + "|" + "Nalog za prenos: " + analitikaIzvoda.getBrojStavke()
                        + analitikaIzvoda.getDuznik() + ";" + analitikaIzvoda.getRacunDuznika()
                        + ";" + analitikaIzvoda.getAdresaDuznika() + ";" + analitikaIzvoda.getPrimalac()
                        + ";" + analitikaIzvoda.getRacunPrimaoca() + ";" + analitikaIzvoda.getAdresaPrimaoca()
                        + ";" + analitikaIzvoda.getModel() + ";" + analitikaIzvoda.getPozivNaBroj()
                        + ";" + analitikaIzvoda.getIznos() + ";" + analitikaIzvoda.getSvrhaPlacanja()
                        + ";" + analitikaIzvoda.getVrstaPlacanja() + ";" + "Hitno: " +  analitikaIzvoda.getHitno();

            Files.write(path, Collections.singleton(line), StandardCharsets.UTF_8, StandardOpenOption.APPEND);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
