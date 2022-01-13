package pi.banka.service.mapper;


import org.mapstruct.Mapper;
import pi.banka.domain.AnalitikaIzvoda;
import pi.banka.service.dto.ReqAnalitikaIzvodaDto;

@Mapper(componentModel = "spring", uses = {})
public interface ReqAnalitikaIzvodaMapper extends EntityMapper<ReqAnalitikaIzvodaDto, AnalitikaIzvoda> {

    ReqAnalitikaIzvodaDto toDto(AnalitikaIzvoda s);
}
