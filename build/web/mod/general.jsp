<script type="text/javascript">
    var DATOS;
    $(document).ready(function () {
        traerTodos();
    });
    function traerTodos() {
        var data;
        var dat = {
            tipo: 'traerTodos'
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'VisionGeneral',
            type: 'post',
            async: false,
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    armar(obj.data);
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
        return data;
    }

    function armar(datos) {
        var $table = $('#tabla-main');
        $(function () {

            $table.bootstrapTable({
                //Modelo tabla padre
                columns: [{
                        field: 'COLID',
                        title: 'ID'
                    },
                    {
                        field: 'COLNOMBRE',
                        title: 'PROYECTO'
                    },
                    {
                        field: 'COLCLIENTE',
                        title: 'CLIENTE'
                    },
                    {
                        field: 'COLJP',
                        title: 'JEFE PROYECTO'
                    },
                    {
                        field: 'COLINIPROYECTO',
                        title: 'FECHA INICIO'
                    },
                    {
                        field: 'COLFINPROYECTO',
                        title: 'FECHA FIN'
                    }],
                data: datos,
                detailView: true,
                onExpandRow: function (index, row, $detail) {
                    console.log(row);
                    $detail.html('<table></table>').find('table').bootstrapTable({
                        columns: [{
                                field: 'COLETAPA',
                                title: 'ETAPA'
                            }, {
                                field: 'COLFECHAINI',
                                title: 'FECHA INICIO'
                            }, {
                                field: 'COLFECHAFIN',
                                title: 'FECHA FIN'
                            }],
                        data: row.etapas,
                        //detailView: row.nested[0]['etapas'] !== undefined,
                        detailView: true,
                        onExpandRow: function (indexb, rowb, $detailb) {
                            $detailb.html('<table></table>').find('table').bootstrapTable({
                                columns: [{
                                        field: 'COLRECURSO',
                                        title: 'RECURSO'
                                    },
                                    {
                                        field: 'COLPORCENTAJE',
                                        title: 'PORCENTAJE'
                                    },
                                    {
                                        field: 'COLROL',
                                        title: 'ROL'
                                    }],
                                data: rowb.recursos
                            });
                        }
                    });
                }
            });
        });
    }

</script>

<div class="row">
    <div class="col-md-12">
        <div class="page-header">
            <h1>
                Visión <small>general</small>
            </h1>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <table id="tabla-main" class="table table-hover table-condensed table-striped small">
            <thead>
                <tr>
                    <th>
                        #
                    </th>
                    <th>
                        ID
                    </th>
                    <th>
                        NOMBRE
                    </th>
                    <th>
                        CLIENTE
                    </th>
                    <th>
                        FECHA INICIO
                    </th>
                    <th>
                        JEFE DE PROYECTO
                    </th>
                </tr>
            </thead>
            <tbody id="cuerpo-proyectos">

            </tbody>
        </table>
    </div>
</div>
