<script type="text/javascript">
    var DATOSEDIT;
    $(document).ready(function () {
        cargarProyectos();
        cargarRecursos();
        cargarRoles();
        $('#proyectos').change(function () {
            cargarEtapas();
        });
    });


    function cargarProyectos() {
        var dat = {
            tipo: 'cargarProyectos'
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Asignaciones',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#proyectos').html(obj.combo);
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

    function cargarEtapas() {
        limpiarHidden();
        $('#hidIdProyecto').val($('#proyectos').val());
        $('#titulo-recursos').html('');
        var dat = {
            tipo: 'cargarEtapas',
            idProyecto: $('#proyectos').val()
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Asignaciones',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    if (parseInt(obj.registros) === 0) {
                        $('#cuerpo-etapas').html("<tr><td colspan='4' class='text-center'>El proyecto seleccionado no posee etapas asignadas.</td></tr></div>");
                    } else {
                        $('#tabla-etapas').show();
                        $('#cuerpo-etapas').html(obj.tabla);
                    }

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
        return false;
    }

    function verRecursos(idProyecto, idEtapa, nombreEtapa, idEtapaProyecto) {

        $('#recursos').val('0');
        $('#roles').val('0');
        $('#porcentaje').val('');
        $('#hidIdProyecto').val(idProyecto);
        $('#hidIdEtapa').val(idEtapa);
        $('#hidIdEtapaProyecto').val(idEtapaProyecto);
        $('#titulo-recursos').html(nombreEtapa);
        var dat = {
            tipo: 'verRecursos',
            idProyecto: idProyecto,
            idEtapa: idEtapa
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Asignaciones',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#cuerpo-recursos').html(obj.tabla);
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

    function cargarRecursos() {
        $('#recursos').html('');
        var dat = {
            tipo: 'cargarRecursos'
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Asignaciones',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#recursos').html(obj.combo);
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

    function cargarRoles() {
        $('#roles').html('');
        var dat = {
            tipo: 'cargarRoles'
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Asignaciones',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#roles').html(obj.combo);
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

    function agregarRecurso() {
        var dat = {
            tipo: 'agregarRecurso',
            idRecurso: $('#recursos').val(),
            idRol: $('#roles').val(),
            porcentaje: $('#porcentaje').val(),
            idEtapaProyecto: $('#hidIdEtapaProyecto').val()
        };

        if (validarRecurso(dat)) {
            var datos = JSON.stringify(dat);
            $.ajax({
                url: 'Asignaciones',
                type: 'post',
                data: {
                    datos: datos
                },
                success: function (resp) {
                    var obj = JSON.parse(resp);
                    if (obj.estado === 'ok') {
                        verRecursos($('#hidIdProyecto').val(), $('#hidIdEtapa').val(), $('#titulo-recursos').text(), $('#hidIdEtapaProyecto').val());
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
    }

    function quitarRecurso(idEtapaProyectoPersonal) {

        var dat = {
            tipo: 'quitarRecurso',
            idEtapaProyectoPersonal: idEtapaProyectoPersonal
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Asignaciones',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    verRecursos($('#hidIdProyecto').val(), $('#hidIdEtapa').val(), $('#titulo-recursos').text(), $('#hidIdEtapaProyecto').val());
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

    function limpiarHidden() {
        $('#hidIdProyecto').val('');
        $('#hidIdEtapaProyecto').val('');
        $('#hidIdEtapa').val('');
    }

    function validarRecurso(datos) {
        if(datos.idRecurso === '0'){
            alert('Debe seleccionar al recurso');
            return false;
        }
        if(datos.idRol === '0'){
            alert('Debe seleccionar el rol del recurso');
            return false;
        }
        if(datos.porcentaje === '' || parseInt(datos.porcentaje) < 1){
            alert('Debe indicar el porcentaje de asignación para el recurso');
            return false;
        }
        if(parseInt(datos.porcentaje) > 100){
            alert("El porcentaje de asignación no puede ser mayor a 100");
            return false;
        }
        if($('hidIdEtapaProyecto').val() === ''){
            alert('No ha seleccionado una etapa para asignar recursos');
            return false;
        }
        return true;
    }

</script>

<div class="container-fluid">
    <input type='hidden' value='' id='hidIdProyecto' />
    <input type='hidden' value='' id='hidIdEtapaProyecto' />
    <input type='hidden' value='' id='hidIdEtapa' />
    <div class="row">
        <div class="col-md-12">
            <div class="page-header">
                <h1>
                    Asignación recursos - etapa
                </h1>
            </div>
        </div>
    </div>

    <div class="row">

        <!-- Proyectos y etapas asociadas -->
        <div class="col-sm-4">
            <div class="form-group-sm">
                <label for="proyectos">Proyectos</label>
                <select id="proyectos" class="form-control"></select>
            </div>
            <div class="form-group-sm">
                <table id="tabla-etapas" class="table table-hover table-condensed table-striped small">
                    <thead>
                        <tr>
                            <th>ETAPA</th>
                            <th>INICIO</th>
                            <th>FIN</th>
                            <th>ACCIONES</th>
                        </tr>
                    </thead>
                    <tbody id="cuerpo-etapas"></tbody>
                </table>
            </div>
        </div>
        <!-- /Proyectos y etapas asociadas -->

        <!-- Recursos para las etapas seleccionadas-->
        <div class='col-sm-8' id='contenedor-recursos'>

            <!-- Mantenedor agregar recursos -->
            <div class='row' id='mantenedor-recursos'>
                <div class='col-sm-4'>
                    <div class='form-group-sm'>
                        <label for='recursos'>Recursos</label>
                        <select id='recursos' class='form-control'></select>
                    </div>
                </div>
                <div class='col-sm-4'>
                    <div class='form-group-sm'>
                        <label for='roles'>Rol</label>
                        <select id='roles' class='form-control'></select>
                    </div>
                </div>
                <div class='col-sm-2'>
                    <div class='form-group-sm'>
                        <label for='porcentaje'>Porcentaje</label>
                        <input type='number' id='porcentaje' max='100' min='1' class='form-control' />
                    </div>
                </div>
                <div class='col-sm-2'>
                    <div class='form-group-sm'>
                        <label>&nbsp;</label>
                        <br />
                        <button onclick='agregarRecurso();' type='button' class='btn btn-sm btn-success'>Insertar</button>
                    </div>
                </div>
            </div>
            <!-- /Mantenedor agregar recursos -->

            <!-- Tabla listado recursos -->
            <div class='row' id='contenedor-tabla-recursos'>
                <div class='col-sm-12'>
                    <div class='h3' id='titulo-recursos'>

                    </div>
                </div>
                <div class='col-sm-12'>
                    <table class='table table-hover table-condensed table-striped small'>
                        <thead>
                            <tr>
                                <th>RECURSO</th>
                                <th>ROL</th>
                                <th>PORCENTAJE</th>
                                <th>ACCION</th>
                            </tr>
                        </thead>
                        <tbody id='cuerpo-recursos'>

                        </tbody>
                    </table>
                </div>
            </div>
            <!-- Tabla listado recursos -->

        </div>
        <!-- /Recursos para las etapas seleccionadas-->
    </div>
</div>
