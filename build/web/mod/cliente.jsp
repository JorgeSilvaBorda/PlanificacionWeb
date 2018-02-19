<script type="text/javascript">
    $(document).ready(function () {
        cargarClientes();
    });

    var Cliente = function (id, nombre) {
        this.id = id;
        this.nombre = nombre;

        this.get = function () {
            return {
                id: this.id,
                nombre: this.nombre
            };
        };
    };

    function cargarClientes() {
        var dat = {
            tipo: "cargarClientes"
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
                    $('#cuerpo-cliente').html(obj.tabla);
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

    function edicionCliente(boton) {
        var dat = {
            tipo: "getClienteId",
            idCliente: boton
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
                    setCliente(new Cliente(obj.cliente.ID, obj.cliente.NOMBRE));
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

    function guardarCliente() {
        if (validarCliente()) {
            if ($('#btnAccion').text() === 'Agregar') {
                insertarCliente();
            } else {
                grabarCliente();
            }
        }
    }

    function insertarCliente() {
        var dat = {
            tipo: 'insertarCliente',
            cliente: getCliente()
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
                    cargarClientes();
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

    function grabarCliente() {
        var dat = {
            tipo: 'grabarCliente',
            cliente: getCliente()
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
                    cargarClientes();
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

    function validarCliente() {
        if ($('#nombre').val().length < 3) {
            alert('Debe ingresar el nombre del cliente');
            return false;
        }
        return true;
    }

    function setCliente(cliente) {
        $('#nombre').val(cliente.nombre);
        $('#idCliente').val(cliente.id);
        $('#btnAccion').text("Guardar");
        $('#btnAccion').removeClass('btn-default');
        $('#btnAccion').addClass('btn-warning');
    }

    function getCliente() {
        var cliente = new Cliente(
                $('#idCliente').val(),
                $('#nombre').val()
                );
        return cliente.get();
    }

    function limpiar() {
        $('#nombre').val('');
        $('#idCliente').val('');
        $('#btnAccion').text("Agregar");
        $('#btnAccion').removeClass('btn-warning');
        $('#btnAccion').addClass('btn-default');
    }

    function eliminarCliente(boton) {
        var dat = {
            tipo: 'eliminarCliente',
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
                    cargarClientes();
                    limpiar();
                } else {
                    console.log("No se ha podido eliminar el cliente");
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
                    Mantenedor clientes
                </h1>
            </div>

            <div class="row">
                <input type="hidden" value="" id="idCliente">
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
                            <button onclick="guardarCliente();" id="btnAccion" type="button" class="btn btn-default">Agregar</button>
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
                        <tbody id="cuerpo-cliente">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
