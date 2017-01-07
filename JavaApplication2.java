package javaapplication2;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JavaApplication2 {
    
    public static void main(String[] args) throws IOException {
        
         DatabaseConnections db=new DatabaseConnections();
         BufferedReader br=new BufferedReader(new FileReader("InterestingConcepts"));
         File file = new File("Links");
         file.createNewFile();
         BufferedWriter writer = new BufferedWriter(new FileWriter(file));
         //File file2 = new File("Results");
         //file2.createNewFile();
         //BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2));
         File file2 = new File("goodForms");
         file2.createNewFile();
         BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2));
         File file3 = new File("badForms");
         file3.createNewFile();
         BufferedWriter writer3 = new BufferedWriter(new FileWriter(file3));
         
         //Creating an array list of interesting concepts
         ArrayList concepts=new ArrayList();
         ArrayList formsList=new ArrayList();
         ArrayList badForms=new ArrayList();
         String line;
         while((line=br.readLine())!=null)
                concepts.add(line);
        
         
        //Creating an arraylist for links 
        ArrayList links=new ArrayList();
        int depth=0;
        Elements link;
        links.add("https://searchworks.stanford.edu/");
        links.add("Empty");
        Document doc;
        Document document;
        try{
        for(int i=0;i<links.size();i++)
        {
            if(depth<=3)
            { 
                if(links.get(i)=="Empty")
                { links.add("Empty");
                    depth++;
                    continue;
                }
                try{
                doc=Jsoup.connect((String)links.get(i)).get();
                link = doc.select("a[href]");
                //Put every link on that page in the list containing the links
		for(Element l: link){
			//System.out.println("link: " +l.attr("abs:href"));
                        if(!links.contains(l.attr("abs:href")))
                        {
                          writer.write(l.attr("abs:href")+"\n");
                          links.add(l.attr("abs:href"));
                        }
                }
                }
                catch(IOException e)
                {
                    continue;
                }
                //Search for form in every link
                Elements forms = doc.getElementsByTag("form");
                  for (Element form : forms) {
                      if((!badForms.contains(form.id()))&&(!formsList.contains(form.id())))
                      {
                       //System.out.println("hi: "+links.get(i));
                       String x = form.id();
                       //System.out.println(form.id());
                       //System.out.println(form.tag());
                       String text=form.html();
                       //If login form, then ignore
                       if(text.contains("type=\"password\""))
                       {
                           badForms.add(form.id());
                           System.out.println("BAD:"+form.id());
                           writer3.write(links.get(i)+"\t"+form.id()+"\n");
                           continue;
                       }
                       else
                       { formsList.add(form.id());
                         writer2.write(links.get(i)+"\t"+form.id()+"\n");
                         System.out.println("GOOD:"+form.id());
                         String att;
                         Attributes attributes = form.attributes();
                         System.out.println("Link:"+links.get(i));
                         //System.out.println("====================");
                         //System.out.println(text);
                         //If form has a field which has a field for author or its synoynm, then fill it
                         if(form.html().contains("author")||form.html().contains("Author"))
                         {
                             
                             for (Attribute attribute : attributes)
                         {
                             att=attribute.toString();
                             //System.out.println(att);
                           // if(att.contains("Author")||att.contains("author"))
                            //System.out.println(att);
                         }
                         }
                       }
                  }
                }
            }
            else
                break;
            
        }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Problem on last step\n");
        }
       /* Connection.Response loginForm = Jsoup.connect("http://dissexpress.umi.com/dxweb/search.html")
            .method(Connection.Method.GET)
            .execute();

                        
                for(Object c: concepts)       
                {   int count=0;
                    document =(Document) Jsoup.connect("http://dissexpress.umi.com/dxweb/results.html")
            .data("QryTxt", (String)c)
            .data("top_search","Search")
            .cookies(loginForm.cookies())            
            .post();
             
            Elements result;
             result=document.getElementsByClass("result_col");
             
                 //title=document.getElementsByClass("bold");
                 //author=document.getElementsByClass("italic");
                 for(Element p:result)
                 {
                     count++;
                     String arr[]=p.text().split(",");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     if(arr.length>=4)
                     db.insertBook(arr[0],arr[1],arr[2],arr[3]);
                             
                 }
               
            //Scan through all the pages containing the results
            Elements pages=document.getElementsByClass("pageSelector").select("a[href]");
            for(Element p:pages)
            {
                System.out.println("link:"+p.text());
                //Connecting to each page containing the results
                doc=Jsoup.connect(p.attr("abs:href")).data("QryTxt", (String)c)
            .data("top_search","Search")
            .cookies(loginForm.cookies())            
            .post();;
                //title=doc.getElementsByClass("bold");
                 //author=doc.getElementsByClass("italic");
                 for(Element p1:result)
                 {
                     
                     String arr[]=p1.text().split(",");
                     try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JavaApplication2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     if(arr.length>=4)
                     {db.insertBook(arr[0],arr[1],arr[2],arr[3]);
                     count++;
                     }       
                 }
               
            }
               System.out.println(c+": count:"+count); 
                //String text1 = document.body().text();
                //System.out.println(text1);
                //writer2.write(text1+"\n");
             } */
        
        writer.close();
        writer2.close();
        writer3.close();
       
    }
    
}
