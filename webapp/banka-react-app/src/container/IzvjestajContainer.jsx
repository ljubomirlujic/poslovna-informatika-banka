import React, { useEffect, useState } from "react";
import IzvjestajView from "../components/IzvjestajView";
import { NalogZaPrenosService } from "../services/NalogZaPrenosService";

function IzvjestajContainer() {
  const [clients, setClients] = useState();
  const [analitike, setAnalitike] = useState([]);

  const pretraziAnalitike = (id, datumPocetka, datumKraja) => {
    NalogZaPrenosService.fetchAnalitike(id, datumPocetka, datumKraja).then(
      (res) => setAnalitike(res)
    );
    console.log(id, datumPocetka, datumKraja);
  };

  console.log(analitike);

  useEffect(() => {
    NalogZaPrenosService.fetchClients().then((res) => setClients(res));
  }, []);
  console.log(clients);

  return (
    <IzvjestajView
      klijenti={clients}
      analitike={analitike}
      pretrazi={pretraziAnalitike}
    />
  );
}

export default IzvjestajContainer;
