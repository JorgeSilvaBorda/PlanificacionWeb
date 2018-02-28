
<script type='text/javascript'>
    var SALIDA;
    $(document).ready(function () {
        getCargaTodos();
    });
    function getCargaTodos() {
        var dat = {
            tipo: 'getCargaTodos'
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Informe',
            type: 'post',
            async: false,
            data: {
                datos: datos
            },
            success: function (resp) {
                
                var obj = JSON.parse(resp);
                SALIDA = obj;
                if (obj.estado === 'ok') {
                    console.log(obj.valores);
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
<style>
    #chartdiv {
        width: 100%;
        height: 500px;
        margin-top: 15px;
        margin-bottom: 15px;
    }

    .amChartsPeriodSelector {
        float: right;
    }

    .amChartsPeriodSelector .amChartsButton {
        padding-top: 5px;
        padding-bottom: 3px;
        -moz-border-radius: 0;
        border-radius: 0;
        border: 0;
        border-bottom: 1px solid #dddddd;
        outline: none;
        background: #fff;
        color: #000;
    }

    .amChartsPeriodSelector .amChartsButton:hover {
        background-color: #eeeeee;
    }

    .amChartsPeriodSelector .amChartsButtonSelected {
        background-color: #fff;
        border: 0;
        border-bottom: 1px solid #0088CC;
        color: #000000;
        padding-bottom: 3px;
        -moz-border-radius: 0;
        border-radius: 0;
        margin: 1px;
        outline: none;
    }

    .amChartsDataSetSelector {
        padding: 5px 0 0 15px;
    }
</style>

<div class='container-fluid'>
    <div class='row'>
        <div class="col-md-12">
            <div class="page-header">
                <h1>
                    Informe de carga
                </h1>
            </div>
        </div>
    </div>
</div>
