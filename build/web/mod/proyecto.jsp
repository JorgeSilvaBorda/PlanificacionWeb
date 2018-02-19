<script type="text/javascript">

    $(document).ready(function () {
        cargarProyectos();
    });

    function cargarProyectos() {

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
                                <li><a href="#edicion" data-toggle="tab">Edición</a></li>
                            </ul>
                        </div>
                        
                        <div class="panel-body">
                            <div class="tab-content">
                                
                                <div class="tab-pane fade in active" id="enCurso">
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
                                                    FECHA FIN
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
                                
                                <!-- Contenido de la edición de proyectos -->
                                <div class="tab-pane fade" id="edicion">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

