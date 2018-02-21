<script type="text/javascript">
    $(document).ready(function () {
        //Cargar grid en JSON
        cargarGrid();
    });

    function cargarGrid() {
    }

    var data = [{
            'COLID': '1',
            'COLNOMBRE': 'Ejemplo',
            'COLCLIENTE': 'CLIENTE',
            'COLJP': 'Jorge Silva Borda',
            'COLFECHAINI': '20-12-2018',
            'COLFECHAFIN': '20-12-2018',
            'nested': [{
                    'COLETAPA': 'ANALISIS',
                    'COLFECHAINI': '20-12-2018',
                    'COLFECHAFIN': '23-12-2018',
                    'other': [{
                            'COLRECURSO': 'Hugo Mercado',
                            'COLPORCENTAJE': '50%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Jorge Silva',
                            'COLPORCENTAJE': '30%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Lorena González',
                            'COLPORCENTAJE': '20%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        }]
                },
                {
                    'COLETAPA': 'DISEÑO',
                    'COLFECHAINI': '20-12-2018',
                    'COLFECHAFIN': '23-12-2018',
                    'other': [{
                            'COLRECURSO': 'Hugo Mercado',
                            'COLPORCENTAJE': '50%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Jorge Silva',
                            'COLPORCENTAJE': '30%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Lorena González',
                            'COLPORCENTAJE': '20%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        }]
                },
                {
                    'COLETAPA': 'DESARROLLO',
                    'COLFECHAINI': '20-12-2018',
                    'COLFECHAFIN': '23-12-2018',
                    'other': [{
                            'COLRECURSO': 'Hugo Mercado',
                            'COLPORCENTAJE': '50%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Jorge Silva',
                            'COLPORCENTAJE': '30%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Lorena González',
                            'COLPORCENTAJE': '20%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        }]
                },
                {
                    'COLETAPA': 'PRUEBAS',
                    'COLFECHAINI': '20-12-2018',
                    'COLFECHAFIN': '23-12-2018',
                    'other': [{
                            'COLRECURSO': 'Hugo Mercado',
                            'COLPORCENTAJE': '50%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Jorge Silva',
                            'COLPORCENTAJE': '30%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Lorena González',
                            'COLPORCENTAJE': '20%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        }]
                },
                {
                    'COLETAPA': 'PRODUCCION',
                    'COLFECHAINI': '20-12-2018',
                    'COLFECHAFIN': '23-12-2018',
                    'other': [{
                            'COLRECURSO': 'Hugo Mercado',
                            'COLPORCENTAJE': '50%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Jorge Silva',
                            'COLPORCENTAJE': '30%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        },
                        {
                            'COLRECURSO': 'Lorena González',
                            'COLPORCENTAJE': '20%',
                            'COLEDICION': '<button type=\"button\">Editar</button>'
                        }]
                }]
        }];


    var $table = $('#tabla-main');
    $(function () {

        $table.bootstrapTable({
            //Modelo tabla padre
            columns: [{
                    field: 'COLID',
                    title: 'ID'
                }, {
                    field: 'COLNOMBRE',
                    title: 'PROYECTO'
                },
                {
                    field: 'COLFECHAINI',
                    title: 'FECHA INICIO'
                },
                {
                    field: 'COLFECHAFIN',
                    title: 'FECHA FIN'
                },
                {
                    field: 'COLCLIENTE',
                    title: 'CLIENTE'
                },
                {
                    field: 'COLJP',
                    title: 'JEFE PROYECTO'
                }],
            data: data,
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
                    data: row.nested,
                    // Simple contextual, assumes all entries have further nesting
                    // Just shows example of how you might differentiate some rows, though also remember row class and similar possible flags
                    detailView: row.nested[0]['other'] !== undefined,
                    onExpandRow: function (indexb, rowb, $detailb) {
                        $detailb.html('<table></table>').find('table').bootstrapTable({
                            columns: [{
                                    field: 'COLRECURSO',
                                    title: 'RECURSO'
                                }, {
                                    field: 'COLPORCENTAJE',
                                    title: 'PORCENTAJE'
                                },
                                {
                                    field: 'COLEDICION',
                                    title: 'ACCION'
                                }],
                            data: rowb.other
                        });
                    }
                });

            }
        });
    });
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
