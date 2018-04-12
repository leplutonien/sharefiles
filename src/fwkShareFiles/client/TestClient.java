package fwkShareFiles.client;

import fwkShareFiles.Constantes;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;




public class TestClient implements InterWin,Constantes{

    public TestClient() {
    }

    
    public static void main(String[] args) 
    {
       Client clt=new Client("127.0.0.1",4445,new TestClient());
       if(clt.connecter())
       {
           if(clt.authenticate("plutonien","cool"))
           {
               ShareFile [] sharedFiles = clt.getShareFile();
               int count=sharedFiles.length;           
               for (int i = 0; i < count; i++) 
               {           
                    
                    Affichage(sharedFiles[i]);
               }
               clt.DownloadFile(sharedFiles,"/home/marlin/test_dest/"); 
               //clt.disConnect();
           }
       }
    }
     
    public static void Affichage(ShareFile sf)
    {
       
        if(!sf.isDirectory())        
        { 
            
            System.out.println(sf.getName()+"==>"+sf.lengthToString()+"==>"+sf.getParent());           
        }
        else
        {
             ShareFile [] sharedFiles =sf.listFiles();      
             int count=sharedFiles.length;
             if(count==0)
             {
                 //dosssier vide
                 System.out.println("Dossier vide "+sf.getName());                 
             }
             else
             {
                 for (int i = 0; i < count; i++)
                 {
                    
                      Affichage(sharedFiles[i]);
                 }
             }
        }
        
    }

    @Override
    public void showMsgDlg(String Msg, String type) 
    {
        if(type.equals(ERROR_MESSAGE))
            System.err.println(Msg);
        else
            System.out.println(Msg);
        
    }

    @Override
    public void RefrechElement(Object obj) 
    {
        System.out.println((String)obj);
        
    }
}
