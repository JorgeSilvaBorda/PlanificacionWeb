<script type="text/javascript">
    $(document).ready(function () {
        cargarEtapas();
    });

    var Etapa = function (id, nombre) {
        this.id = id;
        this.nombre = nombre;

        this.get = function () {
            return {
                id: this.id,
                nombre: this.nombre
            };
        };
    };

    function cargarEtapas() {
        var dat = {
            tipo: "cargarEtapas"
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
                    $('#cuerpo-etapa').html(obj.tabla);
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

    function edicionEtapa(boton) {
        var dat = {
            tipo: "getEtapaId",
            idEtapa: boton
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
                    setEtapa(new Etapa(obj.etapa.ID, obj.etapa.NOMBRE));
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

    function guardarEtapa() {
        if (validarEtapa()) {
            if ($('#btnAccion').text() === 'Agregar') {
                insertarEtapa();
            } else {
                grabarEtapa();
            }
        }
    }

    function insertarEtapa() {
        var dat = {
            tipo: 'insertarEtapa',
            etapa: getEtapa()
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
                    cargarEtapas();
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

    function grabarEtapa() {
        var dat = {
            tipo: 'grabarEtapa',
            etapa: getEtapa()
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
                    cargarEtapas();
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

    function validarEtapa() {
        if ($('#nombre').val().length < 3) {
            alert('Debe ingresar el nombre de la etapa');
            return false;
        }
        return true;
    }

    function setEtapa(etapa) {
        $('#nombre').val(etapa.nombre);
        $('#idEtapa').val(etapa.id);
        $('#btnAccion').text("Guardar");
        $('#btnAccion').removeClass('btn-default');
        $('#btnAccion').addClass('btn-warning');
    }

    function getEtapa() {
        var etapa = new Etapa(
                $('#idEtapa').val(),
                $('#nombre').val()
                );
        return etapa.get();
    }

    function limpiar() {
        $('#nombre').val('');
        $('#idEtapa').val('');
        $('#btnAccion').text("Agregar");
        $('#btnAccion').removeClass('btn-warning');
        $('#btnAccion').addClass('btn-default');
    }

    function eliminarEtapa(boton) {
        var dat = {
            tipo: 'eliminarEtapa',
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
                    cargarEtapas();
                    limpiar();
                } else {
                    console.log("No se ha podido eliminar la etapa");
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
                    Mantenedor etapas
                </h1>
            </div>

            <div class="row">
                <input type="hidden" value="" id="idEtapa">
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
                            <button onclick="guardarEtapa();" id="btnAccion" type="button" class="btn btn-default">Agregar</button>
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
                        <tbody id="cuerpo-etapa">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
