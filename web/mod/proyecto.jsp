

<script type="text/javascript">

    var Proyecto = function (nombre, idCliente, fechaIni, idJefeProyecto) {
        this.idProyecto = null;
        this.nombre = nombre;
        this.idCliente = idCliente;
        this.fechaIni = fechaIni;
        this.idJefeProyecto = idJefeProyecto;
        this.get = function () {
            return {
                nombre: this.nombre,
                idCliente: this.idCliente,
                fechaIni: this.fechaIni,
                idJefeProyecto: this.idJefeProyecto,
            };
        };
        this.set = function (pro) {
            this.nombre = pro.nombre;
            this.idCliente = pro.idCliente;
            this.fechaIni = formatDate(pro.fechaIni);
            this.idJefeProyecto = pro.idJefeProyecto;
        };
        this.setIdProyecto = function (idProyecto) {
            this.idProyecto = idProyecto;
        };
    };

    $(document).ready(function () {
        cargarProyectos();//-->llama a cargarProyectosEdicion();
        cargarCombo("cliente");
        cargarCombo("jefeproyecto");
        //$("#modal-edicion").modal()
    });

    function cargarProyectos() {
        var dat = {
            tipo: 'cargarProyectos'
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Proyectos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#cuerpo-proyectos').html(obj.tabla);
                } else {
                    console.log("No se puede cargar los proyectos.");
                }
            }
        });
    }

    function crearProyecto() {
        var proyecto = new Proyecto(
                $('#nombre').val(),
                $('#cliente').val(),
                $('#fechaini').val(),
                $('#jefeproyecto').val()
                );
        var datos = JSON.stringify({
            tipo: 'ingresarProyecto',
            proyecto: proyecto.get()
        });
        console.log(datos);
        $.ajax({
            url: 'Proyectos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    cargarProyectos();
                    limpiar();
                } else {
                    console.log("No se puede ingresar el proyecto")
                }
            }
        });
    }

    function grabarProyecto() {
        var pro = {
            idProyecto: $('#idProyecto').val(),
            nombre: $('#nombre').val(),
            idCliente: $('#cliente').val(),
            fechaIni: $('#fechaini').val(),
            idJefeProyecto: $('#jefeproyecto').val()
        };
        var dat = {
            tipo: "guardarProyecto",
            proyecto: pro
        };

        var datos = JSON.stringify(dat);
        $.ajax({
            type: 'post',
            url: 'Proyectos',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    cargarProyectos();
                    limpiar();
                } else {
                    console.log("Error al guardar el proyecto");
                }
            }
        });
    }

    function accion() {
        if (validarProyecto()) {
            if ($('#btnAccion').text() === 'Ingresar') {
                crearProyecto();
            } else {
                grabarProyecto();
            }
        }
    }

    function cargarCombo(cual) {
        var dat = {
            tipo: 'cargarCombo',
            cual: cual
        };

        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Otros',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#' + cual).html('');
                    $('#' + cual).html(obj.combo);
                } else {
                    console.log('error');
                    console.log(resp);
                    console.log(obj.error);
                }
            }
        });
    }

    function validarProyecto() {
        var fechaini = $('#fechaini').val();
        var nombre = $('#nombre').val();
        var cliente = $("#cliente").val();
        var jefeproyecto = $('#jefeproyecto').val();

        if (nombre.length < 2) {
            alert('Debe indicar el nombre del proyecto');
            return false;
        }
        if (cliente === 0 || cliente === '0') {
            alert('Debe seleccionar un cliente');
            return false;
        }
        if (fechaini.length < 1) {
            alert('Debe indicar la fecha de inicio del proyecto');
            return false;
        }
        if (jefeproyecto === 0 || jefeproyecto === '0') {
            alert('Debe seleccionar el jefe de proyecto');
            return false;
        }
        return true;
    }

    function limpiar() {
        $('#btnAccion').text('Ingresar');
        $('#nombre').val('');
        cargarCombo("cliente");
        cargarCombo("jefeproyecto");
        $('#fechaini').val('');
        $('#idProyecto').val('');

        $('#btnAccion').removeClass('btn-default');
        $('#btnAccion').removeClass('btn-warning');
        $('#btnAccion').addClass('btn-default');
    }

    function montarProyecto(pro) {
        $('#nombre').val(pro.nombre);
        $('#fechaini').val(pro.fechaIni);
        $('#cliente').val(pro.idCliente);
        $('#jefeproyecto').val(pro.idJefeProyecto);
    }

    function edicionProyecto(id) {
        $('#idProyecto').val(id);
        var dat = {
            tipo: 'getProyectoId',
            idProyecto: id
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Proyectos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    var pro = new Proyecto();
                    pro.set(obj.proyecto);
                    pro.setIdProyecto($('#idProyecto').val());
                    montarProyecto(pro);
                    $('#btnAccion').text('Grabar');
                    $('#btnAccion').removeClass('btn-default');
                    $('#btnAccion').removeClass('btn-warning');
                    $('#btnAccion').addClass('btn-warning');
                } else {
                    console.log("No se puede cargar la edición del proyecto");
                }
            }
        });
    }

    function eliminarProyecto(id) {
        var dat = {
            tipo: "eliminarProyecto",
            idProyecto: id
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Proyectos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    cargarProyectos();
                    limpiar();
                } else {
                    console.log("No se pudo eliminar el proyecto");
                }
            }
        });
    }

    //Edición de etapas proyecto-----------------------------------------------------------------

    function etapasProyecto(boton) {
        var fila = $(boton).parent().parent().parent();
        var texto = $(fila.children()[2]).text();
        var id = $(fila.children()[1]).text();
        var objeto = buscarEtapas(id);
        $('#titulo-modal').html(texto);
        var tabla = document.createElement("table");
        $(tabla).html(objeto.tabla);

        (objeto.registros === 0 ? $('#cuerpo-modal').html(tabla) : $('#cuerpo-modal').html("<h2>No hay etapas registradas en el proyecto</h2>"));
        $('#cuerpo-modal').html(objeto.combo + "<br />" + objeto.tabla);

        $('#modal-edicion').modal();
    }

    function buscarEtapas(idProyecto) {
        var SALIDA;
        var dat = {
            tipo: 'traerEtapas',
            idProyecto: idProyecto
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Proyectos',
            type: 'post',
            async: false,
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    SALIDA = obj;
                } else {
                    console.log("No se puede traer las etapas del proyecto.");
                    console.log(obj.error);
                }
            }
        });
        return SALIDA;
    }

    function eliminarEtapaProyecto(idEtapaProyecto, idProyecto) {
        var dat = {
            tipo: 'eliminarEtapaProyecto',
            idEtapaProyecto: idEtapaProyecto
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Proyectos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    var objeto = buscarEtapas(idProyecto);
                    var tabla = document.createElement("table");
                    $(tabla).html(objeto.tabla);

                    (objeto.registros === 0 ? $('#cuerpo-modal').html(tabla) : $('#cuerpo-modal').html("<h2>No hay etapas registradas en el proyecto</h2>"));
                    $('#cuerpo-modal').html(objeto.combo + "<br />" + objeto.tabla);
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

    var ETAPA;
    function agregarEtapaProyecto() {
        var etapa = {
            idProyecto: parseInt($('#idProyectoEdicion').val()),
            idEtapa: parseInt($('#etapas').val()),
            fechaIni: $('#fechaIniEtapa').val(),
            fechaFin: $('#fechaFinEtapa').val()
        };
        ETAPA = etapa;
        var dat = {
            tipo: 'agregarEtapaProyecto',
            etapa: etapa
        };

        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Proyectos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    var objeto = buscarEtapas(etapa.idProyecto);
                    var tabla = document.createElement("table");
                    $(tabla).html(objeto.tabla);

                    (objeto.registros === 0 ? $('#cuerpo-modal').html(tabla) : $('#cuerpo-modal').html("<h2>No hay etapas registradas en el proyecto</h2>"));
                    $('#cuerpo-modal').html(objeto.combo + "<br />" + objeto.tabla);
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
<!-- Modal para edicion de proyecto -->

<div id="modal-edicion" class="modal modal-lg fade center-block" role="dialog">
    <div class="modal-dialog">

        <!-- Contenido-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="titulo-modal"></h4>
            </div>
            <div class="modal-body" id="cuerpo-modal">

            </div>
            <div class="modal-footer">

                <table class="tabla-form" style="width: 100%;">
                    <tr>
                        <td>
                            <button type="button" class="btn btn-default pull-right" data-dismiss="modal">Cerrar</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

    </div>
</div>

<!-- /Modal -->


<div class="container-fluid">
    <div class="row">
        <div class="col-sm-6">
            <div class="form-group-sm">
                <select id="bla" class="form-control"></select>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="form-group-sm">
                <button type="button" class="btn btn-success btn-sm">Agregar</button>
            </div>
        </div>
    </div>
    <input id="idProyecto" type="hidden" value="" />
    <div class="row">
        <div class="col-md-12">
            <div class="page-header">
                <h1>
                    Proyectos
                </h1>
            </div>
            <div class="row">
                <div class="col-sm-3">
                    <table class="tabla-form">
                        <tr>
                            <td class="td-label">
                                <label for="nombre">Nombre</label>
                            </td>
                            <td>
                                <div class="form-group-sm">
                                    <input id="nombre" type="text" class="form-control small" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="td-label">
                                <label for="cliente">Cliente</label>
                            </td>
                            <td>
                                <div class="form-group-sm">
                                    <select class="form-control small" id="cliente"></select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="td-label">
                                <label for="fechaini">Fecha inicio</label>
                            </td>
                            <td>
                                <div class="form-group-sm">
                                    <input id="fechaini" type="date" class="form-control small" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="td-label">
                                <label for="jefeproyecto">Jefe de Proyecto</label>
                            </td>
                            <td>
                                <div class="form-group-sm">
                                    <select class="form-control small" id="jefeproyecto"></select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="form-group-sm">
                                    <button id="btnAccion" onclick="accion();" type="button" class="btn btn-default">Ingresar</button>
                                </div>
                            </td>
                            <td>
                                <div class="form-group-sm">
                                    <button id="btnLimpiar" onclick="limpiar();" type="button" class="btn btn-default">Limpiar</button>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="col-sm-9">
                    <table class="table table-hover table-condensed table-striped small">
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
                                <th>
                                    ACCIONES
                                </th>
                            </tr>
                        </thead>
                        <tbody id="cuerpo-proyectos">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


