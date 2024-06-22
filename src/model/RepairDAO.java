/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import javax.swing.filechooser.FileSystemView;
import java.awt.Desktop;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.classes.Repair;

/**
 *
 * @author Antonio
 */
public class RepairDAO {

    SQLConnection cn = new SQLConnection();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean insertRepair(Repair repair) {
        String sql = "INSERT INTO reparaciones (id_dispositivo, servicio, costo, fecha_recepcion, fecha_entrega) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, repair.getIdDevice());
            ps.setString(2, repair.getService());
            ps.setDouble(3, repair.getPrice());
            ps.setDate(4, new java.sql.Date(repair.getReceivedDate().getTime()));
            ps.setDate(5, null);
//            ps.setString(6, repair.getState().toString());
//            ps.setString(7, repair.getPaymentState().toString());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public List<Repair> selectAllRepairs() {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT r.id_reparacion, r.id_dispositivo, td.tipo AS dispositivo, d.problema, r.servicio, r.costo, r.fecha_recepcion, r.fecha_entrega, r.estado, r.estado_pago, "
                + "COALESCE((SELECT SUM(monto) FROM pagos WHERE pagos.id_reparacion = r.id_reparacion), 0) AS abonado "
                + "FROM reparaciones r "
                + "JOIN dispositivos d ON r.id_dispositivo = d.id_dispositivo "
                + "JOIN tipos_dispositivos td ON d.id_tipo_dispositivo = td.id_tipo_dispositivo";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Repair repair = new Repair();
                repair.setId(rs.getInt("id_reparacion"));
                repair.setIdDevice(rs.getInt("id_dispositivo"));
                repair.setDevice(rs.getString("dispositivo"));
                repair.setProblem(rs.getString("problema"));
                repair.setService(rs.getString("servicio"));
                repair.setPrice(rs.getDouble("costo"));
                repair.setAbonado(rs.getDouble("abonado"));
                repair.setReceivedDate(rs.getDate("fecha_recepcion"));
                repair.setDeliveredDate(rs.getDate("fecha_entrega"));
                repair.setState(Repair.Estado.fromString(rs.getString("estado")));
                repair.setPaymentState(Repair.EstadoPago.fromString(rs.getString("estado_pago")));
                repairs.add(repair);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        return repairs;
    }

    public List<Repair> selectRepairsByStatus(String estado) {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT r.id_reparacion, r.id_dispositivo, td.tipo AS dispositivo, d.problema, r.servicio, r.costo, "
                + "r.fecha_recepcion, r.fecha_entrega, r.estado, r.estado_pago, "
                + "COALESCE((SELECT SUM(monto) FROM pagos WHERE pagos.id_reparacion = r.id_reparacion), 0) AS abonado "
                + "FROM reparaciones r "
                + "JOIN dispositivos d ON r.id_dispositivo = d.id_dispositivo "
                + "JOIN tipos_dispositivos td ON d.id_tipo_dispositivo = td.id_tipo_dispositivo "
                + "WHERE r.estado = ?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado);
            rs = ps.executeQuery();
            while (rs.next()) {
                Repair repair = new Repair();
                repair.setId(rs.getInt("id_reparacion"));
                repair.setIdDevice(rs.getInt("id_dispositivo"));
                repair.setDevice(rs.getString("dispositivo"));
                repair.setProblem(rs.getString("problema"));
                repair.setService(rs.getString("servicio"));
                repair.setPrice(rs.getDouble("costo"));
                repair.setAbonado(rs.getDouble("abonado")); // Asigna el valor de abonado
                repair.setReceivedDate(rs.getDate("fecha_recepcion"));
                repair.setDeliveredDate(rs.getDate("fecha_entrega"));
                repair.setState(Repair.Estado.fromString(rs.getString("estado")));
                repair.setPaymentState(Repair.EstadoPago.fromString(rs.getString("estado_pago")));
                repairs.add(repair);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        return repairs;
    }

    public List<String> getRepairStates() {
        List<String> states = new ArrayList<>();
        String sql = "SHOW COLUMNS FROM reparaciones LIKE 'estado'";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String columnType = rs.getString("Type"); // Obtenemos el tipo de la columna
                String enumValues = columnType.replaceAll("enum\\(|\\)", ""); // Eliminamos "enum(" y ")"
                String[] values = enumValues.split(","); // Separamos los valores

                for (String value : values) {
                    states.add(value.replace("'", "")); // Eliminamos las comillas simples
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return states;
    }

    public List<String> getRepairPaymentStates() {
        List<String> paymentStates = new ArrayList<>();
        String sql = "SHOW COLUMNS FROM reparaciones LIKE 'estado_pago'";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                String columnType = rs.getString("Type"); // Obtenemos el tipo de la columna
                String enumValues = columnType.replaceAll("enum\\(|\\)", ""); // Eliminamos "enum(" y ")"
                String[] values = enumValues.split(","); // Separamos los valores

                for (String value : values) {
                    paymentStates.add(value.replace("'", "")); // Eliminamos las comillas simples
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentStates;
    }

    public boolean updateRepair(Repair repair) {
        String sql = "UPDATE reparaciones SET id_dispositivo = ?, servicio = ?, costo = ?, fecha_recepcion = ?, fecha_entrega = ?, estado = ?, estado_pago = ? WHERE id_reparacion = ?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, repair.getIdDevice());
            ps.setString(2, repair.getService());
            ps.setDouble(3, repair.getPrice());
            ps.setDate(4, new java.sql.Date(repair.getReceivedDate().getTime()));
            ps.setDate(5, repair.getDeliveredDate() != null ? new java.sql.Date(repair.getDeliveredDate().getTime()) : null);
            ps.setString(6, repair.getState().toString());
            ps.setString(7, repair.getPaymentState().toString());
            ps.setInt(8, repair.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRepair(int id) {
        String sqlDeletePayments = "DELETE FROM pagos WHERE id_reparacion = ?";
        String sqlDeleteRepair = "DELETE FROM reparaciones WHERE id_reparacion = ?";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = cn.getConnectDB();
            con.setAutoCommit(false); // Desactiva el auto-commit para iniciar la transacción

            // Eliminar pagos relacionados
            ps = con.prepareStatement(sqlDeletePayments);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            // Eliminar reparación
            ps = con.prepareStatement(sqlDeleteRepair);
            ps.setInt(1, id);
            ps.executeUpdate();

            con.commit(); // Realiza el commit de la transacción
            return true;
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback(); // Realiza el rollback en caso de error
                }
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.toString());
            }
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.toString());
            }
        }
    }

    public Repair searchRepair(int id) {
        Repair rep = new Repair();
        String sql = "SELECT r.id_reparacion, r.id_dispositivo, td.tipo AS dispositivo, d.problema, r.servicio, r.costo, "
                + "r.fecha_recepcion, r.fecha_entrega, r.estado, r.estado_pago, c.nombre AS nombre_cliente, "
                + "COALESCE(SUM(p.monto), 0) AS abonado "
                + "FROM reparaciones r "
                + "JOIN dispositivos d ON r.id_dispositivo = d.id_dispositivo "
                + "JOIN tipos_dispositivos td ON d.id_tipo_dispositivo = td.id_tipo_dispositivo "
                + "JOIN clientes c ON d.id_cliente = c.id_cliente "
                + "LEFT JOIN pagos p ON r.id_reparacion = p.id_reparacion "
                + "WHERE r.id_reparacion = ? ";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                rep.setId(rs.getInt("id_reparacion"));
                rep.setIdDevice(rs.getInt("id_dispositivo"));
                rep.setClient(rs.getString("nombre_cliente"));
                rep.setDevice(rs.getString("dispositivo"));
                rep.setProblem(rs.getString("problema"));
                rep.setService(rs.getString("servicio"));
                rep.setPrice(rs.getDouble("costo"));
                rep.setAbonado(rs.getDouble("abonado"));
                rep.setReceivedDate(rs.getDate("fecha_recepcion"));
                rep.setDeliveredDate(rs.getDate("fecha_entrega"));

                String estado = rs.getString("estado");
                if (estado != null) {
                    rep.setState(Repair.Estado.valueOf(estado.toUpperCase().replace(" ", "_")));
                }

                String estadoPago = rs.getString("estado_pago");
                if (estadoPago != null) {
                    rep.setPaymentState(Repair.EstadoPago.valueOf(estadoPago.toUpperCase().replace(" ", "_")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return rep;
    }

    public int getDeviceIdByRepairId(int idRepair) {
        int deviceId = -1;
        String query = "SELECT id_dispositivo FROM reparaciones WHERE id_reparacion = ?";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(query);
            ps.setInt(1, idRepair);
            rs = ps.executeQuery();

            if (rs.next()) {
                deviceId = rs.getInt("id_dispositivo");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el ID del dispositivo: " + e.toString());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión: " + ex.toString());
            }
        }
        return deviceId;
    }

    public void pdfRepairDetails(int repairId, int clientId, String usuario) {
        try {
            Date date = new Date();
            FileOutputStream archivo;
            String url = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
            File salida = new File(url + "/reparacion_" + repairId + ".pdf");
            archivo = new FileOutputStream(salida);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();

            // logo
            Image img = Image.getInstance(getClass().getResource("/resources/logo-128px.png"));
            img.scaleToFit(100, 100);
            img.setAlignment(Element.ALIGN_LEFT);
//            doc.add(img);

            // Crear tabla para los datos del vendedor y la empresa
            PdfPTable headerTable = new PdfPTable(4);
            headerTable.setWidthPercentage(100);
            headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            float[] columnWidthsEncabezado = new float[]{20f, 30f, 70f, 40f};
            headerTable.setWidths(columnWidthsEncabezado);
            headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerTable.addCell(img);
            headerTable.addCell("");

            // Obtener información de la configuración de la empresa
            String configQuery = "SELECT nombre, telefono, direccion, mensaje FROM Config LIMIT 1";
            String empresaNombre = "", empresaTelefono = "", empresaDireccion = "", empresaMensaje = "";

            try {
                con = cn.getConnectDB();
                ps = con.prepareStatement(configQuery);
                rs = ps.executeQuery();
                if (rs.next()) {
                    empresaNombre = rs.getString("nombre");
                    empresaTelefono = rs.getString("telefono");
                    empresaDireccion = rs.getString("direccion");
                    empresaMensaje = rs.getString("mensaje");
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener la configuración de la empresa: " + e.toString());
            }

            // Datos de la empresa
            Paragraph empresaInfo = new Paragraph();
            empresaInfo.add("Nombre: " + empresaNombre + "\n");
            empresaInfo.add("Teléfono: " + empresaTelefono + "\n");
            empresaInfo.add("Dirección: " + empresaDireccion + "\n\n");
            PdfPCell empresaCell = new PdfPCell(empresaInfo);
            empresaCell.setBorder(Rectangle.NO_BORDER);
            empresaCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerTable.addCell(empresaCell);

            // Datos del usuario
            Paragraph vendedorInfo = new Paragraph();
            vendedorInfo.add("Usuario: " + usuario + "\n");
            vendedorInfo.add("Folio de Reparación: " + repairId + "\n");
            vendedorInfo.add("Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(date) + "\n\n");
            PdfPCell vendedorCell = new PdfPCell(vendedorInfo);
            vendedorCell.setBorder(Rectangle.NO_BORDER);
            vendedorCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerTable.addCell(vendedorCell);

            doc.add(headerTable);

            // Información del cliente
            Paragraph clienteInfo = new Paragraph("DATOS DEL CLIENTE\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE));
            doc.add(clienteInfo);

            PdfPTable clienteTabla = new PdfPTable(3);
            clienteTabla.setWidthPercentage(100);
            float[] columnWidthsCliente = new float[]{50f, 25f, 25f};
            clienteTabla.setWidths(columnWidthsCliente);
            clienteTabla.setHorizontalAlignment(Element.ALIGN_LEFT);

            // Encabezados de cliente
            Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(255, 140, 0)); // Naranja suave
            PdfPCell cellClienteHeader = new PdfPCell(new Phrase("Nombre", headerFont));
            cellClienteHeader.setBorder(Rectangle.BOX);
            cellClienteHeader.setBackgroundColor(new BaseColor(255, 228, 181)); // Naranja claro
            clienteTabla.addCell(cellClienteHeader);

            cellClienteHeader = new PdfPCell(new Phrase("Teléfono", headerFont));
            cellClienteHeader.setBorder(Rectangle.BOX);
            cellClienteHeader.setBackgroundColor(new BaseColor(255, 228, 181));
            clienteTabla.addCell(cellClienteHeader);

            cellClienteHeader = new PdfPCell(new Phrase("Dirección", headerFont));
            cellClienteHeader.setBorder(Rectangle.BOX);
            cellClienteHeader.setBackgroundColor(new BaseColor(255, 228, 181));
            clienteTabla.addCell(cellClienteHeader);

            String clienteQuery = "SELECT nombre, telefono, direccion FROM clientes WHERE id_cliente = ?";
            try {
                ps = con.prepareStatement(clienteQuery);
                ps.setInt(1, clientId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    clienteTabla.addCell(rs.getString("nombre"));
                    clienteTabla.addCell(rs.getString("telefono"));
                    clienteTabla.addCell(rs.getString("direccion") + "\n\n");
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            doc.add(clienteTabla);

            // Información de la reparación
            Paragraph reparacionInfo = new Paragraph("DETALLES DE LA REPARACIÓN\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE));
            doc.add(reparacionInfo);

            String reparacionQuery = "SELECT r.id_reparacion, td.tipo AS dispositivo, d.problema, r.servicio, r.costo, "
                    + "r.fecha_recepcion, r.fecha_entrega, r.estado, r.estado_pago "
                    + "FROM reparaciones r "
                    + "JOIN dispositivos d ON r.id_dispositivo = d.id_dispositivo "
                    + "JOIN tipos_dispositivos td ON d.id_tipo_dispositivo = td.id_tipo_dispositivo "
                    + "WHERE r.id_reparacion = ?";

            PdfPTable reparacionTabla = new PdfPTable(2);
            reparacionTabla.setWidthPercentage(100);
            float[] columnWidthsReparacion = new float[]{30f, 70f};
            reparacionTabla.setWidths(columnWidthsReparacion);
            reparacionTabla.setHorizontalAlignment(Element.ALIGN_LEFT);

            try {
                ps = con.prepareStatement(reparacionQuery);
                ps.setInt(1, repairId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    PdfPCell cellHeader;

                    cellHeader = new PdfPCell(new Phrase("Dispositivo", headerFont));
                    cellHeader.setBorder(Rectangle.BOX);
                    cellHeader.setBackgroundColor(new BaseColor(255, 228, 181));
                    reparacionTabla.addCell(cellHeader);
                    reparacionTabla.addCell(rs.getString("dispositivo"));

                    cellHeader = new PdfPCell(new Phrase("Problema", headerFont));
                    cellHeader.setBorder(Rectangle.BOX);
                    cellHeader.setBackgroundColor(new BaseColor(255, 228, 181));
                    reparacionTabla.addCell(cellHeader);
                    reparacionTabla.addCell(rs.getString("problema"));

                    cellHeader = new PdfPCell(new Phrase("Servicio", headerFont));
                    cellHeader.setBorder(Rectangle.BOX);
                    cellHeader.setBackgroundColor(new BaseColor(255, 228, 181));
                    reparacionTabla.addCell(cellHeader);
                    reparacionTabla.addCell(rs.getString("servicio"));

                    cellHeader = new PdfPCell(new Phrase("Costo", headerFont));
                    cellHeader.setBorder(Rectangle.BOX);
                    cellHeader.setBackgroundColor(new BaseColor(255, 228, 181));
                    reparacionTabla.addCell(cellHeader);
                    reparacionTabla.addCell("$" + rs.getDouble("costo"));

                    cellHeader = new PdfPCell(new Phrase("Fecha Recepción", headerFont));
                    cellHeader.setBorder(Rectangle.BOX);
                    cellHeader.setBackgroundColor(new BaseColor(255, 228, 181));
                    reparacionTabla.addCell(cellHeader);
                    reparacionTabla.addCell(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("fecha_recepcion")));

                    cellHeader = new PdfPCell(new Phrase("Fecha Entrega", headerFont));
                    cellHeader.setBorder(Rectangle.BOX);
                    cellHeader.setBackgroundColor(new BaseColor(255, 228, 181));
                    reparacionTabla.addCell(cellHeader);
                    Date fechaEntrega = rs.getDate("fecha_entrega");
                    if (fechaEntrega != null) {
                        reparacionTabla.addCell(new SimpleDateFormat("dd/MM/yyyy").format(fechaEntrega));
                    } else {
                        reparacionTabla.addCell("No especificada");
                    }

                    cellHeader = new PdfPCell(new Phrase("Estado", headerFont));
                    cellHeader.setBorder(Rectangle.BOX);
                    cellHeader.setBackgroundColor(new BaseColor(255, 228, 181));
                    reparacionTabla.addCell(cellHeader);
                    reparacionTabla.addCell(rs.getString("estado"));

                    cellHeader = new PdfPCell(new Phrase("Estado Pago", headerFont));
                    cellHeader.setBorder(Rectangle.BOX);
                    cellHeader.setBackgroundColor(new BaseColor(255, 228, 181));
                    reparacionTabla.addCell(cellHeader);
                    reparacionTabla.addCell(rs.getString("estado_pago"));
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            doc.add(reparacionTabla);

            // Tabla de pagos
            Paragraph pagosInfo = new Paragraph("HISTORIAL DE PAGOS\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE));
            doc.add(pagosInfo);

            PdfPTable pagosTabla = new PdfPTable(3);
            pagosTabla.setWidthPercentage(100);
            float[] columnWidthsPagos = new float[]{30f, 40f, 30f};
            pagosTabla.setWidths(columnWidthsPagos);
            pagosTabla.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cellPagosHeader;

            cellPagosHeader = new PdfPCell(new Phrase("Fecha de Pago", headerFont));
            cellPagosHeader.setBorder(Rectangle.BOX);
            cellPagosHeader.setBackgroundColor(new BaseColor(255, 228, 181));
            pagosTabla.addCell(cellPagosHeader);

            cellPagosHeader = new PdfPCell(new Phrase("Método de Pago", headerFont));
            cellPagosHeader.setBorder(Rectangle.BOX);
            cellPagosHeader.setBackgroundColor(new BaseColor(255, 228, 181));
            pagosTabla.addCell(cellPagosHeader);

            cellPagosHeader = new PdfPCell(new Phrase("Monto", headerFont));
            cellPagosHeader.setBorder(Rectangle.BOX);
            cellPagosHeader.setBackgroundColor(new BaseColor(255, 228, 181));
            pagosTabla.addCell(cellPagosHeader);

            double totalPagado = 0.0;

            String pagosQuery = "SELECT fecha_pago, metodo_pago, monto FROM pagos WHERE id_reparacion = ?";
            try {
                ps = con.prepareStatement(pagosQuery);
                ps.setInt(1, repairId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    pagosTabla.addCell(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("fecha_pago")));
                    pagosTabla.addCell(rs.getString("metodo_pago"));
                    double monto = rs.getDouble("monto");
                    pagosTabla.addCell("$" + monto);
                    totalPagado += monto;
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            doc.add(pagosTabla);

            // Mostrar el total pagado
            Paragraph totalPagadoInfo = new Paragraph();
            totalPagadoInfo.add(Chunk.NEWLINE);
            totalPagadoInfo.add(new Phrase("Total Pagado: $" + totalPagado, new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.RED)));
            totalPagadoInfo.setAlignment(Element.ALIGN_RIGHT);
            doc.add(totalPagadoInfo);

            // Mensaje de agradecimiento al final del documento
            Paragraph agradecimiento = new Paragraph();
            agradecimiento.add(Chunk.NEWLINE);
            agradecimiento.add("Gracias por su preferencia\n");
            agradecimiento.setAlignment(Element.ALIGN_CENTER);
            doc.add(agradecimiento);

            doc.close();
            archivo.close();
            Desktop.getDesktop().open(salida);
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }

}
