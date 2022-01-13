package pi.banka.service.dto;

import java.util.Objects;
import java.io.Serializable;

public class ReqAnalitikaIzvodaDto implements Serializable {

    private Long id;

    private Double iznos;

    private String duznik;

    private String adresaDuznika;

    private String svrhaPlacanja;

    private String vrstaPlacanja;

    private String primalac;

    private String adresaPrimaoca;

    private String racunDuznika;

    private String racunPrimaoca;

    private Integer model;

    private String pozivNaBroj;

    private Boolean hitno;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getIznos() {
        return iznos;
    }

    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }

    public String getDuznik() {
        return duznik;
    }

    public String getAdresaDuznika() {
        return adresaDuznika;
    }

    public void setAdresaDuznika(String adresaDuznika) {
        this.adresaDuznika = adresaDuznika;
    }

    public String getAdresaPrimaoca() {
        return adresaPrimaoca;
    }

    public void setAdresaPrimaoca(String adresaPrimaoca) {
        this.adresaPrimaoca = adresaPrimaoca;
    }

    public void setDuznik(String duznik) {
        this.duznik = duznik;
    }

    public String getSvrhaPlacanja() {
        return svrhaPlacanja;
    }

    public void setSvrhaPlacanja(String svrhaPlacanja) {
        this.svrhaPlacanja = svrhaPlacanja;
    }

    public String getVrstaPlacanja() {
        return vrstaPlacanja;
    }

    public void setVrstaPlacanja(String vrstaPlacanja) {
        this.vrstaPlacanja = vrstaPlacanja;
    }

    public String getPrimalac() {
        return primalac;
    }

    public void setPrimalac(String primalac) {
        this.primalac = primalac;
    }

    public String getRacunDuznika() {
        return racunDuznika;
    }

    public void setRacunDuznika(String racunDuznika) {
        this.racunDuznika = racunDuznika;
    }

    public String getRacunPrimaoca() {
        return racunPrimaoca;
    }

    public void setRacunPrimaoca(String racunPrimaoca) {
        this.racunPrimaoca = racunPrimaoca;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getPozivNaBroj() {
        return pozivNaBroj;
    }

    public void setPozivNaBroj(String pozivNaBroj) {
        this.pozivNaBroj = pozivNaBroj;
    }

    public Boolean getHitno() {
        return hitno;
    }

    public void setHitno(Boolean hitno) {
        this.hitno = hitno;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReqAnalitikaIzvodaDto)) {
            return false;
        }

        ReqAnalitikaIzvodaDto reqAnalitikaIzvodaDto = (ReqAnalitikaIzvodaDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reqAnalitikaIzvodaDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "ReqAnalitikaIzvodaDto{" +
                "id=" + id +
                ", iznos=" + iznos +
                ", duznik='" + duznik + '\'' +
                ", adresaDuznika='" + adresaDuznika + '\'' +
                ", svrhaPlacanja='" + svrhaPlacanja + '\'' +
                ", vrstaPlacanja='" + vrstaPlacanja + '\'' +
                ", primalac='" + primalac + '\'' +
                ", adresaPrimaoca='" + adresaPrimaoca + '\'' +
                ", racunDuznika='" + racunDuznika + '\'' +
                ", racunPrimaoca='" + racunPrimaoca + '\'' +
                ", model=" + model +
                ", pozivNaBroj='" + pozivNaBroj + '\'' +
                ", hitno=" + hitno +
                '}';
    }
}
