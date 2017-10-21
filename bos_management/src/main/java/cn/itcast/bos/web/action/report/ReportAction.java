package cn.itcast.bos.web.action.report;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.web.action.common.BaseAction;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

// 导出运单 报表 
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ReportAction extends BaseAction<WayBill> {

    @Autowired
    private WayBillService wayBillService;

    /**
     * @return
     * @throws IOException
     */
    @Action("report_exportXls")
    public String exportXls() throws IOException {
        // 查询出 满足当前条件 结果数据
        List<WayBill> wayBills = wayBillService.findWayBills(model);

        //解决表格数据超出65535问题,分sheet处理
        int totle = wayBills.size();//获取list集合的size
        int count = 2;//每个工作表格最多存储5000条数据
        int avg = totle / count;//数据是5000的倍数,需要avg 个sheet,假如数据不是5000的倍数,需要avg+1个sheet

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        for (int i = 0; i < avg + 1; i++) {
            // 生成Excel文件
            HSSFSheet sheet = hssfWorkbook.createSheet("运单数据" + i);
            // 表头
            HSSFRow headRow = sheet.createRow(0);
            headRow.createCell(0).setCellValue("运单号");
            headRow.createCell(1).setCellValue("寄件人");
            headRow.createCell(2).setCellValue("寄件人电话");
            headRow.createCell(3).setCellValue("寄件人地址");
            headRow.createCell(4).setCellValue("收件人");
            headRow.createCell(5).setCellValue("收件人电话");
            headRow.createCell(6).setCellValue("收件人地址");
            //第几个表格  第几条数据跳出循环
            //0             4999
            //1             9999
            //n             i*count
            int num = i * count;//第几个表格
            int index = 0;//标记 用做判断是否每个表格数据达到规定的5000条
            for (int j = num; j < totle; j++) {
                if (index == count) {
                    break;//判断index == count 的时候跳出当前for循环
                }
                // 表格数据
                WayBill wayBill = wayBills.get(j);
                HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
                dataRow.createCell(1).setCellValue(wayBill.getSendName());
                dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
                dataRow.createCell(3).setCellValue(wayBill.getSendAddress());
                dataRow.createCell(4).setCellValue(wayBill.getRecName());
                dataRow.createCell(5).setCellValue(wayBill.getRecMobile());
                dataRow.createCell(6).setCellValue(wayBill.getRecAddress());
                index++;
            }
        }


        // 下载导出
        // 设置头信息
        ServletActionContext.getResponse().setContentType(
                "application/vnd.ms-excel");
        String filename = "运单数据.xls";
        String agent = ServletActionContext.getRequest()
                .getHeader("user-agent");
        filename = FileUtils.encodeDownloadFilename(filename, agent);
        ServletActionContext.getResponse().setHeader("Content-Disposition",
                "attachment;filename=" + filename);

        ServletOutputStream outputStream = ServletActionContext.getResponse()
                .getOutputStream();
        hssfWorkbook.write(outputStream);

        // 关闭
        hssfWorkbook.close();

        return NONE;
    }

    @Action("report_exportPdf")
    public String exportPdf() throws IOException, DocumentException {
        // 查询出 满足当前条件 结果数据
        List<WayBill> wayBills = wayBillService.findWayBills(model);

        // 下载导出
        // 设置头信息
        ServletActionContext.getResponse().setContentType("application/pdf");
        String filename = "运单数据.pdf";
        String agent = ServletActionContext.getRequest()
                .getHeader("user-agent");
        filename = FileUtils.encodeDownloadFilename(filename, agent);
        ServletActionContext.getResponse().setHeader("Content-Disposition",
                "attachment;filename=" + filename);

        // 生成PDF文件
        Document document = new Document();
        PdfWriter.getInstance(document, ServletActionContext.getResponse()
                .getOutputStream());
        document.open();
        // 写PDF数据
        // 向document 生成pdf表格
        Table table = new Table(7);
        table.setWidth(80); // 宽度
        table.setBorder(1); // 边框
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式

        // 设置表格字体
        BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
                false);
        Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);

        // 写表头
        table.addCell(buildCell("运单号", font));
        table.addCell(buildCell("寄件人", font));
        table.addCell(buildCell("寄件人电话", font));
        table.addCell(buildCell("寄件人地址", font));
        table.addCell(buildCell("收件人", font));
        table.addCell(buildCell("收件人电话", font));
        table.addCell(buildCell("收件人地址", font));
        // 写数据
        for (WayBill wayBill : wayBills) {
            table.addCell(buildCell(wayBill.getWayBillNum(), font));
            table.addCell(buildCell(wayBill.getSendName(), font));
            table.addCell(buildCell(wayBill.getSendMobile(), font));
            table.addCell(buildCell(wayBill.getSendAddress(), font));
            table.addCell(buildCell(wayBill.getRecName(), font));
            table.addCell(buildCell(wayBill.getRecMobile(), font));
            table.addCell(buildCell(wayBill.getRecAddress(), font));
        }
        // 将表格加入文档
        document.add(table);

        document.close();

        return NONE;
    }

    private Cell buildCell(String content, Font font)
            throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }

    @Action("report_exportJasperPdf")
    public String exportJasperPdf() throws IOException, DocumentException,
            JRException, SQLException {
        // 查询出 满足当前条件 结果数据
        List<WayBill> wayBills = wayBillService.findWayBills(model);

        // 下载导出
        // 设置头信息
        ServletActionContext.getResponse().setContentType("application/pdf");
        String filename = "运单数据.pdf";
        String agent = ServletActionContext.getRequest()
                .getHeader("user-agent");
        filename = FileUtils.encodeDownloadFilename(filename, agent);
        ServletActionContext.getResponse().setHeader("Content-Disposition",
                "attachment;filename=" + filename);

        // 根据 jasperReport模板 生成pdf
        // 读取模板文件
        String jrxml = ServletActionContext.getServletContext().getRealPath(
                "/jasper/waybill.jrxml");
        JasperReport report = JasperCompileManager.compileReport(jrxml);

        // 设置模板数据
        // Parameter变量
        Map<String, Object> paramerters = new HashMap<String, Object>();
        paramerters.put("company", "传智播客");
        // Field变量
        JasperPrint jasperPrint = JasperFillManager.fillReport(report,
                paramerters, new JRBeanCollectionDataSource(wayBills));
        System.out.println(wayBills);
        // 生成PDF客户端
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                ServletActionContext.getResponse().getOutputStream());
        exporter.exportReport();// 导出
        ServletActionContext.getResponse().getOutputStream().close();

        return NONE;
    }


}
