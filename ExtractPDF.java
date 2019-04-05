import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import sun.jvm.hotspot.debugger.Page;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] org_number=new String[200];
        String[] page_number=new String[200];
        int k=0;
        try {
            File file = new File("D:\\Verizon_extract.pdf");
            PDDocument doc = PDDocument.load(file);
            for (int i = 2; i < 18; i++) {
                PDPage page = doc.getPage(i);
                Rectangle rec;
                if (i == 2)
                    rec = new Rectangle(10, 280, 200, 230);
                else
                    rec = new Rectangle(10, 170, 200, 400);
                PDFTextStripperByArea st = new PDFTextStripperByArea();
                st.setSortByPosition(true);
                st.addRegion("class1", rec);
                st.extractRegions(page);
                FileWriter fout = new FileWriter("D:\\temp.txt", true);
                fout.write(st.getTextForRegion("class1"));
                fout.close();
            }
            File f = new File("D:\\temp.txt");
            FileReader in = new FileReader(f);
            BufferedReader br = new BufferedReader(in);
            FileWriter fp=new FileWriter("D:\\pages.txt");
            String s;
            while ((s = br.readLine()) != null) {
                if (s.length() == 13||s.length()==14||s.length()==15)
                    fp.write(s+"\n");
                else if (s.compareTo("Subtotal") == 0)
                    fp.write("");
                else if(s.length()==5) //s.compareTo("9393 ")==0
                    fp.write(s+"\n");
                else {
                    fp.write(s.charAt(s.length() - 3));
                    fp.write(s.charAt(s.length() - 2));
                    fp.write(s.charAt(s.length() - 1));
                    fp.write("\n");
                }
            }
                doc.close();
                fp.close();
                FileReader fr=new FileReader("D:\\pages.txt");
                BufferedReader buf=new BufferedReader(fr);
                FileWriter fw=new FileWriter("D:\\fp.txt");
                String K,start,end,org_num;
                while((K=buf.readLine())!=null){
                    if(K.length()>5){
                       fw.write(K);
                       fw.write("\n");
                       fw.write(buf.readLine());
                        fw.write("\n");
                    }
                    else if(K.matches("9393 ")){ //
                        fw.write(K);
                        fw.write("\n");
                        fw.write(buf.readLine());
                        fw.write("\n");
                        fw.write(buf.readLine());
                        fw.write("\n");
                    }
                    else
                        continue;
                }
                fw.close();
                fr.close();
        }
        catch(IOException e){
            System.out.println(e);
        }
        try{
            FileReader fr1=new FileReader("D:\\fp.txt");
            BufferedReader buf1=new BufferedReader(fr1);
            String temp;
            int i=1,j=0;
            while((temp=buf1.readLine())!=null){
                //System.out.println(temp);
                if(i%2!=0) {
                    org_number[j] = temp;
                    j++;
                }
                else {
                    page_number[k] = temp;
                    k++;
                }
                i++;
            }
            System.out.println("hello  "+j+"  "+k);
            for(int l=0;l<k;l++){ //73
                System.out.println("orgnumber :"+org_number[l]);
                if(org_number[l].compareTo("9393 ")==0) {   //
                    System.out.println(" has pages from :" + page_number[l] + "-" + org_number[l + 1]);
                    break;
                }
                System.out.printf(" has pages from :%d-%d",Integer.valueOf(page_number[l].trim()),Integer.valueOf(page_number[l + 1].trim())-1);
                System.out.println();
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
        try{

            PDDocument document = PDDocument.load(new File("D:\\Verizon_extract.pdf"));
            // Instantiating Splitter class
            Splitter splitter = new Splitter();
            splitter.setStartPage(Integer.valueOf(page_number[0].trim()));
            splitter.setEndPage(Integer.valueOf(org_number[k].trim())); //73
            // splitting the pages of a PDF document
            List<PDDocument> Pages = splitter.split(document);
            Iterator<PDDocument> iterator = Pages.listIterator();
            int i =Integer.valueOf(page_number[0].trim());
            while (iterator.hasNext()) {
                PDDocument pd = iterator.next();
                pd.save("D:\\extract\\split_" + i +".pdf");
                i++;
            }
            document.close();
            for(int a=0;a<k+1;a++){ //74
                PDDocument[] doc;
                PDFMergerUtility pdmer=new PDFMergerUtility();
                pdmer.setDestinationFileName("D:\\extract\\"+org_number[a]+" Verizon April 2019" +".pdf");
                if(org_number[a].compareTo("9393 ")==0){

                    for(int j=Integer.valueOf(page_number[a].trim());j<Integer.valueOf(org_number[a+1].trim());j++){
                        File f1=new File("D:\\extract\\split_"+j+".pdf");
                        File f2=new File("D:\\extract\\split_"+(j+1)+".pdf");
                        pdmer.addSource(f1);
                        pdmer.addSource(f2);
                        pdmer.mergeDocuments();
                    }

                    break;
                }
                if((Integer.valueOf(page_number[a+1].trim())-Integer.valueOf(page_number[a].trim()))==1)
                    doc=new PDDocument[1];
            doc =new PDDocument[Integer.valueOf(page_number[a+1].trim())-Integer.valueOf(page_number[a].trim())];

            if((Integer.valueOf(page_number[a+1].trim())-Integer.valueOf(page_number[a].trim()))==1){
                File f1=new File("D:\\extract\\split_"+Integer.valueOf(page_number[a].trim())+".pdf");
                pdmer.addSource(f1);
                pdmer.mergeDocuments();
            }
            for(int j=Integer.valueOf(page_number[a].trim());j<Integer.valueOf(page_number[a+1].trim())-1;j++){
                File f1=new File("D:\\extract\\split_"+j+".pdf");
                File f2=new File("D:\\extract\\split_"+(j+1)+".pdf");
                pdmer.addSource(f1);
                pdmer.addSource(f2);
                pdmer.mergeDocuments();
            }
        }
            for(int b=Integer.valueOf(page_number[0].trim());b<=Integer.valueOf(org_number[k].trim());b++)
                new File("D:\\extract\\split_" + b+".pdf").delete();
        }
        catch (IOException e){
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }
}