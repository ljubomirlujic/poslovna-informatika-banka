package pi.banka.service.mapper;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pi.banka.domain.DnevnoStanje;
import pi.banka.service.dto.DnevnoStanjeDTO;

@Component
public class DnevnoStanjeToDto implements Converter<DnevnoStanje, DnevnoStanjeDTO> {
    @Override
    public DnevnoStanjeDTO convert(DnevnoStanje source) {
        DnevnoStanjeDTO dto = new DnevnoStanjeDTO();

        dto.setId(source.getId());
        dto.setDatumIzvoda(source.getDatumIzvoda());

        return dto;
    }
}
