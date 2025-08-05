package org.superservice.superservice.utilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.superservice.superservice.DTOs.RepuestoRetiradoReporteDTO;
import org.superservice.superservice.utilities.alertas.Alertas;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class GeneradorReportes {

    public static void repuestosMasRetiradosEnMes(File file, List<RepuestoRetiradoReporteDTO> listaMasRetiradosDTO,
                                                  LocalDate fechaMin, LocalDate fechaMax) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        long max = 1; // Para calcular el valor máximo

        for (RepuestoRetiradoReporteDTO dto : listaMasRetiradosDTO) {
            long cantidad = dto.cantidad();
            String etiqueta = dto.detalle() + " - " + dto.marca();
            dataset.setValue(cantidad, "masRetirados", etiqueta);
            if (cantidad > max) {
                max = cantidad;
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "LOS " + listaMasRetiradosDTO.size() + " REPUESTOS MÁS RETIRADOS ENTRE: " + fechaMin + " y " + fechaMax,
                "REPUESTOS",
                "VECES RETIRADO",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                true
        );

        // Configurar el eje Y
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        int tickUnit = calcularTickUnit(max);
        double upperBound = Math.ceil((double) max / tickUnit) * tickUnit;

        rangeAxis.setRange(0, upperBound);
        rangeAxis.setTickUnit(new NumberTickUnit(tickUnit));
        rangeAxis.setNumberFormatOverride(NumberFormat.getIntegerInstance());

        // Configurar renderizado de las barras
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelsVisible(true);

        // Formato para etiquetas
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", numberFormat));

        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER)
        );

        try {
            ChartUtils.saveChartAsJPEG(file, chart, 1200, 700);
            Alertas.exito("Reporte", "Se generado con éxito el reporte en:\n" +
                    file);
        } catch (IOException ex) {
            ex.printStackTrace();
            Alertas.error("Emisión nota de retiro", ex.getMessage());
        }
    }

    public static void totalVentasAnual(File file, Map<String, BigDecimal> valores) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, BigDecimal> entry : valores.entrySet()) {
            String mes = entry.getKey();
            BigDecimal totalVentas = entry.getValue();
            dataset.setValue(totalVentas, "totalVentas", mes);
        }

        //TO-DO: HACER Q TOME EL AÑO CORRESPONDIENTE AL REPORTE
        JFreeChart chart = ChartFactory.createBarChart(
                "TOTAL DE VENTAS AÑO: 2025",
                null, // eje X
                "$", // eje Y
                dataset,
                PlotOrientation.VERTICAL,
                false, // leyenda
                false,
                true
        );

        // Configurar renderizado para mostrar montos en las barras
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Mostrar etiquetas con los valores
        renderer.setDefaultItemLabelsVisible(true);

        // Formato de número (ej. 12,000)
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", numberFormat));

        // Posición de las etiquetas (encima de la barra)
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER)
        );

        try {
            ChartUtils.saveChartAsJPEG(file, chart, 1200, 700);
            Alertas.exito("Reporte", "Se generado con éxito el reporte en:\n" +
                    file);
        } catch (IOException ex) {
            ex.printStackTrace();
            Alertas.error("Emisión nota de retiro", ex.getMessage());
        }
    }

    public static void cantidadVentasAnual(File file, Map<String, Long> valores) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Long> entry : valores.entrySet()) {
            String mes = entry.getKey();
            Long cantidadDeVentas = entry.getValue();
            dataset.setValue(cantidadDeVentas, "CantidadVentas", mes);
        }

        //TO-DO: HACER Q TOME EL AÑO CORRESPONDIENTE AL REPORTE
        JFreeChart chart = ChartFactory.createBarChart(
                "CANTIDAD DE VENTAS AÑO: 2025",
                null, // eje X
                "N° de Ventas", // eje Y
                dataset,
                PlotOrientation.VERTICAL,
                false, // leyenda
                false,
                true
        );

        CategoryPlot plot = chart.getCategoryPlot();

        // Configurar eje Y con rango dinámico
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        long max = valores.values().stream().mapToLong(Long::longValue).max().orElse(1);
        int tickUnit = calcularTickUnit(max);
        double upperBound = Math.ceil((double) max / tickUnit) * tickUnit;

        rangeAxis.setRange(0, upperBound);
        rangeAxis.setTickUnit(new NumberTickUnit(tickUnit));
        rangeAxis.setNumberFormatOverride(NumberFormat.getIntegerInstance());

        // Mostrar etiquetas encima de las barras
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", NumberFormat.getIntegerInstance()));
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER)
        );

        try {
            ChartUtils.saveChartAsJPEG(file, chart, 1200, 700);
            Alertas.exito("Reporte", "Se generado con éxito el reporte en:\n" +
                    file);
        } catch (IOException ex) {
            ex.printStackTrace();
            Alertas.error("Emisión nota de retiro", ex.getMessage());
        }
    }

    // auxiliar para calcular el TickUnit óptimo
    private static int calcularTickUnit(long max) {
        if (max <= 10) {
            return 1;
        }
        if (max <= 50) {
            return 5;
        }
        if (max <= 100) {
            return 10;
        }
        if (max <= 500) {
            return 50;
        }
        if (max <= 1000) {
            return 100;
        }
        return 500;
    }

}
