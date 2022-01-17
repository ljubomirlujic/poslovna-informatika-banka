import React from "react";
import IzvjestajTabela from "./IzvjestajTabela";
import IzborPeriodaAnalitke from "./IzborPeriodaAnalitke";
import { Button } from "@mui/material";
import { Link } from "react-router-dom";

function IzvjestajView(props) {
  return (
    <>
      <div className="footer">
        <h2>NARODNA BANKA SRBIJE</h2>
        <Link to="/">
          <h3>PRENOS</h3>
        </Link>
      </div>

      <div className="datePicker">
        <h4>Analitike izvoda</h4>
        <IzborPeriodaAnalitke
          klijenti={props.klijenti}
          pretrazi={props.pretrazi}
        />
      </div>
      <div className="tabelaContainer">
        <IzvjestajTabela analitike={props.analitike} />
      </div>
    </>
  );
}

export default IzvjestajView;
