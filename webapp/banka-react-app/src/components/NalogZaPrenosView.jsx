import React, { useState } from "react";
import { Button, Switch } from "@material-ui/core";
import { Link } from "react-router-dom";

const NALOG_ZA_PRENOS_DEFAULT = {
  id: null,
  iznos: 0,
  duznik: "",
  adresaDuznika: "",
  svrhaPlacanja: "",
  vrstaPlacanja: "",
  primalac: "",
  adresaPrimaoca: "",
  racunDuznika: "",
  racunPrimaoca: "",
  model: 0,
  pozivNaBroj: "",
  hitno: false,
};

function NalogZaPrenosView(props) {
  const [nalog, setNalog] = useState(NALOG_ZA_PRENOS_DEFAULT);

  const handleChange = (event, prop) => {
    setNalog({
      ...nalog,
      [prop]: event.target.value,
    });
  };

  const handleChangeSwitch = (event) => {
    setNalog({
      ...nalog,
      hitno: event.target.checked,
    });
  };

  const validation = () => {
    let validation = true;
    if (nalog.racunDuznika.length < 18) {
      alert("Pogresan broj");
      validation = false;
    }
    return validation;
  };

  return (
    <>
      <div className="footer">
        <h2>NARODNA BANKA SRBIJE</h2>
        <Link to="/izvjestaj">
          <h3>ANALITIKE IZVJESTAJ</h3>
        </Link>
      </div>
      <div className="container">
        <h2>NALOG ZA PRENOS</h2>{" "}
        <form
          onSubmit={(event) => {
            event.preventDefault();
            if (!validation()) {
              return false;
            }
            props.addNalog(nalog);
            console.log(nalog);
          }}
        >
          <div className="platilacContainer">
            <h4 className="tekst">PLATILAC</h4>
            <label className="tekst">Racun platioca</label>
            <input
              type="text"
              name="racunKodBankePlatioca"
              id="racunKodBanke"
              className="racunBanke"
              maxLength="18"
              minLength="18"
              onChange={(event) => handleChange(event, "racunDuznika")}
            />

            <label className="tekst">Ime i prezime</label>
            <input
              type="text"
              name="imeIPrezime"
              id="imeIPrezime"
              onChange={(event) => handleChange(event, "duznik")}
            />
            <label className="tekst">Adresa</label>
            <input
              type="text"
              name="adresa"
              id="adresa"
              onChange={(event) => handleChange(event, "adresaDuznika")}
            />
            <hr style={{ border: "1px solid black" }} />
          </div>
          <div className="primalacContainer">
            <h4 className="tekst">PRIMALAC</h4>
            <label className="tekst">Racun primaoca</label>
            <input
              type="text"
              name="racunKodBankePrimaoca"
              id="racunKodBanke"
              className="racunBanke"
              maxLength="18"
              minLength="18"
              onChange={(event) => handleChange(event, "racunPrimaoca")}
            />

            <label className="tekst">Ime i prezime</label>
            <input
              type="text"
              name="imeIPrezimePrimaoc"
              id="imeIPrezime"
              onChange={(event) => handleChange(event, "primalac")}
            />
            <label className="tekst">Adresa</label>
            <input
              type="text"
              name="adresaPrimaoc"
              id="adresa"
              onChange={(event) => handleChange(event, "adresaPrimaoca")}
            />
            <label className="tekst">Model i poziv na broj(odobrenje)</label>
            <input
              type="text"
              name="model"
              id="model"
              onChange={(event) => handleChange(event, "model")}
            />
            <input
              type="text"
              name="pozivNaBroj"
              id="pozivNaBroj"
              onChange={(event) => handleChange(event, "pozivNaBroj")}
            />
            <hr style={{ border: "1px solid black" }} />
          </div>
          <div className="detaljiPlacanja">
            <h4 className="tekst">DETALJI PLACANJA</h4>
            <div id="iznosIValutaLabel">
              <label className="tekst" id="iznosLabel">
                Iznos
              </label>
              <label className="tekst" id="valutaLabel">
                Valuta
              </label>
            </div>
            <div id="iznosIValuta">
              <input
                type="text"
                name="iznos"
                id="iznos"
                onChange={(event) => handleChange(event, "iznos")}
              />
              <input
                type="text"
                name="valuta"
                id="valuta"
                readOnly
                value="RSD"
              />
            </div>
            <label className="tekst">Svrha placanja</label>
            <textarea
              name="svrhaPlacanja"
              onChange={(event) => handleChange(event, "svrhaPlacanja")}
            ></textarea>
            <h4 id="h4Hitan">HITAN NALOG</h4>
            <Switch onChange={(event) => handleChangeSwitch(event)} />
          </div>
          <div className="posaljiBtn">
            <Button className="submit" variant="contained" type="submit">
              Posalji
            </Button>
          </div>
        </form>
      </div>
    </>
  );
}

export default NalogZaPrenosView;
