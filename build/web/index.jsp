<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="includes.jsp" %>
        <title>Planificación</title>
    </head>
    <body id="main">
        <div id="modal-edicion" class="modal modal-lg fade text-center" role="dialog">
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
        <%@include file="main.jsp" %>
    </body>
</html>
