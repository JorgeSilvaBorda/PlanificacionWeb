<script type="text/javascript">

    var Proyecto = function (nombre, idCliente, fechaIni, idJefeProyecto) {
        this.nombre = nombre;
        this.idCliente = idCliente;
        this.fechaIni = fechaIni;
        this.idJefeProyecto = idJefeProyecto;
        this.nomCliente = getNomCliente(idCliente);
        this.nomJefeProyecto = getNomJefeProyecto(idJefeProyecto);
        this.get = function () {
            return {
                nombre: this.nombre,
                idCliente: this.idCliente,
                nomCliente: this.nomCliente,
                fechaIni: this.fechaIni,
                idJefeProyecto: this.idJefeProyecto,
                nomJefeProyecto: this.nomJefeProyecto
            };
        };
    };

    function getNomCliente(idCliente) {
        var salida;
        var dat = {
            tipo: 'getNomClienteId',
            idCliente: idCliente
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            type: 'post',
            url: 'Otros',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    salida = obj.nombre;
                    return salida;
                } else {
                    console.log("No se puede obtener el nombre del cliente.");
                }
            }
        });
        return salida;
    }

    function getNomJefeProyecto(idJefeProyecto) {
        var salida = "";
        var dat = {
            tipo: 'getNomJefeProyectoId',
            idJefeProyecto: idJefeProyecto
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            type: 'post',
            url: 'Otros',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    salida = obj.nombre;
                    return salida;
                } else {
                    console.log("No se puede obtener el nombre del jefe de proyecto.");
                }
            }
        });
        return salida;
    }

    $(document).ready(function () {
        cargarProyectos();
        cargarCombo("cliente");
        cargarCombo("jefeproyecto");
    });

    function cargarProyectos() {
        var dat = {
            tipo: 'cargarProyectos'
        };
        var datos = JSON.stringify(dat);
        $.ajax({

        });
    }

    var PROJ;
    function crearProyecto() {
        var proyecto = new Proyecto(
                $('#nombre').val(),
                $('#cliente').val(),
                $('#fechaini').val(),
                $('#jefeproyecto').val()
                );
        PROJ = proyecto;
        var datos = JSON.stringify({
            tipo: 'ingresarProyecto',
            proyecto: proyecto.get()
        });
        $.ajax({
            url: 'Proyectos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function(resp){
                var obj = JSON.parse(resp);
                if(obj.estado === 'ok'){
                    
                }else{
                    console.log("No se puede ingresar el proyecto")
                }
            }
        });
    }

    function grabarProyecto() {

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

        $('#btnAccion').removeClass('btn-default');
        $('#btnAccion').removeClass('btn-warning');
        $('#btnAccion').addClass('btn-default');
    }
</script>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <div class="page-header">
                <h1>
                    Proyectos
                </h1>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <div class="panel with-nav-tabs panel-default">
                        <div class="panel-heading">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#enCurso" data-toggle="tab">En curso</a></li>
                                <li><a href="#edicion" data-toggle="tab">Edici�n</a></li>
                            </ul>
                        </div>

                        <div class="panel-body">
                            <div class="tab-content">

                                <div class="tab-pane fade in active" id="enCurso">
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

                                <!-- Contenido de la edici�n de proyectos -->
                                <div class="tab-pane fade" id="edicion">
                                    <form id="form-proyecto">

                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

