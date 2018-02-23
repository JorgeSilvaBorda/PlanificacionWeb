<script src="../amcharts/amstock.js" type="text/javascript"></script>
<script src="../amcharts/amcharts.js" type="text/javascript"></script>
<script src="../amcharts/serial.js" type="text/javascript"></script>
<script src="../amcharts/export.min.js" type="text/javascript"></script>

<link href="../amcharts/export.css" rel="stylesheet" type="text/css"/>

<script type='text/javascript'>
    
    $(document).ready(function(){
        
    });
    
    function getRangoFechas() {
        var dat = {
            tipo: 'getRangoFechas'
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Inform',
            type: 'post',
            async: false,
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    return {
                        fechaIni: obj.fechaIni,
                        fechaFin: obj.fechaFin
                    };
                } else {
                    console.log(obj.error);
                }
            },
            error: function (a, b, c) {
                console.log(a);
                console.log(b);
                console.log(c);
            }
        });
    }

</script>


<div class='container-fluid'>
    <div class='row'>

    </div>
</div>
