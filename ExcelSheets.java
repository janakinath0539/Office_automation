import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) {

        try{
            FileInputStream file=new FileInputStream(new File("D:\\MARCH.xlsx"));
            XSSFWorkbook wb=new XSSFWorkbook(file);
            XSSFSheet st=wb.getSheetAt(0);
            int firstRowNum=st.getFirstRowNum();
            int lastRowNum=st.getLastRowNum();
            int temp=lastRowNum;
            st.removeRow(st.getRow(0));
            st.removeRow(st.getRow(1));
            st.createRow(1);

            //assigning the default values for removing things
            for(int k=firstRowNum+2;k<lastRowNum;k++) {
                Cell c=st.getRow(k).getCell(1);
                if (c == null || c.getCellType() == CellType.BLANK){
                    Cell c1=st.getRow(k).createCell(1);
                    c1.setCellValue("none");
                    if(st.getRow(k).getCell(0).toString().equalsIgnoreCase("Total Charges"))
                        continue;
                    st.getRow(k).getCell(2).setCellValue("0");
                }
            }

            //removing the blanks and subtotals and extracting the required data
           for(int i=firstRowNum+2;i<=lastRowNum-1;i++){
                if(st.getRow(i).getCell(0).toString().equalsIgnoreCase("Subtotal")) {
                    int j;
                    for (j = i; j <lastRowNum-1; j++) {
                        st.getRow(j).getCell(0).setCellValue(st.getRow(j+1).getCell(0).toString());
                        if(st.getRow(j).getCell(1).toString().equalsIgnoreCase("none")){
                            if(j+1==lastRowNum)
                                break;
                            st.getRow(j).getCell(1).setCellValue(st.getRow(j+1).getCell(1).toString());
                            st.getRow(j+1).getCell(1).setCellValue("none");
                            if(st.getRow(j).getCell(2).toString().equalsIgnoreCase("0")){
                                st.getRow(j).getCell(2).setCellValue(st.getRow(j+1).getCell(2).toString());
                                st.getRow(j+1).getCell(2).setCellValue("0");
                            }
                        }
                    }
                    st.getRow(j).getCell(0).setCellValue(" ");
                    lastRowNum--;
                }
            }

           //extracting the ORG number and formatting numbers
            for(int i=firstRowNum+2;i<lastRowNum;i++)
            {
                // cell 8
                Cell c8=st.getRow(i).createCell(8);
                st.getRow(i).getCell(8).setCellValue(st.getRow(i).getCell(0).toString());
                //cell 0
                String [] s=st.getRow(i).getCell(0).toString().split("-");
                st.getRow(i).getCell(0).setCellValue(s[0]);
                //cell 3
                Cell c4=st.getRow(i).createCell(3);
                st.getRow(i).getCell(3).setCellValue(st.getRow(i).getCell(2).toString());
                //cell 2
                //Cell c2=st.getRow(i).createCell(2);
                st.getRow(i).getCell(2).setCellValue("CELL");
                //cell 4
                Cell cc=st.getRow(i).createCell(4);
                st.getRow(i).getCell(4).setCellValue("08-02-2018");
                //cell 5
                Cell c3=st.getRow(i).createCell(5);
                st.getRow(i).getCell(5).setCellValue("1");
                //cell 6
                Cell c6=st.getRow(i).createCell(6);
                st.getRow(i).getCell(6).setCellValue("Cellular");
                //cell 9
                Cell c5=st.getRow(i).createCell(9);
                st.getRow(i).getCell(9).setCellValue("verizon Monthly Billing");
                //cell 7
                Cell c=st.getRow(i).createCell(7);
                String [] s1=st.getRow(i).getCell(1).toString().split(" /");
                st.getRow(i).getCell(7).setCellValue(s1[0].replaceAll("-",""));
                //cell 1
                //Cell c1=st.getRow(i).createCell(1);
                st.getRow(i).getCell(1).setCellValue("1");

            }

            //cleaning it up
            for(int i=lastRowNum-1;i<temp;i++){
                st.removeRow(st.getRow(i));

            }


            file.close();
            FileOutputStream outputStream = new FileOutputStream("D:\\MARCH.xlsx");
            wb.write(outputStream);
            outputStream.close();
            wb.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}