package pi.banka.service.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pi.banka.domain.AnalitikaIzvoda;
import pi.banka.service.dto.AnalitikaIzvodaDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnalitikaIzvodaToDto implements Converter<AnalitikaIzvoda, AnalitikaIzvodaDTO> {

    @Autowired
    private DnevnoStanjeToDto toDto;

    @Override
    public AnalitikaIzvodaDTO convert(AnalitikaIzvoda source) {
        AnalitikaIzvodaDTO dto = new AnalitikaIzvodaDTO();

        dto.setBrojStavke(source.getBrojStavke());
        dto.setDuznik(source.getDuznik());
        dto.setIznos(source.getIznos());
        dto.setHitno(source.getHitno());
        dto.setId(source.getId());
        dto.setModel(source.getModel());
        dto.setPrimalac(source.getPrimalac());
        dto.setRacunDuznika(source.getRacunDuznika());
        dto.setRacunPrimaoca(source.getRacunPrimaoca());
        dto.setPozivNaBroj(source.getPozivNaBroj());
        dto.setDnevnoStanje(toDto.convert(source.getDnevnoStanje()));
        dto.setSvrhaPlacanja(source.getSvrhaPlacanja());
        dto.setVrstaPlacanja(source.getVrstaPlacanja());

        return dto;
    }

    public List<AnalitikaIzvodaDTO> convertList(List<AnalitikaIzvoda> source) {
        List<AnalitikaIzvodaDTO> ret = new ArrayList();

        for (AnalitikaIzvoda z : source) {
            ret.add(this.convert(z));
        }

        return ret;
    }
}
