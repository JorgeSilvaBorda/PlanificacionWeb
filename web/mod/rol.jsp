<script type="text/javascript">
    $(document).ready(function () {
        cargarRoles();
    });

    var Rol = function (id, nombre) {
        this.id = id;
        this.nombre = nombre;

        this.get = function () {
            return {
                id: this.id,
                nombre: this.nombre
            };
        };
    };

    function cargarRoles() {
        var dat = {
            tipo: "cargarRoles"
        };

        var datos = JSON.stringify(dat);

        $.ajax({
            url: "Conector",
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    $('#cuerpo-rol').html(obj.tabla);
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

    function edicionRol(boton) {
        var dat = {
            tipo: "getRolId",
            idRol: boton
        };

        var datos = JSON.stringify(dat);

        $.ajax({
            url: "Conector",
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    setRol(new Rol(obj.rol.ID, obj.rol.NOMBRE));
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

    function guardarRol() {
        if (validarRol()) {
            if ($('#btnAccion').text() === 'Agregar') {
                insertarRol();
            } else {
                grabarRol();
            }
        }
    }

    function insertarRol() {
        var dat = {
            tipo: 'insertarRol',
            rol: getRol()
        };

        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Conector',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    cargarRoles();
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

    function grabarRol() {
        var dat = {
            tipo: 'grabarRol',
            rol: getRol()
        };
        console.log(dat);
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Conector',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (resp) {
                var obj = JSON.parse(resp);
                if (obj.estado === 'ok') {
                    cargarRoles();
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

    function validarRol() {
        if ($('#nombre').val().length < 3) {
            alert('Debe ingresar el nombre del rol');
            return false;
        }
        return true;
    }

    function setRol(rol) {
        $('#nombre').val(rol.nombre);
        $('#idRol').val(rol.id);
        $('#btnAccion').text("Guardar");
        $('#btnAccion').removeClass('btn-default');
        $('#btnAccion').addClass('btn-warning');
    }

    function getRol() {
        var rol = new Rol(
                $('#idRol').val(),
                $('#nombre').val()
                );
        return rol.get();
    }

    function limpiar() {
        $('#nombre').val('');
        $('#idRol').val('');
        $('#btnAccion').text("Agregar");
        $('#btnAccion').removeClass('btn-warning');
        $('#btnAccion').addClass('btn-default');
    }

    function eliminarRol(boton) {
        var dat = {
            tipo: 'eliminarRol',
            id: boton
        };
        var datos = JSON.stringify(dat);
        $.ajax({
            url: 'Conector',
            type: 'post',
            data: {
                datos: datos
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    cargarRoles();
                    limpiar();
                } else {
                    console.log("No se ha podido eliminar el rol");
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
                    Mantenedor roles
                </h1>
            </div>

            <div class="row">
                <input type="hidden" value="" id="idRol">
                <div class="col-sm-3">
                    <form role="form">
                        <div class="form-group-sm">
                            <label for="nombre">
                                Nombre
                            </label>
                            <input type="text" class="form-control" id="nombre" />
                        </div>
                        <br />
                        <div class="form-group-sm">
                            <button onclick="guardarRol();" id="btnAccion" type="button" class="btn btn-default">Agregar</button>
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
                                    NOMBRE
                                </th>
                                <th>
                                    ACCIONES
                                </th>
                            </tr>
                        </thead>
                        <tbody id="cuerpo-rol">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
