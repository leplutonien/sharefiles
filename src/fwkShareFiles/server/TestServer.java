
package fwkShareFiles.server;

import fwkShareFiles.Constantes;
import fwkShareFiles.InterWin;
import fwkShareFiles.ShareFile;

public class TestServer implements InterWin,Constantes{

    public TestServer() {
    }    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {         
         ShareFile fl=new ShareFile("/home/marlin/Preferences_installe_Linux/"); 
         //fl.addwhoCanNotDownload("127.0.0.1");
         Server myServ = new Server(4445,new TestServer());
         myServ.addElementsShare(new ShareFile[]{fl});
         myServ.launchServer();
        
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
        
    }
}
