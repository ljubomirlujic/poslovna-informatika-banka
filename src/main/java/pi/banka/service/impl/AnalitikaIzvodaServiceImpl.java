package pi.banka.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.banka.domain.AnalitikaIzvoda;
import pi.banka.repository.AnalitikaIzvodaRepository;
import pi.banka.service.AnalitikaIzvodaService;
import pi.banka.service.dto.AnalitikaIzvodaDTO;
import pi.banka.service.dto.ReqAnalitikaIzvodaDto;
import pi.banka.service.mapper.AnalitikaIzvodaMapper;
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

    public AnalitikaIzvodaServiceImpl(AnalitikaIzvodaRepository analitikaIzvodaRepository, AnalitikaIzvodaMapper analitikaIzvodaMapper, ReqAnalitikaIzvodaMapper reqAnalitikaIzvodaMapper) {
        this.analitikaIzvodaRepository = analitikaIzvodaRepository;
        this.analitikaIzvodaMapper = analitikaIzvodaMapper;
        this.reqAnalitikaIzvodaMapper = reqAnalitikaIzvodaMapper;
    }

    @Override
    public AnalitikaIzvodaDTO save(AnalitikaIzvodaDTO analitikaIzvodaDTO) {
        log.debug("Request to save AnalitikaIzvoda : {}", analitikaIzvodaDTO);
        AnalitikaIzvoda analitikaIzvoda = analitikaIzvodaMapper.toEntity(analitikaIzvodaDTO);
        analitikaIzvoda = analitikaIzvodaRepository.save(analitikaIzvoda);
        return analitikaIzvodaMapper.toDto(analitikaIzvoda);
    }

    @Override
    public ReqAnalitikaIzvodaDto save(ReqAnalitikaIzvodaDto reqAnalitikaIzvodaDto) {
        log.debug("Request to save AnalitikaIzvoda : {}", reqAnalitikaIzvodaDto);
        AnalitikaIzvoda analitikaIzvoda = reqAnalitikaIzvodaMapper.toEntity(reqAnalitikaIzvodaDto);
        analitikaIzvoda = analitikaIzvodaRepository.save(analitikaIzvoda);
        return reqAnalitikaIzvodaMapper.toDto(analitikaIzvoda);
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
    public Optional<AnalitikaIzvodaDTO> findOne(Long id) {
        log.debug("Request to get AnalitikaIzvoda : {}", id);
        return analitikaIzvodaRepository.findById(id).map(analitikaIzvodaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnalitikaIzvoda : {}", id);
        analitikaIzvodaRepository.deleteById(id);
    }
}
