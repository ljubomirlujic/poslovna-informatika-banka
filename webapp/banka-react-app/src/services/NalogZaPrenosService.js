import axios from "axios"


const addNalogZaPrenos = async (nalog) => {

    await axios.post("http://localhost:8080/api/analitike-izvoda", nalog)
    return Promise.resolve(nalog)

}

async function fetchClients() {
    try {
        const resposne = await axios.get("http://localhost:8080/api/klijenti");
        return resposne.data
    } catch (error) {
        console.error(`Error loading clients !: ${error}`);
    }
}

async function fetchAnalitike(id, datumPocetka, datumKraja) {
    try {
        const resposne = await axios.get(`http://localhost:8080/api/analitike-izvoda/${id}/${datumPocetka}/${datumKraja}`);
        return resposne.data
    } catch (error) {
        console.error(`Error loading clients !: ${error}`);
    }
}


export const NalogZaPrenosService = {
    addNalogZaPrenos,
    fetchClients,
    fetchAnalitike
}