import React, { useState } from "react";
import TextField from "@mui/material/TextField";
import DateRangePicker from "@mui/lab/DateRangePicker";
import AdapterDateFns from "@mui/lab/AdapterDateFns";
import LocalizationProvider from "@mui/lab/LocalizationProvider";
import Box from "@mui/material/Box";
import Autocomplete from "@mui/material/Autocomplete";
import { Button } from "@mui/material";
import { format } from "date-fns";

const DEFAULT_PODACI = {
  idKlijenta: "",
  period: {
    pocetniDatum: "",
    krajnjiDatum: "",
  },
};

function IzborPeriodaAnalitke(props) {
  const [value, setValue] = React.useState([null, null]);
  const [podaci, setPodaci] = useState(DEFAULT_PODACI);

  let klijentiLista = props.klijenti;
  let klijenti;
  if (klijentiLista != undefined) {
    klijenti = props.klijenti.map((klijent) => ({
      label: klijent.ime + " | " + klijent.jmbg,
      idKlijenta: klijent.id,
    }));
  }

  const handleChange = (newValue) => {
    setPodaci({
      ...podaci,
      idKlijenta: newValue.idKlijenta,
    });
    console.log("ss", newValue.idKlijenta);
  };

  const handleChangePicker = (value) => {
    setValue(value);
    setPodaci({
      ...podaci,
      period: {
        pocetniDatum: format(value[0], "yyyy-MM-dd"),
        krajnjiDatum: format(value[1], "yyyy-MM-dd"),
      },
    });
  };

  console.log(podaci);
  return (
    <form
      onSubmit={(event) => {
        event.preventDefault();
        props.pretrazi(
          podaci.idKlijenta,
          podaci.period.pocetniDatum,
          podaci.period.krajnjiDatum
        );
      }}
    >
      <div className="autoComplete">
        <Autocomplete
          disablePortal
          id="combo-box-demo"
          options={klijenti}
          sx={{ width: 300 }}
          onChange={(event, newValue) => handleChange(newValue)}
          renderInput={(params) => <TextField {...params} label="Klijent" />}
        />
      </div>
      <LocalizationProvider dateAdapter={AdapterDateFns}>
        <DateRangePicker
          startText="Check-in"
          endText="Check-out"
          value={value}
          onChange={(value) => handleChangePicker(value)}
          renderInput={(startProps, endProps) => (
            <React.Fragment>
              <TextField {...startProps} />
              <Box sx={{ mx: 2 }}> to </Box>
              <TextField {...endProps} />
            </React.Fragment>
          )}
        />
      </LocalizationProvider>
      <div className="buttonPregledaj">
        <Button variant="contained" type="submit" id="buttonPregledaj">
          Pregledaj
        </Button>
      </div>
    </form>
  );
}

export default IzborPeriodaAnalitke;
