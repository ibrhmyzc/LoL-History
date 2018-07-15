package sample;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MyPDFObject {
    Document doc = null;
    private List<Integer> matchIdList = null;
    private List<Integer> damageList = null;
    private List<String> champNameList = null;

    public MyPDFObject(List<Integer> matchIdList, List<Integer> damageList, List<String> champNameList){
        this.matchIdList = matchIdList;
        this.damageList = damageList;
        this.champNameList = champNameList;

        try{
            doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream("myPdf.pdf"));
            doc.open();
            addMetaData();
            addData();
            doc.close();
        }catch(Exception ex){
            System.out.println("Could not create pdf file");
        }
    }

    private void addData() throws DocumentException {
        PdfPTable table = new PdfPTable(3);

        PdfPCell c1 = new PdfPCell(new Phrase("MatchId"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Champion"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Damage"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        for(int i = 0; i < champNameList.size(); ++i){
            table.addCell(matchIdList.get(i).toString());
            table.addCell(champNameList.get(i).toString());
            table.addCell(damageList.get(i).toString());
        }
        doc.add(table);
    }

    private void addMetaData(){
        doc.addTitle("MATCH HISTORY REPORT");
        doc.addCreator("Ibrahim Yazici");
        doc.addAuthor("Ibrahim Yazici");
    }
}
