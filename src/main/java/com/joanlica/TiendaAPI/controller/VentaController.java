package com.joanlica.TiendaAPI.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ventas")
public class VentaController {
    /* 3.
     * CRUD de Venta
     * POST: /ventas/crear
     * GET: /ventas
     * GET: /ventas/{codigo_producto}
     * DELETE: /ventas/eliminar/{codigo_producto}
     * PUT /ventas/editar/{codigo_producto}
     * */

    /*No es necesario actualizar el stock de un producto( descontar)
    *  al realizar una venta, ni controlar si cuenta con cantidad para vender.
    * Se considerara como un plus para el punto 8.
    * */

    /*5. GET /ventas/productos/{codigo_venta}
    * lista productos de una venta
    * */
    /*6. GET /ventas/{fecha_venta}
    * sumatoria monto(euros) y cantidad total de ventas de un dia
    * */
    /* 7. GET /ventas/mayor_venta
    * {codigo_venta,total,cantidad de productos, nombre y apellido de cliente,
    *  de la mayor venta de todas.}
    * PATRON DTO.
    * */


    /* 8.
    * Propuesta de mejora, de end-point, agregado de clase.
    * Especificar en un doc requerimientos planteados y su especificacion tecnica,
    * */

}