// import HMM.java
// import Stemmer.java


public class Analyzer {
    // this class is analysis the token from text and the input from user
    // if the string is Chinese,we do ChineseSegmentation
	// else if the string is English,we do EnglishStemmer.
	
	public String filter (String str) {
		// remove the character from string if it not a CN or EN.
		// we use whitespace instead of the char
		char [] charList = str.toCharArray();
		for (int i = 0 ; i<charList.length; i++) {
			if ( !isCNorEN(charList[i]) ) {
				charList[i] = ' ';
			}
		}
		return String.valueOf(charList);
	}
	
	public boolean isCNorEN(char c) {
		// whether a character is a CN or EN
		if (c<='z' && c>='a')
			return true;
		else if (c<='Z' && c>='A')
			return true;
		else if ((int)c<=40869 && (int)c>=19968)  
			//Chinese in unicode is from 19968 to 40869
	        return true;
		else return false;
	}
	
	public void print(String str) {
		// just a short print
		System.out.println(str);
	}
	
	public String splitAndDivide(String str) {
		// split the str and divide is to CNStr and ENStr.
		// first,use whitespace to split the input str and get a String list.
		// then,for every String in the list,divide it to CNstr and ENstr.
		// for ENstr we use Stemmer algorithm and for CNstr we use chineseSegmentation.
		// (HMM, CRF, dictionary and so on...)
		// in the end, we use a string named keyword to store the answer,then return it.
		String [] strList = str.trim().split(" ");
		String keyWord="";
		
		for (int i =0 ; i<strList.length; i++) {
			if (strList[i].length() == 0 || strList[i].equals(" "))
				continue; // remove the empty string
			//System.out.println(strList[i]);
			keyWord += (divideToCNEN(strList[i])+" "); // we use whitespace to distinguish
			//System.out.println("in splitAndDivide,keyWord="+keyWord);
		}
		return keyWord;		
	}
	
	public String divideToCNEN(String str) {
		// divide a String to CNString and ENString
		// CNString only contains Chinese and 
		// ENString only contains English
		char [] strCharList = str.toCharArray();
		char [] strCNList = new char[str.length()];
		char [] strENList = new char[str.length()];
		for (int i = 0 ; i<str.length(); i++) {
			if (strCharList[i]<=40869 && strCharList[i]>=19968)  
				//Chinese in unicode is from 19968 to 40869
		        strCNList[i] = strCharList[i];
			else strENList[i] = strCharList[i];
		}
		String CN = String.valueOf(strCNList);
		String EN = String.valueOf(strENList);
		//System.out.println("in divideToCNEN,cn="+CN+" en="+EN);
		return CN+" "+EN;
	}
	
	public String analysis (String keyWord, dictionarySegmentation CNCut, Stemmer myStemmer) {
		// The analysis function, the String "keyword" is contained keywords and distinguish.
		// by whitespace. we split the keyword string and get a string list.
		// for every string in the list, if it is Chinese,we use HMM or RF or dictionary.
		// if it is English, we use stemmer. 
		// Then, we get the answer.(also distinguish by whitespace)
		String [] keyWordList = keyWord.split(" ");
		String answer = "";
		for (int i = 0 ; i<keyWordList.length; i++) {
			String target = keyWordList[i].trim();
			if (target.length() == 0)
				continue; // ignore the empty string
			else if ((int)target.charAt(0)<=40869 && (int)target.charAt(0)>=19968) {
				// CN in unicide is from 19968 to 40869
				String str = CNCut.segmentation(new StringBuffer(target));
				answer += (str+" "); // use whitespace to distinguish
			}
			else { //must be EN
				char [] charList = target.toLowerCase().toCharArray();
			    for (int j = 0 ; j<charList.length; j++)
			    	myStemmer.add(charList[j]);
			    myStemmer.stem();
			    answer += (myStemmer.toString()+" "); // use whitespace to distinguish
			}
			//System.out.println("in analysisFunction:"+answer);
		}
		return answer.trim();
	}
	
	
	public static void main(String[] args) throws Exception{
		// Just test the Analyzer, to see whether it can work 
        Analyzer myAnalyzer = new Analyzer();
        //HMM myHMM = new HMM();
        //myHMM.loadData();  // we use dictionary instead of HMM
        dictionarySegmentation CNCut = new dictionarySegmentation();
        CNCut.loadDict();
        Stemmer myStemmer = new Stemmer();
        String str1 = "��һͳ�ش���Ϊһ���ȷ� ���귢���� ���е���ޡ�Apple ��Ҳ����Ҫ���κ�һ����ҵ���������̵�����ƴӲ���ˡ�Apple�����൱����һ��ʱ���ڣ����Լҵ�����ļ����������о������֡������ӵġ������ӵġ���Pad�ġ������Եġ��������ֻ������ֱ�ģ��Լ�����ʶ����Щ�����У�Force Touch iSight����ͷ������̰�ȫ�������������ܵе�Touch ID���������ȶ�ʡ���ֱ��ָ��µ�iOS ��tvOS��OS X��̬�����App Store Siri���缶�������ÿ����ȶ������Լ� ����ɱ��ﵣ�Appleȫ������ �й�����֧�����ǹȷۣ������������˵��Щ��ʱ���Ǻ�����ġ���Щ�������������ǿ���ƻ����ʵ����ǿ���˺δ�����������˵˵�������Ͻ��ܵ�һ���¼ƻ���Apple Upgrade Program ��Ȼ������ר�����������һ�£�ÿ����32�� �����ӵ�У�1.ÿ��һ����iPhone��������Ӫ�̲�����Ӫ�̡�����һ������ֳ�ʵ���ˡ������û��������������ǰ������û���iPhone������100%����������ȷ��׷��Appleһ���ӣ��������������� ����������3����Ʒ����û���˵ĺ����2.Apple Care+֧���ٿ���֮�����ᵽ��iPhone�޿ɱ������Щ���ƣ������32����Ĺ�����������iSight����ͷ��ƻ����1200�������� ������ Windows Phone����׿�ֻ�����׿ƽ�壨Surface�������������˰ɣ��� ���Ʋ�������������1300���� ������Ʊ����� ����ֻ��ˢ���ػ��߸�ʲô˫����ͷ֮�����ͷ�������������������� ������һ�ž��޵�����ͷ������ʹ�õļ����������ճ�������Ƭ*��������*��������Ƶ*���Ǻŵ� ������������顣==============�����������£�Force TouchApple WatchiPhone 6StvOS�Լ��Լ���Apple��δ����չ���������ڴ�==============�������ͬ���ش��ҽ�ʮ�ָ�л�����������ͬ���ӿ�����ٶȣ��Ͼ�ѧ��������л����֧�֡�������������һ�·ۡ��������ע�ң��ҽ������ṩ�����ʵĻش�==============���ش����� tnl.spaceվ��Tony1ee����վ�����Ƽ������߹�ͬά�� �����﷢���������������˽�š�";
        // this is test String 
        System.out.println("���ִ��ַ�����"+str1);
        String str2 = myAnalyzer.filter(str1);
        String  keyWord ;
        keyWord = myAnalyzer.splitAndDivide(str2);
        String answer = myAnalyzer.analysis(keyWord, CNCut, myStemmer);
        System.out.println(answer);
        String [] keyWordList = answer.trim().split(" ");
        for (int i = 0 ; i<keyWordList.length; i++) {
        	System.out.println("keyWord "+i+": "+keyWordList[i]);
        }   // print out the keyWord 
        
	}

}
