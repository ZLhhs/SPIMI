import java.io.File;
import java.util.Scanner;


public class SPIMIndexing {

	int MAXSIZE = 9999; // the max size of the red black tree
	static String tempIndexStorAddress = "c:/SPIMI/tempIndex/";
	static String finalIndexStorAddress ="c:/SPIMI/finalIndex/";
	int RBTreeSize = 0; // the size of the RBTree now! 
	int tempIndexCount = 0;
	int MAXTEMPINDEXNUMBER = 10000;
	String [] tempIndexList = new String [MAXTEMPINDEXNUMBER];
	
	
	public static void deleteOldTempIndex () {
		File [] oldFile = new File(tempIndexStorAddress).listFiles();
		for (int i = 0 ; i<oldFile.length; i++) {
			oldFile[i].delete();
		}   // delete the old tempIndex.
	}
	
	
	public boolean notEnoughMemory(String [] keyWordList) {
		if (RBTreeSize + keyWordList.length > MAXSIZE)
			return true;
		return false;
	}
	
	public void printTempIndexName() {
		System.out.println("This is the temp index name:");
		for (int i = 0 ; i<tempIndexCount; i++) {
			System.out.println(tempIndexList[i]);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        long time1 = System.currentTimeMillis();
		
		SPIMIndexing index = new SPIMIndexing();
		dictionarySegmentation CNCut = new dictionarySegmentation ();
		Stemmer ENCut = new Stemmer ();
		Analyzer myAnalyzer = new Analyzer();
		RBTree RBT = new RBTree();
		CNCut.loadDict(); 
		
		String targetFileAddress = "C:/webData/";
		File targetFileFolder = new File(targetFileAddress);
		String [] targetFileList = targetFileFolder.list();
		File nowTargetFile;
		Scanner input;
		
		
		deleteOldTempIndex();
		for (int i = 0 ; i<targetFileList.length; i++) {
			System.out.println(targetFileList[i]);
			
			nowTargetFile = new File(targetFileAddress + targetFileList[i]);
			input = new Scanner(nowTargetFile);
			String content = input.nextLine();
			String noUse = input.nextLine();noUse = input.nextLine();
			noUse = input.nextLine();noUse = input.nextLine();noUse = input.nextLine();
			while (input.hasNext()) 
				content += (" " + input.nextLine());
			System.out.println(content);
			
			String keyWord = myAnalyzer.analysis(myAnalyzer.splitAndDivide(myAnalyzer.filter(content)), CNCut, ENCut);
		    String [] keyWordList = keyWord.split(" ");
		    
		    if (keyWordList.length > index.MAXSIZE)
		    	System.out.println("-----Doc is too bigger! This Doc will be ignore!-----\n\n");
		    else {
		    	if (index.notEnoughMemory(keyWordList)) 	    
		    	    index.clearRBTreeAndStoreTempIndex(); // flush the RBTree and store the temp index
		        for (int j = 0 ; j<keyWordList.length; j++) 
		    	    // create a new RBTree and insert node
		        	RBT.insertNode(targetFileList[i], Integer.parseInt(
    	    				new StringBuffer(targetFileList[i]).substring(0, 8).toString()));
		       
		        System.out.println("-----Doc has been indexing-----\n\n");
		    }
		}
		if (index.RBTreeSize != 0) // still have some node on the tree
		    index.clearRBTreeAndStoreTempIndex();  // create index for the least id list
		index.printTempIndexName ();
		System.out.println("temp indexing successful~");
		
		//////////// temp indexing successful //////////////
		
		//index.createFinalIndex();// combine all temp index
		System.out.println("final indexing successful~");
		System.out.println(System.currentTimeMillis() - time1);
	}        
	

}


/* 
 *  
 * while (haveFile) {
 *     list = Analyzer(file)  (dictionarySegmentation + stemmer)
 *     if (tooBig)  print and return
 *     if (memoryFull) {
 *         indexStore()
 *     }
 *     Insert List to RBTree
 * }
 * combineAllTempIndex()
 * 
 * 
 *     
 * 
 * */
 