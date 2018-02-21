<style>
    .dropdown-submenu {
        position: relative;
    }

    .dropdown-submenu .dropdown-menu {
        top: 0;
        left: 100%;
        margin-top: -1px;
    }
</style>

<script type="text/javascript">
    $(document).ready(function () {
        $('.dropdown-submenu a.test').on("click", function (e) {
            $(this).next('ul').toggle();
            e.stopPropagation();
            e.preventDefault();
        });
        cargarModulo("general");
    });
    
    function cargarModulo(nombre){
        $('#contenido').load('mod/' + nombre + ".jsp");
    }
</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Navegación</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
                    </button> <span class="navbar-brand">Planificacion</span>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Menú<strong class="caret"></strong></a>
                            <ul class="dropdown-menu">
                                <li onclick="cargarModulo('proyecto');" >
                                    <a href="#">Proyectos</a>
                                </li>
                                <li onclick="cargarModulo('proyectoRecurso');">
                                    <a href="#">Asignaciones</a>
                                </li>
                                <li class="divider"></li>
                                <li class="dropdown-submenu">
                                    <a class="test" tabindex="-1" href="#">Mantenedores <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li onclick="cargarModulo('recurso');" ><a tabindex="-1" href="#">Recurso</a></li>
                                        <li onclick="cargarModulo('rol');" ><a tabindex="-1" href="#">Rol</a></li>
                                        <li onclick="cargarModulo('etapa');" ><a tabindex="-1" href="#">Etapa</a></li>
                                        <li onclick="cargarModulo('cliente');" ><a tabindex="-1" href="#">Cliente</a></li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    
                </div>
            </nav>
        </div>
    </div>
</div>
<br />
<div class="container-fluid" id="contenido"></div>
