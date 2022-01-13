import axios from "axios"


const addNalogZaPrenos = async (nalog) => {
    await axios.post("http://localhost:8080/api/analitike-izvoda", nalog)
    return Promise.resolve(nalog)
}


export const NalogZaPrenosService = {
    addNalogZaPrenos
}