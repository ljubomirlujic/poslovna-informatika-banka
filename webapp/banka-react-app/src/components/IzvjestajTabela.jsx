import * as React from "react";
import PropTypes from "prop-types";
import { useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableFooter from "@mui/material/TableFooter";
import TablePagination from "@mui/material/TablePagination";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import IconButton from "@mui/material/IconButton";
import FirstPageIcon from "@mui/icons-material/FirstPage";
import KeyboardArrowLeft from "@mui/icons-material/KeyboardArrowLeft";
import KeyboardArrowRight from "@mui/icons-material/KeyboardArrowRight";
import LastPageIcon from "@mui/icons-material/LastPage";
import jsPDF from "jspdf";
import "jspdf-autotable";
import { Button } from "@mui/material";

function TablePaginationActions(props) {
  const [analitike, setAnalitike] = React.useState(props.analitike);

  const theme = useTheme();
  const { count, page, rowsPerPage, onPageChange } = props;

  const handleFirstPageButtonClick = (event) => {
    onPageChange(event, 0);
  };

  const handleBackButtonClick = (event) => {
    onPageChange(event, page - 1);
  };

  const handleNextButtonClick = (event) => {
    onPageChange(event, page + 1);
  };

  const handleLastPageButtonClick = (event) => {
    onPageChange(event, Math.max(0, Math.ceil(count / rowsPerPage) - 1));
  };

  return (
    <Box sx={{ flexShrink: 0, ml: 2.5 }}>
      <IconButton
        onClick={handleFirstPageButtonClick}
        disabled={page === 0}
        aria-label="first page"
      >
        {theme.direction === "rtl" ? <LastPageIcon /> : <FirstPageIcon />}
      </IconButton>
      <IconButton
        onClick={handleBackButtonClick}
        disabled={page === 0}
        aria-label="previous page"
      >
        {theme.direction === "rtl" ? (
          <KeyboardArrowRight />
        ) : (
          <KeyboardArrowLeft />
        )}
      </IconButton>
      <IconButton
        onClick={handleNextButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="next page"
      >
        {theme.direction === "rtl" ? (
          <KeyboardArrowLeft />
        ) : (
          <KeyboardArrowRight />
        )}
      </IconButton>
      <IconButton
        onClick={handleLastPageButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="last page"
      >
        {theme.direction === "rtl" ? <FirstPageIcon /> : <LastPageIcon />}
      </IconButton>
    </Box>
  );
}

TablePaginationActions.propTypes = {
  count: PropTypes.number.isRequired,
  onPageChange: PropTypes.func.isRequired,
  page: PropTypes.number.isRequired,
  rowsPerPage: PropTypes.number.isRequired,
};

function createData(
  brojStavke,
  duznik,
  primalac,
  racunDuznika,
  racunPrimaoca,
  iznos,
  svrha,
  datum
) {
  return {
    brojStavke,
    duznik,
    primalac,
    racunDuznika,
    racunPrimaoca,
    iznos,
    svrha,
    datum,
  };
}
// createData("Cupcake", 305, 3.7, 1, 1, 1),

export default function CustomPaginationActionsTable(props) {
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(5);

  // Avoid a layout jump when reaching the last page with empty rows.
  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - rows.length) : 0;

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  let analitikeP = props.analitike;
  let analitike;
  let analitikeListaPreuzimanje;

  if (analitikeP != undefined) {
    console.log(analitikeP);
    analitike = analitikeP.map((analitika) =>
      createData(
        analitika.brojStavke,
        analitika.duznik,
        analitika.primalac,
        analitika.racunDuznika,
        analitika.racunPrimaoca,
        analitika.iznos,
        analitika.svrhaPlacanja,
        analitika.dnevnoStanje.datumIzvoda
      )
    );

    analitikeListaPreuzimanje = analitikeP.map((analitika) => [
      "" + analitika.brojStavke,
      "" + analitika.duznik,
      "" + analitika.primalac,
      "" + analitika.racunDuznika,
      "" + analitika.racunPrimaoca,
      "" + analitika.iznos,
      "" + analitika.svrhaPlacanja,
      "" + analitika.dnevnoStanje.datumIzvoda,
    ]);
  }

  console.log(analitike);

  const doc = new jsPDF();

  doc.autoTable({
    head: [
      [
        "broj stavke",
        "duznik",
        "primalac",
        "racun duznika",
        "racun primaoca",
        "iznos",
        "svrha",
        "datum",
      ],
    ],
    body: analitikeListaPreuzimanje,
    // ...
  });
  const preuzmiAnalitiku = () => {
    doc.save("analitika.pdf");
  };

  const rows = analitike.sort((a, b) => (a.calories < b.calories ? -1 : 1));

  return (
    <TableContainer component={Paper}>
      <Table
        sx={{ minWidth: 500 }}
        aria-label="custom pagination table"
        id="my-table"
      >
        <TableHead>
          <TableRow>
            <TableCell>Broj stavke</TableCell>
            <TableCell align="right">Duznik</TableCell>
            <TableCell align="right">Primalac</TableCell>
            <TableCell align="right">Racun duznika</TableCell>
            <TableCell align="right">Racun primaoca</TableCell>
            <TableCell align="right">Iznos</TableCell>
            <TableCell align="right">Svrha</TableCell>
            <TableCell align="right">Datum prenosa</TableCell>
            <TableCell>
              {" "}
              <Button onClick={preuzmiAnalitiku}>preuzmi</Button>
            </TableCell>
          </TableRow>
        </TableHead>

        <TableBody>
          {(rowsPerPage > 0
            ? rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            : rows
          ).map((row) => (
            <TableRow
              key={row.brojStavke}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {row.brojStavke}
              </TableCell>
              <TableCell align="right">{row.duznik}</TableCell>
              <TableCell align="right">{row.primalac}</TableCell>
              <TableCell align="right">{row.racunDuznika}</TableCell>
              <TableCell align="right">{row.racunPrimaoca}</TableCell>
              <TableCell align="right">{row.iznos}</TableCell>
              <TableCell align="right">{row.svrha}</TableCell>
              <TableCell align="right">{row.datum}</TableCell>
            </TableRow>
          ))}

          {emptyRows > 0 && (
            <TableRow style={{ height: 53 * emptyRows }}>
              <TableCell colSpan={6} />
            </TableRow>
          )}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25, { label: "All", value: -1 }]}
              colSpan={3}
              count={rows.length}
              rowsPerPage={rowsPerPage}
              page={page}
              SelectProps={{
                inputProps: {
                  "aria-label": "rows per page",
                },
                native: true,
              }}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
              ActionsComponent={TablePaginationActions}
            />
          </TableRow>
        </TableFooter>
      </Table>
    </TableContainer>
  );
}
