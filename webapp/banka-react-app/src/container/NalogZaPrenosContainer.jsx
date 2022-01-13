import React from "react";
import NalogZaPrenosView from "../components/NalogZaPrenosView";
import { NalogZaPrenosService } from "../services/NalogZaPrenosService";

function NalogZaPrenos() {
  const addNalogZaPrenos = (nalog) => {
    NalogZaPrenosService.addNalogZaPrenos(nalog);
  };

  return <NalogZaPrenosView addNalog={addNalogZaPrenos} />;
}

export default NalogZaPrenos;
