<script type="text/javascript">

    String.prototype.replaceAll = function (target, replacement) {
        return this.split(target).join(replacement);
    };
    $(document).ready(function () {
        cargarRecursos();
        $('#rut').Rut({
            on_error: function () {
                alert('Rut incorrecto');
                $('#rut').focus();
            },
            format_on: 'keyup'
        });
    });

    var Recurso = function (id, rut, nombre, costo, costouso) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.costo = costo;
        this.costouso = costouso;
        this.get = function () {
            return {
                id: this.id,
                rut: this.rut,
                nombre: this.nombre,
                costo: this.costo,
                costouso: this.costouso
            };
        };
    };

    function cargarRecursos() {
        var dat = {
            tipo: "cargarRecursos"
        };

        var datos = JSON.stringify(dat);

        $.ajax({
            url: "Recursos",
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#cuerpo-recurso').html(obj.tabla);
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

    function edicionRecurso(boton) {
        var dat = {
            tipo: "getRecursoId",
            idRecurso: boton
        };

        var datos = JSON.stringify(dat);

        $.ajax({
            url: "Recursos",
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    setRecurso(new Recurso(obj.recurso.ID, obj.recurso.RUT, obj.recurso.NOMBRE, obj.recurso.COSTO, obj.recurso.COSTOUSO));
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

    function guardarRecurso() {
        if (validarRecurso()) {
            if ($('#btnAccion').text() === 'Agregar') {
                insertarRecurso();
            } else {
                grabarRecurso();
            }
        }
    }

    function insertarRecurso() {
        var dat = {
            tipo: 'insertarRecurso',
            recurso: getRecurso()
        };

        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Recursos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    cargarRecursos();
                    limpiar();
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

    function grabarRecurso() {
        var dat = {
            tipo: 'grabarRecurso',
            recurso: getRecurso()
        };

        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Recursos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    cargarRecursos();
                    limpiar();
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

    function validarRecurso() {
        if ($('#rut').val().length < 3 || $('#nombres').val().length < 3) {
            alert('Debe ingresar al menos el rut y el nombre del recurso');
            return false;
        }
        return true;
    }

    function setRecurso(recurso) {
        //console.log("El recurso: " + recurso);
        $('#rut').val(recurso.rut);
        $('#rut').keyup();
        $('#nombres').val(recurso.nombre);
        $('#costo').val(recurso.costo);
        $('#costouso').val(recurso.costouso);
        $('#idRecurso').val(recurso.id);
        $('#btnAccion').text("Guardar");
        $('#btnAccion').removeClass('btn-default');
        $('#btnAccion').addClass('btn-warning');
    }

    function getRecurso() {
        var recurso = new Recurso(
                $('#idRecurso').val(),
                $('#rut').val().replaceAll(".", ""),
                $('#nombres').val(),
                $('#costo').val(),
                $('#costouso').val()
                );
        return recurso.get();
    }

    function limpiar() {
        $('#rut').val('');
        $('#nombres').val('');
        $('#costo').val('');
        $('#costouso').val('');
        $('#idRecurso').val('');
        $('#btnAccion').text("Agregar");
        $('#btnAccion').removeClass('btn-warning');
        $('#btnAccion').addClass('btn-default');
    }

    function eliminarRecurso(boton) {
        var dat = {
            tipo: 'eliminarRecurso',
            id: boton
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Recursos',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    cargarRecursos();
                    limpiar();
                } else {
                    console.log("No se ha podido eliminar el recurso");
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

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <div class="page-header">
                <h1>
                    Mantenedor recursos
                </h1>
            </div>

            <div class="row">
                <input type="hidden" value="" id="idRecurso">
                <div class="col-sm-3">
                    <form role="form">
                        <div class="form-group-sm">
                            <label for="rut">
                                Rut
                            </label>
                            <input type="text" class="form-control" id="rut" maxlength="12" />
                        </div>
                        <div class="form-group-sm">
                            <label for="nombres">
                                Nombres
                            </label>
                            <input type="text" class="form-control" id="nombres" />
                        </div>
                        <div class="form-group-sm">
                            <label for="costo">
                                Costo
                            </label>
                            <input type="text" class="form-control" id="costo" />
                        </div>
                        <div class="form-group-sm">
                            <label for="costouso">
                                Costo/Uso
                            </label>
                            <input type="text" class="form-control" id="costouso" />
                        </div>
                        <br />
                        <div class="form-group-sm">
                            <button onclick="guardarRecurso();" id="btnAccion" type="button" class="btn btn-default">Agregar</button>
                            <button onclick="limpiar();" id="btLimpiar" type="button" class="btn btn-default">Limpiar</button>
                        </div>
                    </form>
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
                                    RUT
                                </th>
                                <th>
                                    NOMBRES
                                </th>
                                <th>
                                    COSTO
                                </th>
                                <th>
                                    COSTO/USO
                                </th>
                                <th>
                                    ACCIONES
                                </th>
                            </tr>
                        </thead>
                        <tbody id="cuerpo-recurso">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
