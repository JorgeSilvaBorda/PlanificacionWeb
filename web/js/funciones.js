function formatDate(fecha) {
    var dateString = new Date(fecha).toUTCString();
    dateString = dateString.split(' ').slice(0, 4).join(' ');

    var fec = new Date(fecha);
    var dd = fec.getDate();
    var mm = fec.getMonth() + 1; //January is 0!

    var yyyy = fec.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }
    if (mm < 10) {
        mm = '0' + mm;
    }
    return today = yyyy + '-' + mm + '-' + dd;
    

    //return dateString;
}