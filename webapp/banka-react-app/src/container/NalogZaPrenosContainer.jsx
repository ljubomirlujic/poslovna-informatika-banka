import React, { useEffect, useState } from "react";
import NalogZaPrenosView from "../components/NalogZaPrenosView";
import { NalogZaPrenosService } from "../services/NalogZaPrenosService";

function NalogZaPrenos() {
  const [racuni, setRacuni] = useState();
  const addNalogZaPrenos = (nalog) => {
    NalogZaPrenosService.addNalogZaPrenos(nalog);
  };

  useEffect(() => {
    NalogZaPrenosService.fetchRacuni().then((res) => setRacuni(res));
  }, []);

  console.log(racuni);
  return <NalogZaPrenosView addNalog={addNalogZaPrenos} racuni={racuni} />;
}

export default NalogZaPrenos;
