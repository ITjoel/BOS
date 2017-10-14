package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.service.base.SubAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${joel} on 2017/9/23 0023.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea> {

    @Autowired
    private SubAreaService subAreaService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private FixedAreaService fixedAreaService;

    //文件上传提供file/fileFileName/fileContextTypt
    private File file;
    private String fileFileName;

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    @Action(value = "subarea_batchImport", results = {@Result(name = "success", type = "json")})
    public String batchImport() throws FileNotFoundException {
        //判断是否是以.xls/.xlsx结尾文件
        if (StringUtils.isNoneBlank(fileFileName) || fileFileName.endsWith(".xls") || fileFileName.endsWith(".xlsx")) {
            List<SubArea> list = new ArrayList<SubArea>();
            //解析.xls/.xlsx
            try {
                Workbook workbook = null;
                //创建List封装subArea
                if (fileFileName.endsWith(".xls")) {
                    workbook = new HSSFWorkbook(new FileInputStream(file));
                } else if (fileFileName.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(new FileInputStream(file));
                }
                //解析workbook中的sheet
                Sheet sheet = workbook.getSheetAt(0);
                //获得sheet中的每一行
                for (Row rows : sheet) {
                    //跳过第一行
                    if (rows.getRowNum() == 0) {
                        continue;
                    }
                    //跳过空行
                    if (rows.getCell(0) == null || StringUtils.isBlank(rows.getCell(0).getStringCellValue())) {
                        continue;
                    }
                    SubArea subArea = new SubArea();
                    subArea.setId(rows.getCell(0).getStringCellValue());
                    subArea.setKeyWords(rows.getCell(4).getStringCellValue());
                    subArea.setStartNum(rows.getCell(5).getStringCellValue());
                    subArea.setEndNum(rows.getCell(6).getStringCellValue());
                    subArea.setSingle(rows.getCell(7).getStringCellValue().charAt(0));
                    subArea.setAssistKeyWords(rows.getCell(8).getStringCellValue());
                    String area_id = rows.getCell(9).getStringCellValue();
                    Area area = areaService.findOne(area_id);
                    subArea.setArea(area);
                    String fixedArea_id = rows.getCell(10).getStringCellValue();
                    FixedArea fixedArea = fixedAreaService.findOne(fixedArea_id);
                    subArea.setFixedArea(fixedArea);
                    list.add(subArea);
                }
                subAreaService.saveSubAreaBach(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("别瞎搞!!");
        }
        return NONE;
    }


    @Action(value = "subarea_download")
    public String download() {
        //调用业务层查询出所有数据
        List<SubArea> list = subAreaService.findAll();
        //封装到.xls表格中
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet();
        HSSFRow rows = sheet.createRow(0);
        rows.createCell(0).setCellValue("分区编号");
        rows.createCell(1).setCellValue("关键字");
        rows.createCell(2).setCellValue("起始号");
        rows.createCell(3).setCellValue("终止号");
        rows.createCell(4).setCellValue("单双号");
        rows.createCell(5).setCellValue("辅助关键字");

        for (int i = 0; i < list.size(); i++) {
            HSSFRow row_data = sheet.createRow(i + 1);
            row_data.createCell(0).setCellValue(list.get(i).getId());
            row_data.createCell(1).setCellValue(list.get(i).getKeyWords());
            row_data.createCell(2).setCellValue(list.get(i).getStartNum());
            row_data.createCell(3).setCellValue(list.get(i).getEndNum());
            row_data.createCell(4).setCellValue(list.get(i).getSingle());
            row_data.createCell(5).setCellValue(list.get(i).getAssistKeyWords());
        }

        //设置两个头一个流写出
        HttpServletResponse response = ServletActionContext.getResponse();
        String fileName = ".xls";
        response.setContentType(ServletActionContext.getServletContext().getMimeType(fileName));
        response.setHeader("Content-Disposition", "attachement;filename=" + fileName);
        try {
            hssfWorkbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NONE;
    }

    @Action(value = "subfixed_save", results = {@Result(name = "success",
            type = "redirect", location = "./pages/base/sub_area.html")})
    public String save() {
        //调用业务层
        subAreaService.save(model);
        return SUCCESS;
    }

    private String province;
    private String city;
    private String district;

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Action(value = "subarea_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        System.out.println("1111111111111111111");
        Area area = new Area();
        area.setProvince(province);
        area.setCity(city);
        area.setDistrict(district);
        model.setArea(area);
        System.out.println(model);

        Pageable pageable = new PageRequest(page - 1, rows);
        Page<SubArea> page = subAreaService.pageQuery(model, pageable);
        System.out.println(page);
        pushPageDatakToValueStack(page);
        return SUCCESS;
    }
}
